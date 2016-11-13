package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        UserDto userDto = new UserDto();
        userDto.setEmail("test1@mail.com");
        userDto.setPassword("123456789");
        userDto.setPasswordConfirm("123456789");
        ObjectMapper jsonMapper = new ObjectMapper();
        String user = jsonMapper.writeValueAsString(userDto);

        this.mockMvc.perform(
                post("/register").contentType(MediaType.APPLICATION_JSON).content(user))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}