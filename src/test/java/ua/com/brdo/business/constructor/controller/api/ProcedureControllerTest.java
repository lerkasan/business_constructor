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
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;
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
public class ProcedureControllerTest {
    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";

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

    private Procedure returnProcedure(){
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
        return procedure;
    }

    @Test
    @Transactional
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldCreateProcedureTest() throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Procedure procedure = returnProcedure();
        String json = ow.writeValueAsString(procedure);

        mvc.perform(post("/api/procedures/").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldReturnAllListProcedureTest() throws Exception {
        mvc.perform(get("/api/procedures"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldGetProcedureTest() throws Exception {
        Procedure procedure = returnProcedure();
        procedureService.create(procedure);

        mvc.perform(get("/api/procedures/"+procedure.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedure.getName())));
    }

    @Test
    @Transactional
    @WithMockUser(roles={EXPERT,ADMIN})
    public void shouldUpdateProcedureTest() throws Exception {
        Procedure procedure = returnProcedure();
        procedure = procedureService.create(procedure);
        procedure.setName("2");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(procedure);
        System.out.print(procedure + "\n" + procedure);
        mvc.perform(put("/api/procedures/"+procedure.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedure.getName())));
    }
}
