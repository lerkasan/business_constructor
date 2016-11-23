package ua.com.brdo.business.constructor.controller.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.repository.UserRepository;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 Created by Anton on 22.11.2016.
 */
@SpringBootTest
@RunWith(SpringRunner.class)

public class UsersControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     Pseudo-create pseudo-user in @MockBean userRepository with email = "mockUser@mail.com",
     by simulating answer of the method countByEmailAllIgnoreCase(String email) in
     UserRepository.class
     */
    @MockBean
    private UserRepository userRepository;

    @Before
    public void initUser() {
        String email = "mockUser@mail.com";
        when(userRepository.countByEmailAllIgnoreCase(anyString())).thenAnswer(new Answer<Long>() {
            @Override
            public Long answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                if (((String) args[0]).equalsIgnoreCase(email)) return 1L;
                else return 0L;
            }
        });
    }

    @Test
    public void availableEmailTestFalse() throws Exception {
        this.mockMvc.perform(get("/api/users/available?email=MocKuSeR@MaIl.com").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("false"));
    }

    @Test
    public void availableEmailTestTrue() throws Exception {
        this.mockMvc.perform(get("/api/users/available?email=user1@mail.com").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string("true"));
    }
}