package ua.com.brdo.business.constructor.controller.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    private static final String USERS_API_URL = "/api/users";
    private static final String NON_EXISTENT_EMAIL = "non.existent@mail.com";
    private static final String EXISTENT_EMAIL = "some_user1@mail.com";

    @Autowired
    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Test
    public void shouldCreateUser() throws Exception {
        Map<String, String> userDetails = new HashMap<>();
        userDetails.put("email", "email@email.com");
        userDetails.put("rawPassword", "password123");
        String userAsJSON = json(userDetails);

        mockMvc.perform(post(USERS_API_URL).content(userAsJSON).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(USERS_API_URL)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldEmailBeAvailable() throws Exception {
        mockMvc.perform(get(USERS_API_URL + "/available").param("email", NON_EXISTENT_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));
    }

    @Test
    public void shouldEmailBeUnavailable() throws Exception {
        mockMvc.perform(get(USERS_API_URL + "/available").param("email", EXISTENT_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.FALSE.toString()));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get(USERS_API_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON_UTF8, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
