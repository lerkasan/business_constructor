package ua.com.brdo.business.constructor.controller.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UsersControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    private String noSuchEmailCreator() {
        String noSuchEmail = new String("noSuch@email.com");
        while (userRepo.countByEmailIgnoreCase(noSuchEmail) > 0) {
            noSuchEmail += "noSuch";
        }
        return noSuchEmail;
    }

    private static User user = new User();

    public void createUser() {
        user.setUsername("UserFromUsersController");
        user.setEmail("UserFrom@UsersController.com");
        user.setPassword("password");
        user.setRawPassword("password");
        userRepo.saveAndFlush(user);
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void shouldReturnFalseUniqueEmailTest() throws Exception {
        createUser();
        this.mockMvc.perform(get("/api/users/available").param("email", user.getEmail()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(Boolean.FALSE.toString()));
    }

    @Test
    public void shouldReturnTrueUniqueEmailTest() throws Exception {
        this.mockMvc.perform(get("/api/users/available").param("email", noSuchEmailCreator()).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(Boolean.TRUE.toString()));
    }
}