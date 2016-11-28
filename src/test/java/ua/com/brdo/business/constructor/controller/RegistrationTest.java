package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.Application;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private Map<String, String> invalidUserData = new HashMap<>();

    private TestContextManager testContextManager;

    @Before
    @SneakyThrows
    public void setup() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        invalidUserData.put("username", "test1@mail.com");
        invalidUserData.put("email", "test1@mail.com");
        invalidUserData.put("rawPassword", "123456789");
    }

    @SneakyThrows
    @Test
    @Rollback
    public void shouldSuccessfullyRegisterTest() {
        Map<String, String> validUserData = new HashMap<>();
        validUserData.put("username", "test@mail.com");
        validUserData.put("email", "test@mail.com");
        validUserData.put("rawPassword", "123456789");
        String validUserDataJson = jsonMapper.writeValueAsString(validUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(validUserDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @SneakyThrows
    @Test
    @Parameters({"testmail", "user@localhost"})
    public void shouldRejectRegisterWrongEmailTest(String invalidEmail) {
        invalidUserData.put("email", invalidEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Incorrect format of e-mail."))));
    }

    @SneakyThrows
    @Test
    @Parameters({"", "1", "1234", "1234567", "123456789012345678901234567890123"})
    public void shouldRejectRegisterPasswordShortTest(String invalidPassword) {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password length must be between 8 and 32 characters."))));
    }

    @SneakyThrows
    @Test
    @Parameters({"абвгдежзийкл", "abcdФz923435m"})
    public void shouldRejectRegisterPasswordUnallowedCharsTest(String invalidPassword) {
        invalidUserData.put("rawPassword", invalidPassword);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password could include upper and lower case latin letters, numerals (0-9) and special symbols."));
    }

    @SneakyThrows
    @Test
    public void shouldRejectRegisterEmailNotUniqueTest() {
        String nonUniqueEmail = "some_user1@mail.com";
        invalidUserData.put("email", nonUniqueEmail);
        String invalidUserDataJson = jsonMapper.writeValueAsString(invalidUserData);

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with this e-mail is already registered. Try another e-mail."));
    }

    @SneakyThrows
    @Test
    public void shouldRejectRegisterReceiveNoPasswordTest() {
        String invalidUserDataJson = "{\"email\": \"email@mail.com\"}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password field is required."))));
    }

    @SneakyThrows
    @Test
    public void shouldRejectRegisterReceiveNoEmailTest() {
        String invalidUserDataJson = "{\"password\": \"12345678901\"}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidUserDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("E-mail field is required."))));
    }

    @SneakyThrows
    @Test
    public void shouldRejectRegisterReceiveEmptyJsonTest() {
        String invalidEmplyJson = "{}";

        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(invalidEmplyJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password field is required."))))
                .andExpect(content().string((Matchers.containsString("E-mail field is required."))));
    }

    @SneakyThrows
    @Test
    @Parameters({"vbvsbb", "{vbvsbb}"})
    public void shouldRejectRegisterReceiveNotJsonTest(String notJson) {
        this.mockMvc.perform(
                post("/register").contentType(APPLICATION_JSON).content(notJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Received malformed JSON."));
    }
}
