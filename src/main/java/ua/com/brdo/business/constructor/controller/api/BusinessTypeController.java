package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.service.BusinessTypeService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/business-types", produces = APPLICATION_JSON_VALUE)
public class BusinessTypeController {

    private BusinessTypeService businessTypeService;

    @Autowired
    public BusinessTypeController(BusinessTypeService businessTypeService) {
        this.businessTypeService = businessTypeService;
    }

    @ModelAttribute
    private BusinessType lookupBusinessTypeById(@PathVariable(value = "businessTypeId", required = false) Long id) {
        BusinessType businessType = new BusinessType();
        if (id != null) {
            businessType = businessTypeService.findById(id);
        }
        return businessType;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createBusinessType(@Valid @RequestBody BusinessType businessType) {
        BusinessType createdBusinessType = businessTypeService.create(businessType);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/business-types").path("/{id}")
                .buildAndExpand(businessType.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdBusinessType);
    }

    @GetMapping
    public List<BusinessType> listBusinessTypes() {
        return businessTypeService.findAll();
    }

    @GetMapping(path = "/{businessTypeId}")
    public BusinessType getBusinessType(@ModelAttribute BusinessType businessType, @PathVariable Long businessTypeId) {
        return businessType;
    }

    @PutMapping(path = "/{businessTypeId}")
    public BusinessType updateBusinessType(@ModelAttribute BusinessType businessType, @Valid @RequestBody BusinessType updatedBusinessType) {
        Long businessTypeId = businessType.getId();
        updatedBusinessType.setId(businessTypeId);
        return businessTypeService.update(updatedBusinessType);
    }

    @DeleteMapping(path = "/{businessTypeId}")
    public ResponseEntity deleteBusinessType(@ModelAttribute BusinessType businessType) {
        businessTypeService.delete(businessType);
        return ResponseEntity
                .noContent()
                .build();
    }
}
