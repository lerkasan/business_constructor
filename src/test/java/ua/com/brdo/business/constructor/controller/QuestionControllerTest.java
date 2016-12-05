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
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

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
public class QuestionControllerTest {

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

    private final String validQuestionDataJson = "{\"text\":\"Who are you?\"}";

    private final String validOptionDataJson = "{\"title\":\"My option\"}";

    private static final int NON_EXISTENT_ID = 10000;

    private final String EXPERT = "EXPERT";

    private final String USER = "USER";

    private final String ADMIN = "ADMIN";

    private final String questionText = "Who are you?";

    private final String optionTitle = "My option";

    private Question question;

    private Option option;

    @Before
    @SneakyThrows
    public void setup() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        question = new Question();
        question.setText(questionText);
        question = questionService.create(question);

        option = new Option();
        option.setTitle(optionTitle);
        option = optionService.create(option);
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    @SneakyThrows
    public void shouldShowQuestion() {
        mockMvc.perform(get(QUESTIONS_URL + question.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value(questionText)));
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    @SneakyThrows
    public void shouldRejectShowNonExistentQuestion() {
        mockMvc.perform(get(QUESTIONS_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question was not found.")));
    }

    @Test
    @WithMockUser(roles = {USER})
    @SneakyThrows
    public void shouldShowQuestionListToUser() {
        mockMvc.perform(get(QUESTIONS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldShowQuestionListToExpert() {
        mockMvc.perform(get(QUESTIONS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionByExpertTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.inputType").value("SINGLE_CHOICE")));
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectQuestionCreationByUnauthorizedTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectQuestionCreationNotByExpertTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithDefaultCheckboxTest() {
        String validQuestionDataJson = "{\"text\":\"Who are you?\"}";

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.inputType").value("SINGLE_CHOICE")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldUpdateQuestionByExpertTest() {
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value("What is your name?")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldDeleteQuestionByExpertTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectUpdateQuestionByUnauthorizedTest() {
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectUpdateQuestionByNotExpertTest() {
        String updatedTextField = "{\"text\":\"What is your name?\"}";

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @SneakyThrows
    public void shouldRejectDeleteQuestionByUnauthorizedTest() {
         mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectDeleteQuestionByNotExpertTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectUpdateNonExistentQuestionTest() {
        mockMvc.perform(
                put(QUESTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question was not found.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectDeleteNonExistentQuestionTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Question was not found.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldAddOptionToGivenQuestionByExpertTest() {
        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + "/options").contentType(APPLICATION_JSON).content(validOptionDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.title").value(optionTitle)));
    }

    @Ignore
    @Test
    @WithMockUser(roles = {EXPERT, USER})
    @SneakyThrows
    public void shouldGetOptionOfGivenQuestionTest() {
        mockMvc.perform(
                get(QUESTIONS_URL + question.getId() + "/options/" + option.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Ignore
    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldModifyOptionToGivenQuestionByExpertTest() {
        String modifiedOptionDataJson = "{\"title\":\"Modified option\"}";

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + "/options/" + option.getId()).contentType(APPLICATION_JSON).content(modifiedOptionDataJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value("Modified option")));
    }

    @Ignore
    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRemoveOptionFromGivenQuestionByExpertTest() {
        QuestionOption questionOption = new QuestionOption();
        questionOption.setOption(option);
        questionOption.setQuestion(question);
        questionOption = questionOptionService.create(questionOption);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + "/options/" + questionOption.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveNonExistentOptionFromGivenQuestionByExpertTest() {
        question.addOption(option);
        question = questionService.update(question);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + "/options/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveOptionFromNonExistentQuestionByExpertTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID + "/options/" + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }
}