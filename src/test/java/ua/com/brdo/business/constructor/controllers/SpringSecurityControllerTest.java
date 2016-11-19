package ua.com.brdo.business.constructor.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SpringSecurityControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }


    @Test
    public void expectUnauthorizedStatus() throws Exception {
        mvc.perform(get("/app/adminpanel"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/app/expertpanel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void invalidUsernamePassword() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "user")
                .param("password", "invalid");
        mvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void validUsernamePassword() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "admin")
                .param("password", "admin");
        mvc.perform(request)
                .andExpect(status().isOk());
        request = post("/login")
                .param("username", "expert")
                .param("password", "expert");
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void expertCannotAccessAdminPanel() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "expert")
                .param("password", "expert");
        mvc.perform(request)
                .andExpect(status().isOk());
        mvc.perform(get("/app/adminpanel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void adminCanAccessAdminPanel() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "admin")
                .param("password", "admin");
        mvc.perform(request)
                .andExpect(status().isOk());
        mvc.perform(get("/app/adminpanel"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void expertCanAccessExpertPanel() throws Exception {
        RequestBuilder request = post("/login")
                .param("username", "expert")
                .param("password", "expert");
        mvc.perform(request)
                .andExpect(status().isOk());
        mvc.perform(get("/app/expertpanel"))
                .andExpect(status().isUnauthorized());
    }
}
