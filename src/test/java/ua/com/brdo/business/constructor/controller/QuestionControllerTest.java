package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.valueOf;
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

    private MockMvc mockMvc;
    private TestContextManager testContextManager;

    private static final String SINGLE_CHOICE = "SINGLE_CHOICE";
    private static final String MULTI_CHOICE = "MULTI_CHOICE";
    private static final String QUESTION_NOT_FOUND = "Question was not found.";
    private static final String MALFORMED_JSON = "Received malformed JSON.";
    private static final String MALFORMED_URL = "Received malformed URL.";
    private static final String MALFORMED_URL_PARAM = "/api/questions/234@ds";
    private static final String QUESTIONS_URL = "/api/questions/";
    private static final String OPTIONS_DIR = "/options/";
    private static final String validQuestionDataJson = "{\"text\":\"Who are you?\"}";
    private static final String validQuestionDataWithChoiceJson = "{\"text\":\"Who are you?\", \"input_type\": \"MULTI_CHOICE\"}";
    private static final String invalidQuestionDataJson = "{\"text\":\"Who are you?\", \"input_type\": \"MULT\"}";
    private static final String malformedJson = "{\"text\":\"Who are you?}";
    private static final String validOptionDataJson = "{\"title\":\"My option\"}";
    private static final String updatedTextField = "{\"text\":\"What is your name?\"}";
    private static final int NON_EXISTENT_ID = 10000;
    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String questionText = "Who are you?";
    private static final String optionTitle = "My option";

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
        option.setQuestion(question);
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
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
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
                .andExpect((jsonPath("$.input_type").value(SINGLE_CHOICE)));
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
    public void shouldCreateQuestionWithDefaultSingleChoiceTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.input_type").value(SINGLE_CHOICE)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithMultiChoiceTest() {
        System.out.println(validQuestionDataWithChoiceJson);
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionDataWithChoiceJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.input_type").value(MULTI_CHOICE)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectCreateQuestionWithWrongChoiceTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(invalidQuestionDataJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Received malformed JSON.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithOptionsArrayTest() {
//        ObjectMapper jsonMapper = new ObjectMapper();
//        Map<String, Object> validQuestionData = new HashMap<>();
//        Map<String, String> optionData1 = new HashMap<>();
//        Map<String, String> optionData2 = new HashMap<>();
//        optionData1.put("title", "option1");
//        optionData2.put("title", "option2");
//        Set<Map<String,String>> options = new HashSet<>();
//        options.add(optionData1);
//        options.add(optionData2);
//        validQuestionData.put("text", questionText);
//        validQuestionData.put("input_type", MULTI_CHOICE);
//        validQuestionData.put("options", options);
//        String questionDataJson = jsonMapper.writeValueAsString(validQuestionData);
        String questionDataJson = "{\"options\":[{\"title\":\"option1\"},{\"title\":\"option2\"}],\"input_type\":\"MULTI_CHOICE\",\"text\":\"Who are you?\"}";

        mockMvc.perform(
                 post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(questionDataJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.input_type").value(MULTI_CHOICE)))
                .andExpect(jsonPath("$.options").isArray())
                .andExpect(jsonPath("$.options").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldUpdateQuestionByExpertTest() {
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
        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(updatedTextField))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectUpdateQuestionByNotExpertTest() {
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
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectDeleteNonExistentQuestionTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectShowOptionsForNonExistentQuestionTest() {
        mockMvc.perform(
                get(QUESTIONS_URL + NON_EXISTENT_ID + OPTIONS_DIR ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldAddOptionToGivenQuestionByExpertTest() {
        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR).contentType(APPLICATION_JSON).content(validOptionDataJson))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.title").value(optionTitle)));
    }

    @Test
    @WithMockUser(roles = {EXPERT, USER})
    @SneakyThrows
    public void shouldGetOptionOfGivenQuestionTest() {
        mockMvc.perform(
                get(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldModifyOptionToGivenQuestionByExpertTest() {
        String modifiedOptionDataJson = "{\"title\":\"Modified option\"}";

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()).contentType(APPLICATION_JSON).content(modifiedOptionDataJson))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value("Modified option")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRemoveOptionFromGivenQuestionByExpertTest() {
        option = optionService.create(option);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveNonExistentOptionFromGivenQuestionByExpertTest() {
        question.addOption(option);
        question = questionService.update(question);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + OPTIONS_DIR + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectRemoveOptionFromNonExistentQuestionByExpertTest() {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID + OPTIONS_DIR + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldReturnMalformedJsonErrorTest() {
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(MALFORMED_JSON)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldReturnMalformedURLErrorTest() {
        mockMvc.perform(
                get(MALFORMED_URL_PARAM))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(MALFORMED_URL)));
    }
}