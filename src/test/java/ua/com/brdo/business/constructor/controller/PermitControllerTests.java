package ua.com.brdo.business.constructor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.After;
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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PermitControllerTests {

    private ObjectMapper jsonMapper = new ObjectMapper();

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private PermitTypeService permitTypeService;
    @Autowired
    private PermitService permitService;

    private MockMvc mvc;

    @Before
    public void setup() throws Throwable {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
                .apply(springSecurity()).build();
    }

    @Test
    public void shouldGetPermitTest() throws Throwable {
        mvc.perform(get("/api/permits/2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldGetPermitTypeTest() throws Throwable {
        mvc.perform(get("/api/permitstype/1").with(user("admin").roles("admin")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @WithMockUser
    @Test
    public void shouldAddNewPermitTest() throws Throwable {
        mvc.perform(post("/api/permits/permittype/1").contentType(MediaType.APPLICATION_JSON)
                .content(permitRequestData("create")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        clearPermit();
    }

    @WithMockUser
    @Test
    public void shouldAddNewPermitTypeTest() throws Throwable {
        Map<String, String> permitTypeData = new HashMap<>();
        permitTypeData.put("name", "permitType test");
        String validPermitType = jsonMapper.writeValueAsString(permitTypeData);
        mvc.perform(post("/api/permitstype").contentType(MediaType.APPLICATION_JSON).content(validPermitType))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        clearpermitType();
    }

    @WithMockUser
    @Test
    public void shouldUpdatePermitTypeTest() throws Throwable {
        Map<String, String> permitTypeData = new HashMap<>();
        permitTypeData.put("id", "1");
        permitTypeData.put("name", "parmitType");
        String validPermitType = jsonMapper.writeValueAsString(permitTypeData);
        mvc.perform(put("/api/permitstype").contentType(MediaType.APPLICATION_JSON)
                .content(validPermitType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @WithMockUser
    @Test
    public void shouldUpdatePermitTest() throws Throwable {
        mvc.perform(put("/api/permits").contentType(MediaType.APPLICATION_JSON)
                .content(permitRequestData("update")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @WithMockUser
    @Test
    public void shouldDeletePermitType() throws Throwable {
        PermitType permitType = new PermitType();
        permitType.setName("should delete");
        permitTypeService.create(permitType);
        permitType = permitTypeService.findByName("should delete");
        Long id = permitType.getId();
        mvc.perform(delete("/api/permitstype/" + id))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @WithMockUser
    @Test
    public void shouldDeletePermit() throws Throwable {
        Permit permit = new Permit();
        permit.setName("should delete");
        permit.setPermitTypeId(1L);
        permit.setLegalDocumentId(1L);
        permit.setFormId(1L);
        permit.setNumber(" ");
        permit.setTerm(" ");
        permit.setPropose(" ");
        permit.setStatus((byte) 1);
        PermitType permitType = permitTypeService.findById(1L);
        permitService.create(permit, permitType);
        permit = permitService.findByName("should delete");
        Long id = permit.getId();
        mvc.perform(delete("/api/permits/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

    @WithMockUser
    @Test
    public void shouldGetListPermits() throws Throwable {
        mvc.perform(get("/api/permits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    private String permitRequestData(String method) throws JsonProcessingException {
        Map<String, String> permitData = new HashMap<>();

        permitData.put("permitTypeId", "1");
        permitData.put("legalDocumentId", "1");
        permitData.put("formId", "1");
        permitData.put("number", " ");
        permitData.put("term", " ");
        permitData.put("propose", " ");
        permitData.put("status", "1");
        if (method.equals("update") || method.equals("delete")) {
            permitData.put("id", "1");
            permitData.put("name", "Test permit updated");
        }
        else {
            permitData.put("name", "Test permit");
        }
        String validPermit = jsonMapper.writeValueAsString(permitData);
        return validPermit;
    }


    private void clearPermit() {
        Permit permit = permitService.findByName("Test permit");
        if (permit != null) {
            permitService.delete(permit.getId());
        }
    }
    private void clearpermitType(){
        PermitType permitType = permitTypeService.findByName("permitType test");
        if(permitType != null){
            permitTypeService.delete(permitType.getId());
        }
    }
}
