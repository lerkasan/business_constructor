package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.service.impl.BusinessService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/businesses", produces = APPLICATION_JSON_VALUE)
public class BusinessController {

    private BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @ApiIgnore
    @ModelAttribute
    private Business lookupBusinessById(@PathVariable(value = "businessId", required = false) Long businessId, Authentication authentication) {
        Business business = new Business();
        if (businessId != null) {
            business = businessService.findById(businessId);
            businessService.checkBusinessOwnership(business, authentication);
        }
        return business;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createBusiness(@Valid @RequestBody Business business, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        business.setUser(user);
        Business createdBusiness = businessService.create(business);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/businesses").path("/{id}")
                .buildAndExpand(business.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdBusiness);
    }

    @GetMapping
    public List<Business> listBusinessesByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return businessService.findByUser(user);
    }

    @GetMapping(path = "/{businessId}")
    public Business getBusiness(@ApiIgnore @ModelAttribute Business business, @PathVariable Long businessId) {
        return business;
    }

    @PutMapping(path = "/{businessId}")
    public Business updateBusiness(@ApiIgnore @ModelAttribute Business business, @Valid @RequestBody Business updatedBusiness, Authentication authentication) {
        Long businessId = business.getId();
        updatedBusiness.setId(businessId);
        User user = (User) authentication.getPrincipal();
        updatedBusiness.setUser(user);
        return businessService.update(updatedBusiness);
    }

    @DeleteMapping(path = "/{businessId}")
    public ResponseEntity deleteBusiness(@ApiIgnore @ModelAttribute Business business) {
        businessService.delete(business);
        return ResponseEntity
                .noContent()
                .build();
    }
}
