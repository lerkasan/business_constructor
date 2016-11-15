package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class RegisterControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    Map<String, String> userData = new HashMap<>();

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
        userData.put("username", "test@mail.com");
        userData.put("password", "12345678");
        userData.put("email", "test@mail.com");
    }

    @SneakyThrows
    @Test
    public void successfulRegisterTest() {
        String userDataJson = jsonMapper.writeValueAsString(userData);
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterWrongEmailTest() {
        userData.put("email", "testmail");
        String userDataJson = jsonMapper.writeValueAsString(userData);
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Incorrect format of e-mail."))));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterPasswordShortTest() {
        userData.put("email", "test1@mail.com");
        userData.put("password", "1234");
        String userDataJson = jsonMapper.writeValueAsString(userData);
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password length must be between 8 and 100 characters."))));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterPasswordUnallowedCharsTest() {
        userData.put("email", "test1@mail.com");
        userData.put("password", "абвгдежзийкл");
        String userDataJson = jsonMapper.writeValueAsString(userData);
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Password could include upper and lower case latin letters, numerals (0-9) and special symbols."));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterEmailNotUniqueTest() {
        userData.put("email", "some_user1@mail.com");
        String userDataJson = jsonMapper.writeValueAsString(userData);
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(userDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User with this e-mail is already registered."));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterReceiveNoPasswordTest() {
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"email@mail.com\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password field is required."))));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterReceiveNoEmailTest() {
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content("{\"password\": \"12345678901\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("E-mail field is required."))));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterReceiveEmptyJsonTest() {
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string((Matchers.containsString("Password field is required."))))
                .andExpect(content().string((Matchers.containsString("E-mail field is required."))));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterReceiveMalformedJsonTest() {
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content("{vfdbdbd}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Received malformed JSON."));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegisterReceiveNotJsonTest() {
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content("vbvsbb"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Received malformed JSON."));
    }
}