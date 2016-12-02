package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.Application;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = {Application.class})
public class RegistrationTest {

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
    @SneakyThrows
    public void setup() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        invalidUserData.put("username", "test1@mail.com");
        invalidUserData.put("email", "test1@mail.com");
        invalidUserData.put("rawPassword", "123456789");
    }

    @Test
    @SneakyThrows
    public void shouldSuccessfullyRegisterTest() {
        Map<String, String> validUserData = new HashMap<>();
        validUserData.put("username", "test@mail.com");
        validUserData.put("email", "test@mail.com");
        validUserData.put("rawPassword", "123456789");
        String validUserDataJson = jsonMapper.writeValueAsString(validUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(validUserDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.rawPassword").doesNotExist());
    }

    @Test
    @Parameters({"testmail", "user@localhost"})
    @SneakyThrows
    public void shouldRejectRegisterWrongEmailTest(String invalidEmail) {
        invalidUserData.put("email", invalidEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Incorrect format of e-mail."))));
    }

    @Test
    @Parameters({"", "1", "1234", "1234567", "123456789012345678901234567890123"})
    @SneakyThrows
    public void shouldRejectRegisterPasswordShortTest(String invalidPassword) {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password length must be between 8 and 32 characters."))));
    }

    @Test
    @Parameters({"абвгдежзийкл", "abcdФz923435m"})
    @SneakyThrows
    public void shouldRejectRegisterPasswordUnallowedCharsTest(String invalidPassword) {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Password could include upper and lower case latin letters, numerals (0-9) and special symbols."));
    }

    @Test
    @SneakyThrows
    public void shouldRejectRegisterEmailNotUniqueTest() {
        String nonUniqueEmail = "some_user1@mail.com";
        invalidUserData.put("email", nonUniqueEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("User with this e-mail is already registered. Try another e-mail."));
    }

    @Test
    @SneakyThrows
    public void shouldRejectRegisterReceiveNoPasswordTest() {
        String invalidUserDataJson = "{\"email\": \"email@mail.com\"}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password field is required."))));
    }

    @Test
    @SneakyThrows
    public void shouldRejectRegisterReceiveNoEmailTest() {
        String invalidUserDataJson = "{\"password\": \"12345678901\"}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("E-mail field is required."))));
    }

    @Test
    @SneakyThrows
    public void shouldRejectRegisterReceiveEmptyJsonTest() {
        String invalidEmplyJson = "{}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidEmplyJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((containsString("Password field is required."))))
                .andExpect(content().string((containsString("E-mail field is required."))));
    }

    @Test
    @Parameters({"vbvsbb", "{vbvsbb}"})
    @SneakyThrows
    public void shouldRejectRegisterReceiveNotJsonTest(String notJson) {
        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(notJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.message").value("Received malformed JSON."));
    }

    @WithMockUser
    @Test
    public void shouldReturnFalseUniqueEmailTest() throws Exception {
        createUser();
        this.mockMvc.perform(get("/api/users/available").param("email", user.getEmail()).accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("false"));
    }

    @Test
    public void shouldReturnTrueUniqueEmailTest() throws Exception {
        this.mockMvc.perform(get("/api/users/available").param("email", "noSuch@email.com").accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("true"));
    }
}
