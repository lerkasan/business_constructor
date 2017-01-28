package ua.com.brdo.business.constructor.controller.api;

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

import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;
import ua.com.brdo.business.constructor.service.ProcedureDocumentService;
import ua.com.brdo.business.constructor.service.ProcedureService;
import ua.com.brdo.business.constructor.service.ProcedureTypeService;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ProcedureDocumentControllerTest {

    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";

    @Autowired
    private ProcedureDocumentService procedureDocumentService;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProcedureTypeService procedureTypeService;

    @Autowired
    private PermitService permitService;

    @Autowired
    private PermitTypeService permitTypeService;

    @Autowired
    private MockMvc mvc;

    private ProcedureDocument returnProcedureDocument(){
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
        procedure.setToolId(1L);
        procedure.setProcedureType(procedureType);
        procedure.setPermit(permit);
        procedure = procedureService.create(procedure);

        ProcedureDocument procedureDocument = new ProcedureDocument();
        procedureDocument.setName("1");

        procedureDocument.setProcedure(procedure);
        return procedureDocument;
    }

    @Test
    @Transactional
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldCreateProcedureDocumentTest() throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ProcedureDocument procedureDocument = returnProcedureDocument();
        String json = ow.writeValueAsString(procedureDocument);

        mvc.perform(post("/api/documents/").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldReturnAllListProcedureDocumentTest() throws Exception {
        mvc.perform(get("/api/documents"))
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
        procedureDocument.setName("2");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(procedureDocument);
        System.out.print(procedureDocument + "\n" + procedureDocument);
        mvc.perform(put("/api/documents/"+procedureDocument.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedureDocument.getName())));
    }
}
