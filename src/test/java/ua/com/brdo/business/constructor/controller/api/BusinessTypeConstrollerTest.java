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
import ua.com.brdo.business.constructor.service.BusinessTypeService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class BusinessTypeConstrollerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    ObjectMapper jsonMapper;

    private MockMvc mockMvc;
    private TestContextManager testContextManager;

    private BusinessType businessType;

    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";
    private static final String BUSINESS_TYPE_URL = "/api/business-types/";
    private static final String title = "Business type title";
    private static final String codeKved = "12.34";
    private static final String dummyTitle = "Dummy title";
    private static final String dummyCodeKved = "11.11";
    private static final String updatedTitle = "New business type title";
    private static final String updatedCodeKved = "98.76";
    private static final int NON_EXISTENT_ID = 100000;
    private static final String NOT_FOUND = "Specified business type was not found.";

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        businessType = new BusinessType();
        businessType.setTitle(dummyTitle);
        businessType.setCodeKved(dummyCodeKved);
        businessTypeService.create(businessType);
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    public void shouldShowBusinessType() throws Exception {
        mockMvc.perform(get(BUSINESS_TYPE_URL + businessType.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value(businessType.getTitle())));
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    public void shouldRejectShowNonExistentBusinessType() throws Exception {
        mockMvc.perform(get(BUSINESS_TYPE_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {USER, EXPERT})
    public void shouldShowBusinessTypeList() throws Exception {
        mockMvc.perform(get(BUSINESS_TYPE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$").isArray()));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldCreateBusinessTypeByExpertTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType();
        String validBusinessTypeJson = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(validBusinessTypeJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(header().string("Location", CoreMatchers.notNullValue()))
                .andExpect((jsonPath("$.title").value(title)))
                .andExpect((jsonPath("$.codeKved").value(codeKved)));
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectBusinessTypeCreationByUnauthorizedTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType();
        String validBusinessTypeJson = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(validBusinessTypeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectBusinessTypeCreationNotByExpertTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType();
        String validBusinessTypeJson = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(validBusinessTypeJson))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldUpdateBusinessTypeByExpertTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType(updatedTitle, updatedCodeKved);
        String modifiedBusinessType = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                put(BUSINESS_TYPE_URL + businessType.getId()).contentType(APPLICATION_JSON).content(modifiedBusinessType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value(updatedTitle)))
                .andExpect((jsonPath("$.codeKved").value(updatedCodeKved)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldDeleteBusinessTypeByExpertTest() throws Exception {
        mockMvc.perform(
                delete(BUSINESS_TYPE_URL + businessType.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectUpdateBusinessTypeByAnonymousTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType(updatedTitle, updatedCodeKved);
        String modifiedBusinessType = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                put(BUSINESS_TYPE_URL + businessType.getId()).contentType(APPLICATION_JSON).content(modifiedBusinessType))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void shouldRejectDeleteBusinessTypeByAnonymousTest() throws Exception {
        mockMvc.perform(
                delete(BUSINESS_TYPE_URL + businessType.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectUpdateBusinessTypeByUnauthorizedTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType(updatedTitle, updatedCodeKved);
        String modifiedBusinessType = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                put(BUSINESS_TYPE_URL + businessType.getId()).contentType(APPLICATION_JSON).content(modifiedBusinessType))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {USER, ADMIN})
    public void shouldRejectDeleteBusinessTypeByUnauthorizedTest() throws Exception {
        mockMvc.perform(
                delete(BUSINESS_TYPE_URL + businessType.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectUpdateNonExistentBusinessTypeTest() throws Exception {
        BusinessType validBusinessType = generateBusinessType();
        String validBusinessTypeJson = jsonMapper.writeValueAsString(validBusinessType);

        mockMvc.perform(
                put(BUSINESS_TYPE_URL + NON_EXISTENT_ID).contentType(APPLICATION_JSON).content(validBusinessTypeJson))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectDeleteNonExistentBusinessTypeTest() throws Exception {
        mockMvc.perform(
                delete(BUSINESS_TYPE_URL + NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value(NOT_FOUND)));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateBusinessTypeWithoutTitleTest() throws Exception {
        BusinessType invalidBusinessType = generateInvalidBusinessTypeWithoutTitle();
        String invalidBusinessTypeJson = jsonMapper.writeValueAsString(invalidBusinessType);
        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(invalidBusinessTypeJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Title field is required.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateBusinessTypeWithoutCodeKvedTest() throws Exception {
        BusinessType invalidBusinessType = generateInvalidBusinessTypeWithoutCodeKved();
        String invalidBusinessTypeJson = jsonMapper.writeValueAsString(invalidBusinessType);
        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(invalidBusinessTypeJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("KVED code field is required.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateBusinessTypeWithDuplicateTitleTest() throws Exception {
        BusinessType invalidBusinessType = generateBusinessType(dummyTitle, codeKved);
        String invalidBusinessTypeJson = jsonMapper.writeValueAsString(invalidBusinessType);
        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(invalidBusinessTypeJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Business type with specified title already exists in database. Title should be unique.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateBusinessTypeWithDuplicateCodeKvedTest() throws Exception {
        BusinessType invalidBusinessType = generateBusinessType(title, dummyCodeKved);
        String invalidBusinessTypeJson = jsonMapper.writeValueAsString(invalidBusinessType);
        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(invalidBusinessTypeJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Business type with specified KVED code already exists in database. KVED code should be unique.")));
    }

    @Test
    @WithMockUser(roles = {EXPERT})
    public void shouldRejectCreateBusinessTypeWithInvalidCodeKvedTest() throws Exception {
        BusinessType invalidBusinessType = generateBusinessType(title, "123.45");
        String invalidBusinessTypeJson = jsonMapper.writeValueAsString(invalidBusinessType);
        mockMvc.perform(
                post(BUSINESS_TYPE_URL).contentType(APPLICATION_JSON).content(invalidBusinessTypeJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.message").value("Format of KVED code must be a pair of one-digit or two-digit numbers separated by dot. Example: 6.21 or 06.21 or 62.2 or 62.21")));
    }

    private BusinessType generateBusinessType() {
        BusinessType validBusinessType = new BusinessType();
        validBusinessType.setTitle(title);
        validBusinessType.setCodeKved(codeKved);
        return validBusinessType;
    }

    private BusinessType generateBusinessType(String title, String codeKved) {
        BusinessType validBusinessType = new BusinessType();
        validBusinessType.setTitle(title);
        validBusinessType.setCodeKved(codeKved);
        return validBusinessType;
    }

    private BusinessType generateInvalidBusinessTypeWithoutTitle() {
        BusinessType invalidBusinessType = new BusinessType();
        invalidBusinessType.setCodeKved(codeKved);
        return invalidBusinessType;
    }

    private BusinessType generateInvalidBusinessTypeWithoutCodeKved() {
        BusinessType invalidBusinessType = new BusinessType();
        invalidBusinessType.setTitle(title);
        return invalidBusinessType;
    }
}
