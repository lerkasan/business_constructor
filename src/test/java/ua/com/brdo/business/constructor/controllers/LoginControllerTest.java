package ua.com.brdo.business.constructor.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.entity.User;
import ua.com.brdo.business.constructor.repositories.UsersRepository;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class) @SpringBootTest @WebAppConfiguration public class LoginControllerTest {

    @Autowired UsersRepository users;

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Before public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test public void getSecurePageWithNonExistUser() throws Exception {
        mvc.perform(get("/panel").with(user("user").password("pass").roles("USER", "ADMIN")))
            .andExpect(authenticated());
    }

    @Test public void getSecurePageWithExistUser() throws Exception {
        User client = users.findByUsername("user");
        UserDetails userDetails =
            new org.springframework.security.core.userdetails.User(client.getUsername(),
                client.getPassword(), client.getRoles());
        mvc.perform(get("/panel").with(user(userDetails))).andExpect(authenticated());
    }

    @Test public void getSecurePageNonLogin() throws Exception {
        mvc.perform(get("/panel").with(anonymous())).andExpect(unauthenticated());
    }

    @Test public void testingFormBasedAuthentication() throws Exception {
        mvc.perform(formLogin("/login").user("user").password("password"))
            .andExpect(authenticated().withUsername("user"));
    }

    @Test public void logoutTest() throws Exception {
        mvc.perform(logout("/logout")).andExpect(redirectedUrl("/login?logout"));
    }

    @Test public void unauthenticatedTest() throws Exception {
        mvc.perform(formLogin().password("invalid")).andExpect(unauthenticated());
    }

    @Test public void authenticationFailed() throws Exception {
        mvc.perform(formLogin("/login").user("user", "notfound").password("pass", "invalid"))
            .andExpect(redirectedUrl("/login?error")).andExpect(unauthenticated());
    }

    @Test public void ifCsrfExistInFormLogin() throws Exception{
        mvc.perform(post("/login").with(csrf()));
    }

    @Test public void providingInvalid() throws Exception{
        mvc.perform(post("/login").with(csrf().useInvalidToken()));
    }
}
