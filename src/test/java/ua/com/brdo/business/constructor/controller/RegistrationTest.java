package ua.com.brdo.business.constructor.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.Application;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Transactional
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = {Application.class})
public class RegistrationTest {

    private static final String REGISTRATION_URL = "/api/users";

    @ClassRule
    public static final SpringClassRule SCR = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private Map<String, String> invalidUserData = new HashMap<>();

    private TestContextManager testContextManager;

    private static User user = new User();

    public void createUser() {
        user.setUsername("UserFromUsersController");
        user.setEmail("UserFrom@UsersController.com");
        user.setPassword("password");
        user.setRawPassword("password".toCharArray());
        userRepo.saveAndFlush(user);
    }

    @Before
    public void setup() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        invalidUserData.put("username", "test1@mail.com");
        invalidUserData.put("email", "test1@mail.com");
        invalidUserData.put("rawPassword", "123456789");
    }

    @Test
    public void shouldSuccessfullyRegisterTest() throws Exception {
        Map<String, String> validUserData = new HashMap<>();
        validUserData.put("username", "test@mail.com");
        validUserData.put("email", "test@mail.com");
        validUserData.put("rawPassword", "123456789");
        String validUserDataJson = jsonMapper.writeValueAsString(validUserData);

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(validUserDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.rawPassword").doesNotExist());
    }

    @Test
    @Parameters({"testmail", "user@localhost"})
    public void shouldRejectRegisterWrongEmailTest(String invalidEmail) throws Exception {
        invalidUserData.put("email", invalidEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Incorrect format of e-mail."))));
    }

    @Test
    @Parameters({"", "1", "1234", "1234567", "123456789012345678901234567890123"})
    public void shouldRejectRegisterPasswordShortTest(String invalidPassword) throws Exception {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password length must be between 8 and 32 characters."))));
    }

    @Test
    @Parameters({"абвгдежзийкл", "abcdФz923435m"})
    public void shouldRejectRegisterPasswordUnallowedCharsTest(String invalidPassword) throws Exception {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Password can include upper and lower case latin letters, numerals (0-9) and special symbols."));
    }

    @Test
    public void shouldRejectRegisterEmailNotUniqueTest() throws Exception {
        String nonUniqueEmail = "some_user1@mail.com";
        invalidUserData.put("email", nonUniqueEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("User with this e-mail is already registered. Try another e-mail."));
    }

    @Test
    public void shouldRejectRegisterReceiveNoPasswordTest() throws Exception {
        String invalidUserDataJson = "{\"email\": \"email@mail.com\"}";

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password field is required."))));
    }

    @Test
    public void shouldRejectRegisterReceiveNoEmailTest() throws Exception {
        String invalidUserDataJson = "{\"password\": \"12345678901\"}";

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("E-mail field is required."))));
    }

    @Test
    public void shouldRejectRegisterReceiveEmptyJsonTest() throws Exception {
        String invalidEmplyJson = "{}";

        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(invalidEmplyJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password field is required."))))
                .andExpect(content().string((containsString("E-mail field is required."))));
    }

    @Test
    @Parameters({"vbvsbb", "{vbvsbb}"})
    public void shouldRejectRegisterReceiveNotJsonTest(String notJson) throws Exception {
        mockMvc.perform(
                post(REGISTRATION_URL).contentType(APPLICATION_JSON).content(notJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Received malformed JSON."));
    }

    @WithMockUser
    @Test
    public void shouldReturnFalseUniqueEmailTest() throws Exception {
        createUser();
        mockMvc.perform(get("/api/users/available").param("email", user.getEmail()).accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("false"));
    }

    @Test
    public void shouldReturnTrueUniqueEmailTest() throws Exception {
        mockMvc.perform(get("/api/users/available").param("email", "noSuch@email.com").accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("true"));
    }
}
