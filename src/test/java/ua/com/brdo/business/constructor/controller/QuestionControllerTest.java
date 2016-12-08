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
    ObjectMapper jsonMapper;

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
    private static final String validOptionDataJson = "{\"title\":\"My option\"}";
    private static final int NON_EXISTENT_ID = 10000;
    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String questionText = "Who are you?";
    private static final String updatedText = "What is your name?";
    private static final String optionTitle = "My option";

    private Question question;
    private Option option;

    private Question generateValidQuestionWithOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        Question questionWithOptions = new Question();
        questionWithOptions.setText(questionText);
        questionWithOptions.setInputType(MULTI_CHOICE);
        questionWithOptions.setOptions(options);
        return questionWithOptions;
    }

    private Question generateValidQuestionWithInputType() {
        Question validQuestion = new Question();
        validQuestion.setText(questionText);
        validQuestion.setInputType(MULTI_CHOICE);
        return validQuestion;
    }

    private Question generateValidQuestionWithTextOnly(String text) {
        Question validQuestion = new Question();
        validQuestion.setText(text);
        return validQuestion;
    }

    private Map<String, String> generateInvalidQuestionWithWrongInputType() {
        Map<String, String> questionData = new HashMap<>();
        questionData.put("text", questionText);
        questionData.put("input_type", "MULT");
        return questionData;
    }

    @Before
    @SneakyThrows
    public void setUp() {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();

        question = new Question();
        question.setText(questionText);
        question = questionService.create(question);

        option = new Option(optionTitle);
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
        Question validQuestion = generateValidQuestionWithTextOnly(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
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
        Question validQuestion = generateValidQuestionWithTextOnly(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectQuestionCreationNotByExpertTest() {
        Question validQuestion = generateValidQuestionWithTextOnly(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithDefaultSingleChoiceTest() {
        Question validQuestion = generateValidQuestionWithTextOnly(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
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
        Question validQuestionWithInputType = generateValidQuestionWithInputType();
        String validQuestionWithInputTypeJson = jsonMapper.writeValueAsString(validQuestionWithInputType);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionWithInputTypeJson))
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
        Map<String, String> invalidQuestionData = generateInvalidQuestionWithWrongInputType();
        String invalidQuestionDataJson = jsonMapper.writeValueAsString(invalidQuestionData);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(invalidQuestionDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Received malformed JSON.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldCreateQuestionWithOptionsArrayTest() {
        Question questionWithOptions = generateValidQuestionWithOptions();
        String questionDataJson = jsonMapper.writeValueAsString(questionWithOptions);

        mockMvc.perform(
                 post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(questionDataJson))
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
        Question validQuestion = generateValidQuestionWithTextOnly(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value(updatedText)));
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
        Question validQuestion = generateValidQuestionWithTextOnly(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    @SneakyThrows
    public void shouldRejectUpdateQuestionByNotExpertTest() {
        Question validQuestion = generateValidQuestionWithTextOnly(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
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
        Question validQuestion = generateValidQuestionWithTextOnly(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionJson))
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
        String malformedJson = "{\"text\": Who are you?}";

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(MALFORMED_JSON)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    @SneakyThrows
    public void shouldRejectCreateQuestionWithoutTextTest() {
        String malformedJson = "{\"te\": \"Who are you?\"}";

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Text field in question is required.")));
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