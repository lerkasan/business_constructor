package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.Application;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Role;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class QuestionnaireTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private OptionService optionService;

    private MockMvc mockMvc;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private Map<String, String> invalidUserData = new HashMap<>();

    private TestContextManager testContextManager;

    private final String QUESTIONS_URL = "/api/questions/";

    private final String OPTIONS_URL = "/api/options/";

    private final String validQuestionDataJson = "{\"inputType\":{\"title\":\"check\"},\"text\":\"Who are you?\"}";

    private final String validOptionDataJson = "{\"title\":\"My option\"}";

    private static final int NON_EXISTENT_ID = 10000;

    private final String EXPERT = "EXPERT";

    private final String USER = "USER";

    User dummyExpert = new User();
    User dummyUser= new User();

    RequestPostProcessor expertAccount;

    RequestPostProcessor userAccount;

    @Before
    @SneakyThrows
    public void setup() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        dummyExpert.setId(2L);
        dummyExpert.setEmail("expert@mail.com");
        dummyExpert.setUsername("expert");
        dummyExpert.setRawPassword("12345678");
        dummyExpert.grantAuthorities(new Role(2L, "ROLE_EXPERT"));

        dummyUser.setId(1L);
        dummyUser.setEmail("user@mail.com");
        dummyUser.setUsername("user");
        dummyUser.setPassword("12345678");
        dummyUser.grantAuthorities(new Role(1L, "ROLE_USER"));

        expertAccount = user("expert");
        userAccount = SecurityMockMvcRequestPostProcessors.httpBasic(dummyUser.getEmail(), dummyUser.getPassword());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldReturnQuestionsList() {
        mockMvc.perform(get(QUESTIONS_URL))
               // .with(user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionByExpertTest() {
        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
//                .with(expertAccount))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value("Who are you?")))
                .andExpect((jsonPath("$.inputType.title").value("check")));
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectQuestionCreationByUnauthorizedTest() {
        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                //.with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectQuestionCreationByUserTest() {
        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
              //  .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithDefaultCheckboxTest() {
        String validQuestionDataJson = "{\"text\":\"Who are you?\"}";

        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                //.with(expertAccount))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value("Who are you?")))
                .andExpect((jsonPath("$.inputType.title").value("checkbox")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldUpdateQuestionByExpertTest() {
        Question questionToUpdate = new Question();
        questionToUpdate.setText("Who are you?");
        questionToUpdate = questionService.create(questionToUpdate);
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL+questionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
               // .with(expertAccount))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value("What is your name?")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldDeleteQuestionByExpertTest() {
        Question questionToDelete = new Question();
        questionToDelete.setText("Who are you?");
        questionToDelete = questionService.create(questionToDelete);

        this.mockMvc.perform(
                delete(QUESTIONS_URL+questionToDelete.getId()))
               // .with(expertAccount))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectUpdateQuestionByUnauthorizedTest() {
        Question questionToUpdate = new Question();
        questionToUpdate.setText("Who are you?");
        questionToUpdate = questionService.create(questionToUpdate);
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL+questionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
               // .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectUpdateQuestionByUserTest() {
        Question questionToUpdate = new Question();
        questionToUpdate.setText("Who are you?");
        questionToUpdate = questionService.create(questionToUpdate);
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL+questionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
              //  .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectDeleteQuestionByUnauthorizedTest() {
        Question questionToDelete = new Question();
        questionToDelete.setText("Who are you?");
        questionToDelete = questionService.create(questionToDelete);

        this.mockMvc.perform(
                delete(QUESTIONS_URL+questionToDelete.getId()))
               // .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectDeleteQuestionByUserTest() {
        Question questionToDelete = new Question();
        questionToDelete.setText("Who are you?");
        questionToDelete = questionService.create(questionToDelete);

        this.mockMvc.perform(
                delete(QUESTIONS_URL+questionToDelete.getId()))
               // .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectUpdateNonExistentQuestionTest() {
        this.mockMvc.perform(
                put(QUESTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                //.with(expertAccount))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question with id = " + NON_EXISTENT_ID + " does not exist.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectDeleteNonExistentQuestionTest() {
        this.mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID))
               // .with(expertAccount))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question with id = " + NON_EXISTENT_ID + " does not exist.")));
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldReturnOptionsList() {
        mockMvc.perform(get(OPTIONS_URL))
              //  .with(user("user")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateOptionByExpertTest() {
        this.mockMvc.perform(
                post(OPTIONS_URL).contentType(APPLICATION_JSON).content(validOptionDataJson))
               //         .with(expertAccount))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.title").value("My option")));
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectOptionCreationByUnauthorizedTest() {
        this.mockMvc.perform(
                post(OPTIONS_URL).contentType(APPLICATION_JSON).content(validOptionDataJson))
                //        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectOptionCreationByUserTest() {
        this.mockMvc.perform(
                post(OPTIONS_URL).contentType(APPLICATION_JSON).content(validOptionDataJson))
                 //       .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldUpdateOptionByExpertTest() {
        Option optionToUpdate = new Option();
        optionToUpdate.setTitle("My option");
        optionToUpdate = optionService.create(optionToUpdate);
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL+optionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
               //         .with(expertAccount))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value("Updated option")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldDeleteOptionByExpertTest() {
        Option optionToDelete = new Option();
        optionToDelete.setTitle("Option to delete");
        optionToDelete = optionService.create(optionToDelete);

        this.mockMvc.perform(
                delete(OPTIONS_URL+optionToDelete.getId()))
                //        .with(expertAccount))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectUpdateOptionByUnauthorizedTest() {
        Option optionToUpdate = new Option();
        optionToUpdate.setTitle("My option");
        optionToUpdate = optionService.create(optionToUpdate);
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL+optionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                 //       .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectUpdateOptionByUserTest() {
        Option optionToUpdate = new Option();
        optionToUpdate.setTitle("My option");
        optionToUpdate = optionService.create(optionToUpdate);
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL+optionToUpdate.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                 //       .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectDeleteOptionByUnauthorizedTest() {
        Option optionToDelete = new Option();
        optionToDelete.setTitle("Option to delete");
        optionToDelete = optionService.create(optionToDelete);

        this.mockMvc.perform(
                delete(OPTIONS_URL+optionToDelete.getId()))
                //        .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectDeleteOptionByUserTest() {
        Option optionToDelete = new Option();
        optionToDelete.setTitle("Option to delete");
        optionToDelete = optionService.create(optionToDelete);

        this.mockMvc.perform(
                delete(OPTIONS_URL+optionToDelete.getId()))
                 //       .with(userAccount))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectUpdateNonExistentOptionTest() {
        this.mockMvc.perform(
                put(OPTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validOptionDataJson))
                //        .with(expertAccount))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Option with id = " + NON_EXISTENT_ID + " does not exist.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectDeleteNonExistentOptionTest() {
        this.mockMvc.perform(
                delete(OPTIONS_URL + NON_EXISTENT_ID))
                //        .with(expertAccount))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Option with id = " + NON_EXISTENT_ID + " does not exist.")));
    }
}