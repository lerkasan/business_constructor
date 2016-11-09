package ua.com.brdo.business.constructor.controller;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).dispatchOptions(true).build();
    }

    @SneakyThrows
    @Test
    @Rollback
    public void successfulRegisterTest() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test1@mail.com");
        userDto.setPassword("123456789");
        userDto.setPasswordConfirm("123456789");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegister_WrongEmailTest() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("testcom");
        userDto.setPassword("123456789");
        userDto.setPasswordConfirm("123456789");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"message\":\"Incorrect format of e-mail.\"}"));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegister_PasswordMismatch_Test() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test2@mail.com");
        userDto.setPassword("12345678");
        userDto.setPasswordConfirm("123456789");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"message\":\"Password and password confirmation don't match.\"}"));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegister_PasswordShort_Test() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test3@mail.com");
        userDto.setPassword("12345");
        userDto.setPasswordConfirm("12345");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"message\":\"Password length must be between 8 and 100 characters.\"}"));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegister_PasswordUnallowedChars_Test() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("test4@mail.com");
        userDto.setPassword("абвгдежзийкл");
        userDto.setPasswordConfirm("абвгдежзийкл");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"message\":\"Password could include upper and lower case latin letters, numerals (0-9) and special symbols.\"}"));
    }

    @SneakyThrows
    @Test
    public void unsuccessfulRegister_EmailNotUnique_Test() {
        //given
        UserDto userDto = new UserDto();
        userDto.setEmail("user1@mail.com");
        userDto.setPassword("1234567890");
        userDto.setPasswordConfirm("1234567890");
        Gson gson = new Gson();
        String user = gson.toJson(userDto);

        //when
        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"message\":\"User with this e-mail is already registered.\"}"));
    }
}
