package ua.com.brdo.business.constructor.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.service.LegalDocumentService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LegalDocumentControllerTest {
    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";
    private static final String USER = "USER";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private LegalDocumentService legalDocumentService;

    private MockMvc mvc;

    private LegalDocument returnLegalDocument(){
        LegalDocument legalDocument = new LegalDocument();
        legalDocument.setAutoBrdo((byte)1);
        legalDocument.setAutoLiga((byte)1);
        legalDocument.setDateAdd(1);
        legalDocument.setDatePub(1);
        legalDocument.setIdLiga("1");
        legalDocument.setIdRada("1");
        legalDocument.setIdState(1);
        legalDocument.setManualSector("1");
        legalDocument.setNumberMj("1");
        legalDocument.setNumberPub("1");
        legalDocument.setNumberRada("1");
        legalDocument.setRegulation(1);
        legalDocument.setTechRegulation(1);
        legalDocument.setTitle("1");
        legalDocument.setInBrdo((byte)1);
        legalDocument.setInLiga((byte)1);
        legalDocument.setInRada((byte)1);
        return legalDocument;
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }
    @Test
    @Transactional
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldCreateLegalDocumentTest() throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(returnLegalDocument());

        mvc.perform(post("/api/laws").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    public void shouldReturnAllListLegalDocumentTest() throws Exception {
        mvc.perform(get("/api/laws")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    public void shouldGetLegalDocumentTest() throws Exception {
        LegalDocument legalDocument = returnLegalDocument();
        legalDocumentService.create(legalDocument);

        mvc.perform(get("/api/laws/"+legalDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value(legalDocument.getTitle())));


    }
    @Test
    @Transactional
    @WithMockUser(roles={EXPERT,ADMIN})
    public void shouldUpdateLegalDocumentTest() throws Exception {
        LegalDocument legalDocument = returnLegalDocument();
        legalDocumentService.create(legalDocument);
        LegalDocument updatedLegalDocument=returnLegalDocument();
        updatedLegalDocument.setTitle("2");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(updatedLegalDocument);
        System.out.print(legalDocument + "\n" + updatedLegalDocument);
        mvc.perform(put("/api/laws/"+legalDocument.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.title").value(legalDocument.getTitle())));
    }

}
