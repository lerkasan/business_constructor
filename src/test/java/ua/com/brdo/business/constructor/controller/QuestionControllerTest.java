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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.brdo.business.constructor.model.InputType;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;
import ua.com.brdo.business.constructor.service.ProcedureService;
import ua.com.brdo.business.constructor.service.ProcedureTypeService;
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
    private ProcedureTypeService procedureTypeService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private PermitTypeService permitTypeService;

    @Autowired
    private PermitService permitService;

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
    private static final String NEXT_QUESTION_DIR = "/next-question";
    private static final String validOptionDataJson = "{\"title\":\"My option\"}";
    private static final int NON_EXISTENT_ID = 100000;
    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String questionText = "Who are you?";
    private static final String updatedText = "What is your name?";
    private static final String optionTitle = "My option";

    private Question question;
    private Question nextQuestion;

    private Option option;
    private Option nextOption;

    private Question generateValidQuestionWithOptions() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        Question questionWithOptions = new Question();
        questionWithOptions.setText(questionText);
        questionWithOptions.setInputType(InputType.MULTI_CHOICE);
        questionWithOptions.setOptions(options);
        return questionWithOptions;
    }

    private Question generateValidQuestion() {
        Question validQuestion = new Question();
        validQuestion.setText(questionText);
        validQuestion.setInputType(InputType.MULTI_CHOICE);
        return validQuestion;
    }

    private Question generateValidQuestion(String text) {
        Question validQuestion = new Question();
        validQuestion.setText(text);
        validQuestion.setInputType(InputType.MULTI_CHOICE);
        return validQuestion;
    }

    private Map<String, String> generateInvalidQuestionWithWrongInputType() {
        Map<String, String> questionData = new HashMap<>();
        questionData.put("text", questionText);
        questionData.put("inputType", "MULT");
        return questionData;
    }

    private Map<String, String> generateInvalidQuestionWithWrongInputField() {
        Map<String, String> questionData = new HashMap<>();
        questionData.put("text", questionText);
        questionData.put("input", "SINGLE_CHOICE");
        return questionData;
    }

    private Option generateOptionWithNextQuestion(Question nextQuestion) {
        Option option = new Option(optionTitle);
        option.setQuestion(question);
        option.setNextQuestion(nextQuestion);
        return option;
    }

    private Option generateOptionWithProcedure() {
        ProcedureType procedureType = new ProcedureType();
        procedureType.setName("procType");
        procedureType = procedureTypeService.create(procedureType);

        PermitType permitType = new PermitType();
        permitType.setName("test");
        permitType = permitTypeService.create(permitType);

        Permit permit = new Permit();
        permit.setName("should delete");
        permit.setLegalDocumentId(1L);
        permit.setFormId(1L);
        permit.setNumber(" ");
        permit.setTerm(" ");
        permit.setPropose(" ");
        permit.setStatus((byte) 1);
        permit = permitService.create(permit, permitType);

        Procedure procedure = new Procedure();
        procedure.setName("1");
        procedure.setReason("1");
        procedure.setResult("1");
        procedure.setCost("1");
        procedure.setTerm("1");
        procedure.setMethod("1");
        procedure.setDecision("1");
        procedure.setDeny("1");
        procedure.setAbuse("1");
        procedure.setProcedureType(procedureType);
        procedure.setPermit(permit);
        procedure = procedureService.create(procedure);

        Option option = new Option(optionTitle);
        option.setQuestion(question);
        option.setProcedure(procedure);
        return option;
    }

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        question = new Question();
        question.setText(questionText);
        question.setInputType(InputType.SINGLE_CHOICE);
        question = questionService.create(question);

        nextQuestion = new Question();
        nextQuestion.setText(questionText);
        nextQuestion.setInputType(InputType.SINGLE_CHOICE);
        nextQuestion = questionService.create(nextQuestion);

        option = new Option(optionTitle);
        option.setQuestion(question);
        option = optionService.create(option);

        nextOption = new Option(optionTitle);
        nextOption.setQuestion(nextQuestion);
        nextOption = optionService.create(nextOption);
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    public void shouldShowQuestion() throws Exception {
        mockMvc.perform(get(QUESTIONS_URL + question.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value(questionText)));
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    public void shouldRejectShowNonExistentQuestion() throws Exception {
        mockMvc.perform(get(QUESTIONS_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {USER})
    public void shouldShowQuestionListToUser() throws Exception {
        mockMvc.perform(get(QUESTIONS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }


    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldShowQuestionListToExpert() throws Exception {
        mockMvc.perform(get(QUESTIONS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldCreateQuestionByExpertTest() throws Exception {
        Question validQuestion = generateValidQuestion();
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.inputType").value(MULTI_CHOICE)));
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectQuestionCreationByUnauthorizedTest() throws Exception {
        Question validQuestion = generateValidQuestion();
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectQuestionCreationNotByExpertTest() throws Exception {
        Question validQuestion = generateValidQuestion();
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldCreateQuestionWithMultiChoiceTest() throws Exception {
        Question validQuestionWithInputType = generateValidQuestion();
        String validQuestionWithInputTypeJson = jsonMapper.writeValueAsString(validQuestionWithInputType);

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(validQuestionWithInputTypeJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.inputType").value(MULTI_CHOICE)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateQuestionWithWrongChoiceTest() throws Exception {
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
    public void shouldCreateQuestionWithOptionsArrayTest() throws Exception {
        Question questionWithOptions = generateValidQuestionWithOptions();
        String questionDataJson = jsonMapper.writeValueAsString(questionWithOptions);

        mockMvc.perform(
                 post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(questionDataJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.text").value(questionText)))
                .andExpect((jsonPath("$.inputType").value(MULTI_CHOICE)))
                .andExpect(jsonPath("$.options").isArray())
                .andExpect(jsonPath("$.options").isNotEmpty());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldUpdateQuestionByExpertTest() throws Exception {
        Question validQuestion = generateValidQuestion(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.text").value(updatedText)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldDeleteQuestionByExpertTest() throws Exception {
        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectUpdateQuestionByUnauthorizedTest() throws Exception {
        Question validQuestion = generateValidQuestion(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectUpdateQuestionByNotExpertTest() throws Exception {
        Question validQuestion = generateValidQuestion(updatedText);
        String modifiedQuestion = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId()).contentType(APPLICATION_JSON).content(modifiedQuestion))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectDeleteQuestionByUnauthorizedTest() throws Exception {
         mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectDeleteQuestionByNotExpertTest() throws Exception {
        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectUpdateNonExistentQuestionTest() throws Exception {
        Question validQuestion = generateValidQuestion(questionText);
        String validQuestionJson = jsonMapper.writeValueAsString(validQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectDeleteNonExistentQuestionTest() throws Exception {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectShowOptionsForNonExistentQuestionTest() throws Exception {
        mockMvc.perform(
                get(QUESTIONS_URL + NON_EXISTENT_ID + OPTIONS_DIR ))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(QUESTION_NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldAddOptionToGivenQuestionByExpertTest() throws Exception {
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
    public void shouldGetOptionOfGivenQuestionTest() throws Exception {
        mockMvc.perform(
                get(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldModifyOptionToGivenQuestionByExpertTest() throws Exception {
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
    public void shouldRemoveOptionFromGivenQuestionByExpertTest() throws Exception {
        option = optionService.create(option);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectRemoveNonExistentOptionFromGivenQuestionByExpertTest() throws Exception {
        question.addOption(option);
        question = questionService.update(question);

        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + OPTIONS_DIR + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectRemoveOptionFromNonExistentQuestionByExpertTest() throws Exception {
        mockMvc.perform(
                delete(QUESTIONS_URL + NON_EXISTENT_ID + OPTIONS_DIR + NON_EXISTENT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldReturnMalformedJsonErrorTest() throws Exception {
        String malformedJson = "{\"text\": Who are you?}";

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(MALFORMED_JSON)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateQuestionWithoutTextTest() throws Exception {
        String malformedJson = "{\"inputType\": \"SINGLE_CHOICE\"}";

        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Text field of question is required.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateQuestionWithOptionWithoutTitleTest() throws Exception {
        String malformedJson = "{\"text\": \"Who are you?\", \"inputType\": \"SINGLE_CHOICE\", \"options\": [{ }] }";
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Title field of option is required.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateQuestionWithoutInputTypeTest() throws Exception {
        Map<String,String> malformedData = generateInvalidQuestionWithWrongInputField();
        String malformedJson = jsonMapper.writeValueAsString(malformedData);
        mockMvc.perform(
                post(QUESTIONS_URL).contentType(APPLICATION_JSON).content(malformedJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Input type is required.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldReturnMalformedURLErrorTest() throws Exception {
        mockMvc.perform(
                get(MALFORMED_URL_PARAM))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(MALFORMED_URL)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldCreateNextQuestionInOptionTest() throws Exception {
        Map<String, String> nextQuestionData = new HashMap<>();
        nextQuestionData.put("id", nextQuestion.getId().toString());
        String nextQuestionDataJson = jsonMapper.writeValueAsString(nextQuestionData);

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId() + NEXT_QUESTION_DIR)
                .contentType(APPLICATION_JSON).content(nextQuestionDataJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldGetNextQuestionInOptionTest() throws Exception {
        Map<String, String> nextQuestionData = new HashMap<>();
        nextQuestionData.put("id", nextQuestion.getId().toString());
        String nextQuestionDataJson = jsonMapper.writeValueAsString(nextQuestionData);

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId() + NEXT_QUESTION_DIR)
                        .contentType(APPLICATION_JSON).content(nextQuestionDataJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));

        mockMvc.perform(
                get(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateNextQuestionLinkingToCurrentQuestionTest() throws Exception {
        Map<String, String> nextQuestionData = new HashMap<>();
        nextQuestionData.put("id", question.getId().toString());
        String nextQuestionDataJson = jsonMapper.writeValueAsString(nextQuestionData);

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId() + NEXT_QUESTION_DIR)
                .contentType(APPLICATION_JSON).content(nextQuestionDataJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Question can't be linked to itself."));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldUpdateNextQuestionInOptionTest() throws Exception {
        Map<String, String> nextQuestionData = new HashMap<>();
        nextQuestionData.put("id", nextQuestion.getId().toString());
        String nextQuestionDataJson = jsonMapper.writeValueAsString(nextQuestionData);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId() + NEXT_QUESTION_DIR)
                .contentType(APPLICATION_JSON).content(nextQuestionDataJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldDeleteNextQuestionInOptionTest() throws Exception {
        mockMvc.perform(
                delete(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId() + NEXT_QUESTION_DIR))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldAddNextQuestionInEntireOptionTest() throws Exception {
        Option optionWithNextQuestion = generateOptionWithNextQuestion(nextQuestion);
        String optionWithNextQuestionJson = jsonMapper.writeValueAsString(optionWithNextQuestion);

        System.out.println("!!!!!!!! "+optionWithNextQuestionJson); // TODO: Remove

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR)
                .contentType(APPLICATION_JSON).content(optionWithNextQuestionJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldAddProcedureInEntireOptionTest() throws Exception {
        Option optionWithProcedure = generateOptionWithProcedure();
        String optionWithProcedureJson = jsonMapper.writeValueAsString(optionWithProcedure);

        System.out.println("!!!!!!!! "+optionWithProcedureJson); // TODO: Remove

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR)
                        .contentType(APPLICATION_JSON).content(optionWithProcedureJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.procedure.id").value(optionWithProcedure.getProcedure().getId())));
    }

    @Test(expected = IllegalArgumentException.class)
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateNextQuestionLinkingToCurrentQuestionInEntireOptionTest() throws Exception {
        Option optionWithNextQuestionSelfLink = generateOptionWithNextQuestion(question);
        String optionWithNextQuestionSelfLinkJson = jsonMapper.writeValueAsString(optionWithNextQuestionSelfLink);

        mockMvc.perform(
                post(QUESTIONS_URL + question.getId() + OPTIONS_DIR)
                .contentType(APPLICATION_JSON).content(optionWithNextQuestionSelfLinkJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Question can't be linked to itself."));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldUpdateNextQuestionInEntireOptionTest() throws Exception {
        Option optionWithNextQuestion = generateOptionWithNextQuestion(nextQuestion);
        String optionWithNextQuestionJson = jsonMapper.writeValueAsString(optionWithNextQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId())
                .contentType(APPLICATION_JSON).content(optionWithNextQuestionJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.nextQuestion.id").value(nextQuestion.getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldUpdateProcedureInEntireOptionTest() throws Exception {
        Option optionWithProcedure = generateOptionWithProcedure();
        String optionWithProcedureJson = jsonMapper.writeValueAsString(optionWithProcedure);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId())
                        .contentType(APPLICATION_JSON).content(optionWithProcedureJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.procedure.id").value(optionWithProcedure.getProcedure().getId())));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldDeleteNextQuestionInEntireOptionTest() throws Exception {
        Option optionWithoutNextQuestion = new Option(optionTitle);
        String optionWithoutNextQuestionJson = jsonMapper.writeValueAsString(optionWithoutNextQuestion);

        mockMvc.perform(
                put(QUESTIONS_URL + question.getId() + OPTIONS_DIR + option.getId())
                .contentType(APPLICATION_JSON).content(optionWithoutNextQuestionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.nextQuestion").doesNotExist()));
    }
}