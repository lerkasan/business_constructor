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
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.service.ProcedureService;


import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class ProcedureControllerTest {
    private static final String ADMIN = "ADMIN";
    private static final String EXPERT = "EXPERT";
    //private static final String USER = "USER";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ProcedureService procedureService;

    private MockMvc mvc;

    private Procedure returnProcedure(){
        Procedure procedure = new Procedure();
        procedure.setName("1");
        procedure.setReason("1");
        procedure.setResult("1");
        procedure.setPermitId((long) 1);
        procedure.setIdType((long) 1);
        procedure.setCost("1");
        procedure.setTerm("1");
        procedure.setMethod("1");
        procedure.setDecision("1");
        procedure.setDeny("1");
        procedure.setAbuse("1");
        return procedure;
    }

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }
    @Test
    @Transactional
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldCreateProcedureTest() throws Throwable {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(returnProcedure());

        mvc.perform(post("/api/procedures/").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
    @Test
    @WithMockUser(roles = {EXPERT,ADMIN})
    public void shouldReturnAllListProcedureTest() throws Exception {
        mvc.perform(get("/api/procedures"))
                .andDo(print())
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
        procedureService.create(procedure);
        Procedure updatedProcedure = returnProcedure();
        updatedProcedure.setName("2");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(updatedProcedure);
        System.out.print(procedure + "\n" + updatedProcedure);
        mvc.perform(put("/api/procedures//"+procedure.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect((jsonPath("$.name").value(procedure.getName())));
    }
}
