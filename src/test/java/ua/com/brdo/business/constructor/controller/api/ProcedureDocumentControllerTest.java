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
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.service.ProcedureDocumentService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class ProcedureDocumentControllerTest {

    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";
    //private static final String USER = "USER";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProcedureDocumentService procedureDocumentService;

    private MockMvc mvc;

    private ProcedureDocument returnProcedureDocument(){
        ProcedureDocument procedureDocument = new ProcedureDocument();
        procedureDocument.setName("1");
        procedureDocument.setProcedureId((long) 1);
        return procedureDocument;
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }
    @Test
    @Transactional
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldCreateProcedureDocumentTest() throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(returnProcedureDocument());

        mvc.perform(post("/api/documents/").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldReturnAllListProcedureDocumentTest() throws Exception {
        mvc.perform(get("/api/documents"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldGetProcedureTest() throws Exception {
        ProcedureDocument procedureDocument = returnProcedureDocument();
        procedureDocumentService.create(procedureDocument);

        mvc.perform(get("/api/documents/"+procedureDocument.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedureDocument.getName())));


    }
    @Test
    @Transactional
    @WithMockUser(roles={EXPERT,ADMIN})
    public void shouldUpdateProcedureTest() throws Exception {
        ProcedureDocument procedureDocument = returnProcedureDocument();
        procedureDocumentService.create(procedureDocument);
        ProcedureDocument updatedProcedureDocument = returnProcedureDocument();
        updatedProcedureDocument.setName("2");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(updatedProcedureDocument);
        System.out.print(procedureDocument + "\n" + updatedProcedureDocument);
        mvc.perform(put("/api/documents//"+procedureDocument.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedureDocument.getName())));
    }
}
