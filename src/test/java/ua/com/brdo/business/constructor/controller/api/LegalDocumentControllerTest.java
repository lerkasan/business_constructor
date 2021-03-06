package ua.com.brdo.business.constructor.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.service.LegalDocumentService;
import ua.com.brdo.business.constructor.service.NotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LegalDocumentControllerTest {
    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";
    private static final String LAWS_URL = "/api/laws/";
    private static final long UNEXIST_ID = 404;

    @Autowired
    private LegalDocumentService legalDocumentService;

    @Autowired
    private MockMvc mvc;

    private LegalDocument generateLegalDocument() {
        LegalDocument legalDocument = new LegalDocument();
        legalDocument.setAutoBrdo((byte) 9);
        legalDocument.setAutoLiga((byte) 9);
        legalDocument.setDateAdd(9);
        legalDocument.setDatePub(9);
        legalDocument.setIdLiga("9");
        legalDocument.setIdRada("idRada9");
        legalDocument.setIdState(0);
        legalDocument.setManualSector("9");
        legalDocument.setNumberMj("9");
        legalDocument.setNumberPub("9");
        legalDocument.setNumberRada("9");
        legalDocument.setRegulation(9);
        legalDocument.setTechRegulation(9);
        legalDocument.setTitle("9");
        legalDocument.setInBrdo((byte) 9);
        legalDocument.setInLiga((byte) 9);
        legalDocument.setInRada((byte) 9);
        return legalDocument;
    }

    private Map<String, String> generateInvalidLegalDocument() {
        Map<String, String> legalDocumentMap = new HashMap<>();
        legalDocumentMap.put("title", "title");
        legalDocumentMap.put("unexisted", "1");
        return legalDocumentMap;
    }

    private String generateJSON(Object legalDocument) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(legalDocument);
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldCreateLegalDocumentTest() throws Throwable {
        LegalDocument legalDocumentToSave = generateLegalDocument();

        mvc.perform(post(LAWS_URL).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(legalDocumentToSave)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(legalDocumentToSave.getTitle()));
    }

    @Test
    @WithMockUser(roles = {USER})
    public void shouldAccessIsDeniedWhenLegalDocumentCreatingByUserTest() throws Throwable {
        mvc.perform(post(LAWS_URL).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(generateLegalDocument())))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldRejectCreateInvalidLegalDocumentTest() throws Throwable {
        mvc.perform(post(LAWS_URL).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(generateInvalidLegalDocument())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldReturnAllLegalDocumentsListTest() throws Exception {
        mvc.perform(get(LAWS_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void shouldReturnLegalDocumentByIdTest() throws Exception {
        LegalDocument legalDocument = generateLegalDocument();
        legalDocumentService.create(legalDocument);

        mvc.perform(get(LAWS_URL + legalDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value(legalDocument.getTitle())));
    }

    @Test
    public void shouldNotFoundLegalDocumentByNotExistingIdTest() throws Exception {
        try {
            legalDocumentService.findById(UNEXIST_ID);
            legalDocumentService.delete(UNEXIST_ID);
        } catch (NotFoundException ignored) {
        }
        mvc.perform(get(LAWS_URL + UNEXIST_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldUpdateLegalDocumentTest() throws Exception {
        LegalDocument legalDocumentToSave = generateLegalDocument();
        legalDocumentService.create(legalDocumentToSave);
        LegalDocument updatedLegalDocument = generateLegalDocument();
        updatedLegalDocument.setTitle("New Title");

        mvc.perform(put(LAWS_URL + legalDocumentToSave.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(updatedLegalDocument)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.id").value(legalDocumentToSave.getId())))
                .andExpect((jsonPath("$.title").value(legalDocumentToSave.getTitle())));
    }

    @Test
    @WithMockUser(roles = {USER})
    public void shouldAccessIsDeniedWhenUpdateLegalDocumentAsUserTest() throws Throwable {
        LegalDocument legalDocumentToSave = generateLegalDocument();
        legalDocumentService.create(legalDocumentToSave);
        LegalDocument updatedLegalDocument = generateLegalDocument();
        updatedLegalDocument.setTitle("2");

        mvc.perform(put(LAWS_URL + legalDocumentToSave.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(updatedLegalDocument)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldNotUpdateByInvalidLegalDocumentTest() throws Throwable {
        LegalDocument legalDocumentToSave = generateLegalDocument();
        legalDocumentService.create(legalDocumentToSave);

        mvc.perform(put(LAWS_URL + legalDocumentToSave.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(generateInvalidLegalDocument())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldNotFoundWhenUpdateNotExistingLegalDocumentIdTest() throws Throwable {
        try {
            legalDocumentService.findById(UNEXIST_ID);
            legalDocumentService.delete(UNEXIST_ID);
        } catch (NotFoundException ignored) {
        }

        mvc.perform(put(LAWS_URL + UNEXIST_ID).contentType(MediaType.APPLICATION_JSON)
                .content(generateJSON(generateLegalDocument())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldDeleteLegalDocumentTest() throws Throwable {
        LegalDocument savedLegalDocument = generateLegalDocument();
        legalDocumentService.create(savedLegalDocument);

        mvc.perform(delete(LAWS_URL + savedLegalDocument.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {USER})
    public void shouldAccessIsDeniedWhenDeleteLegalDocumentAsUserTest() throws Throwable {
        LegalDocument savedLegalDocument = generateLegalDocument();
        legalDocumentService.create(savedLegalDocument);

        mvc.perform(delete(LAWS_URL + savedLegalDocument.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {EXPERT, ADMIN})
    public void shouldNotFoundWhenDeleteNotExistingLegalDocumentIdTest() throws Throwable {
        try {
            legalDocumentService.findById(UNEXIST_ID);
            legalDocumentService.delete(UNEXIST_ID);
        } catch (NotFoundException ignored) {
        }
        mvc.perform(delete(LAWS_URL + UNEXIST_ID))
                .andExpect(status().isNotFound());
    }
}
