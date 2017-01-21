package ua.com.brdo.business.constructor.controller.api;

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

import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.service.BusinessTypeService;
import ua.com.brdo.business.constructor.service.QuestionnaireService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
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
public class QuestionnaireControllerTest {

        @Autowired
        private WebApplicationContext wac;

        @Autowired
        private BusinessTypeService businessTypeService;

        @Autowired
        private QuestionnaireService questionnaireService;

        @Autowired
        ObjectMapper jsonMapper;

        private MockMvc mockMvc;
        private TestContextManager testContextManager;

        private Questionnaire questionnaire;
        private BusinessType businessType;


        private static final String EXPERT = "EXPERT";
        private static final String USER = "USER";
        private static final String ADMIN = "ADMIN";
        private static final String QUESTIONNAIRES_URL = "/api/questionnaires/";
        private static final String title = "Questionnaire title";
        private static final String dummyTitle = "Dummy title";
        private static final String updatedTitle = "Questionnaire title";
        private static final int NON_EXISTENT_ID = 100000;
        private static final String QUESTIONNAIRE_NOT_FOUND = "Questionnaire was not found.";

        @Before
        public void setUp() throws Exception {
                this.testContextManager = new TestContextManager(getClass());
                this.testContextManager.prepareTestInstance(this);
                mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                        .apply(springSecurity())
                        .build();

                businessType = new BusinessType();
                businessType.setTitle("Business title");
                businessType.setCodeKved("12.34");
                businessTypeService.create(businessType);

                questionnaire = new Questionnaire();
                questionnaire.setTitle(dummyTitle);
                questionnaire.setBusinessType(businessType);
                questionnaire = questionnaireService.create(questionnaire);
        }

        @Test
        @WithMockUser(roles = {USER, EXPERT})
        public void shouldShowQuestionnaire() throws Exception {
            mockMvc.perform(get(QUESTIONNAIRES_URL + questionnaire.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                    .andExpect((jsonPath("$.title").value(questionnaire.getTitle())));
        }

        @Test
        @WithMockUser(roles = {USER, EXPERT})
        public void shouldRejectShowNonExistentQuestionnaire() throws Exception {
            mockMvc.perform(get(QUESTIONNAIRES_URL + NON_EXISTENT_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                    .andExpect((jsonPath("$.message").value(QUESTIONNAIRE_NOT_FOUND)));
        }

        @Test
        @WithMockUser(roles = {USER, EXPERT})
        public void shouldShowQuestionnaireList() throws Exception {
                mockMvc.perform(get(QUESTIONNAIRES_URL))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$").isArray()));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldCreateQuestionnaireByExpertTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire();
                String validQuestionnaireJson = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(validQuestionnaireJson))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                        .andExpect((jsonPath("$.title").value(title)));
        }

        @Test
        @WithAnonymousUser
        public void shouldRejectQuestionnaireCreationByUnauthorizedTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire();
                String validQuestionnaireJson = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(validQuestionnaireJson))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = {USER, ADMIN})
        public void shouldRejectQuestionnaireCreationNotByExpertTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire();
                String validQuestionnaireJson = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(validQuestionnaireJson))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldUpdateQuestionnaireByExpertTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire(updatedTitle);
                String modifiedQuestionnaire = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        put(QUESTIONNAIRES_URL + questionnaire.getId()).contentType(APPLICATION_JSON).content(modifiedQuestionnaire))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.title").value(updatedTitle)));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldDeleteQuestionnaireByExpertTest() throws Exception {
                mockMvc.perform(
                        delete(QUESTIONNAIRES_URL + questionnaire.getId()))
                        .andExpect(status().isNoContent());
        }

        @Test
        @WithAnonymousUser
        public void shouldRejectUpdateQuestionnaireByAnonymousTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire(updatedTitle);
                String modifiedQuestionnaire = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        put(QUESTIONNAIRES_URL + questionnaire.getId()).contentType(APPLICATION_JSON).content(modifiedQuestionnaire))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithAnonymousUser
        public void shouldRejectDeleteQuestionnaireByAnonymousTest() throws Exception {
                mockMvc.perform(
                        delete(QUESTIONNAIRES_URL + questionnaire.getId()))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = {USER, ADMIN})
        public void shouldRejectUpdateQuestionnaireByUnauthorizedTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire(updatedTitle);
                String modifiedQuestionnaire = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        put(QUESTIONNAIRES_URL + questionnaire.getId()).contentType(APPLICATION_JSON).content(modifiedQuestionnaire))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = {USER, ADMIN})
        public void shouldRejectDeleteQuestionnaireByUnauthorizedTest() throws Exception {
                mockMvc.perform(
                        delete(QUESTIONNAIRES_URL + questionnaire.getId()))
                        .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldRejectUpdateNonExistentQuestionnaireTest() throws Exception {
                Questionnaire validQuestionnaire = generateQuestionnaire();
                String validQuestionnaireJson = jsonMapper.writeValueAsString(validQuestionnaire);

                mockMvc.perform(
                        put(QUESTIONNAIRES_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validQuestionnaireJson))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.message").value(QUESTIONNAIRE_NOT_FOUND)));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldRejectDeleteNonExistentQuestionnaireTest() throws Exception {
                mockMvc.perform(
                        delete(QUESTIONNAIRES_URL + NON_EXISTENT_ID))
                        .andExpect(status().isNotFound())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.message").value(QUESTIONNAIRE_NOT_FOUND)));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldRejectCreateQuestionnaireWithoutTitleTest() throws Exception {
                Questionnaire invalidQuestionnaire = generateInvalidQuestionnaireWithoutTitle();
                String invalidQuestionnaireJson = jsonMapper.writeValueAsString(invalidQuestionnaire);
                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(invalidQuestionnaireJson))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.message").value("Title field of questionnaire is required.")));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldRejectCreateQuestionnaireWithoutBusinessTypeTest() throws Exception {
                Questionnaire invalidQuestionnaire = generateInvalidQuestionnaireWithoutBusinessType();
                String invalidQuestionnaireJson = jsonMapper.writeValueAsString(invalidQuestionnaire);
                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(invalidQuestionnaireJson))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.message").value("Business type field is required.")));
        }

        @Test
        @WithMockUser(roles = {EXPERT})
        public void shouldRejectCreateQuestionnaireWithDuplicateTitleTest() throws Exception {
                Questionnaire invalidQuestionnaire = generateQuestionnaire(dummyTitle);
                String invalidQuestionnaireJson = jsonMapper.writeValueAsString(invalidQuestionnaire);
                mockMvc.perform(
                        post(QUESTIONNAIRES_URL).contentType(APPLICATION_JSON).content(invalidQuestionnaireJson))
                        .andExpect(status().isUnprocessableEntity())
                        .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                        .andExpect((jsonPath("$.message").value("Questionnaire with specified title already exists. Title should be unique.")));
        }

        private Questionnaire generateQuestionnaire() {
                Questionnaire validQuestionnaire = new Questionnaire();
                validQuestionnaire.setTitle(title);
                validQuestionnaire.setBusinessType(businessType);
                return validQuestionnaire;
        }

        private Questionnaire generateQuestionnaire(String title) {
                Questionnaire validQuestionnaire = new Questionnaire();
                validQuestionnaire.setTitle(title);
                validQuestionnaire.setBusinessType(businessType);
                return validQuestionnaire;
        }

        private Questionnaire generateInvalidQuestionnaireWithoutTitle() {
                Questionnaire invalidQuestionnaire = new Questionnaire();
                invalidQuestionnaire.setBusinessType(businessType);
                return invalidQuestionnaire;
        }

        private Questionnaire generateInvalidQuestionnaireWithoutBusinessType() {
                Questionnaire invalidQuestionnaire = new Questionnaire();
                invalidQuestionnaire.setTitle(title);
                return invalidQuestionnaire;
        }
}
