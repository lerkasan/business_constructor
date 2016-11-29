package ua.com.brdo.business.constructor.controller;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.QuestionOption;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.QuestionOptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class QuestionnaireTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private OptionService optionService;

    @Autowired
    private QuestionOptionService questionOptionService;

    private MockMvc mockMvc;

    private TestContextManager testContextManager;

    private final String QUESTIONS_URL = "/api/questions/";

    private final String OPTIONS_URL = "/api/options/";

    private final String validQuestionDataJson = "{\"inputType\":{\"title\":\"check\"},\"text\":\"Who are you?\"}";

    private final String validOptionDataJson = "{\"title\":\"My option\"}";

    private static final int NON_EXISTENT_ID = 10000;

    private final String EXPERT = "EXPERT";

    private final String USER = "USER";

    private Question question;

    private Option option;

    @Before
    @SneakyThrows
    public void setup() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        question = new Question();
        question.setText("Who are you?");
        question = questionService.create(question);

        option = new Option();
        option.setTitle("My option");
        option = optionService.create(option);
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldReturnQuestionsList() {
        mockMvc.perform(get(QUESTIONS_URL))
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectQuestionCreationByUserTest() {
        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithDefaultCheckboxTest() {
        String validQuestionDataJson = "{\"text\":\"Who are you?\"}";

        this.mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
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
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value("What is your name?")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldDeleteQuestionByExpertTest() {
        this.mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectUpdateQuestionByUnauthorizedTest() {
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectUpdateQuestionByUserTest() {
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        this.mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectDeleteQuestionByUnauthorizedTest() {
         this.mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectDeleteQuestionByUserTest() {
        this.mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectUpdateNonExistentQuestionTest() {
        this.mockMvc.perform(
                put(QUESTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionDataJson))
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
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question with id = " + NON_EXISTENT_ID + " does not exist.")));
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldReturnOptionsList() {
        mockMvc.perform(get(OPTIONS_URL))
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
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectOptionCreationByUserTest() {
        this.mockMvc.perform(
                post(OPTIONS_URL).contentType(APPLICATION_JSON).content(validOptionDataJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldUpdateOptionByExpertTest() {
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL + option.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value("Updated option")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldDeleteOptionByExpertTest() {
        this.mockMvc.perform(
                delete(OPTIONS_URL + option.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectUpdateOptionByUnauthorizedTest() {
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL + option.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectUpdateOptionByUserTest() {
        String updatedTextField = "{\"title\":\"Updated option\"}";

        this.mockMvc.perform(
                put(OPTIONS_URL + option.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectDeleteOptionByUnauthorizedTest() {
        this.mockMvc.perform(
                delete(OPTIONS_URL + option.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldRejectDeleteOptionByUserTest() {
        this.mockMvc.perform(
                delete(OPTIONS_URL + option.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectUpdateNonExistentOptionTest() {
        this.mockMvc.perform(
                put(OPTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validOptionDataJson))
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
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Option with id = " + NON_EXISTENT_ID + " does not exist.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldAddOptionToGivenQuestionByExpertTest() {
        this.mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + "/options").contentType(APPLICATION_JSON).content(validOptionDataJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.title").value("My option")));
    }

    @Ignore
    @Test
    @Rollback(false)
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRemoveOptionFromGivenQuestionByExpertTest() {
        question.addOption(option);
        question = questionService.update(question);
        List<QuestionOption> options = new ArrayList<>(question.getOptions());

        this.mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + "/options/" + options.get(0).getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveNonExistentOptionFromGivenQuestionByExpertTest() {
        question.addOption(option);
        question = questionService.update(question);

        this.mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + "/options/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveOptionFromNonExistentQuestionByExpertTest() {
        this.mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID + "/options/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }
}