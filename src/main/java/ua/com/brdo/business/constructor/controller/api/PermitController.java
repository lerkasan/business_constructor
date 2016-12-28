package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api")
public class PermitController {

    private PermitTypeService permitTypeService;
    private PermitService permitService;

    @Autowired
    public PermitController(PermitTypeService permitTypeService, PermitService permitService){
        this.permitService = permitService;
        this.permitTypeService = permitTypeService;
    }

    @ApiIgnore
    @ModelAttribute
    private Permit lookUpPermitById(@PathVariable(value = "permitId", required = false) Long id){
        Permit permit = null;
        if (id != null) permit = permitService.findById(id);
        return permit;
    }

    @GetMapping(path = "/permits", produces = APPLICATION_JSON_VALUE)
    public List<Permit> getListPermits(){
        return permitService.findAll();
    }

    @GetMapping(path = "/permits/{permitId}", produces = APPLICATION_JSON_VALUE)
    public Permit getPermit(@ApiIgnore @ModelAttribute("permitId") Permit permit){
        return permit;
    }

    @GetMapping(path = "/permittypes/{permitTypeId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getPermitType(@PathVariable String permitTypeId) {
        PermitType permitType = permitTypeService.findById(Long.parseLong(permitTypeId));
        return ResponseEntity.ok().body(permitType);
    }

    @PostMapping(path = "/permittypes/{permitTypeId}/permits", produces = APPLICATION_JSON_VALUE
                                    ,consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createPermit(@PathVariable long permitTypeId, @RequestBody Permit permit) {
        PermitType permitType = permitTypeService.findById(permitTypeId);
        Permit addedPermit = permitService.create(permit, permitType);
        URI location = ServletUriComponentsBuilder
                .fromUriString("permit")
                .path("/{id}")
                .buildAndExpand(addedPermit.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedPermit);
    }

    @PostMapping(path = "/permittypes", consumes = APPLICATION_JSON_VALUE
                                            ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createPermitType(@RequestBody PermitType permitType){
        PermitType addedPermitType = permitTypeService.create(permitType);
        URI location = ServletUriComponentsBuilder
                .fromUriString("permittypes")
                .path("/{id}")
                .buildAndExpand(addedPermitType.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedPermitType);
    }

    @PutMapping(path = "/permits/{permitId}", consumes = APPLICATION_JSON_VALUE
                                        ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermit(@PathVariable long permitId, @RequestBody Permit permit){
        permit.setId(permitId);
        Permit updatedPermit = permitService.update(permit);
        return ResponseEntity.ok().body(updatedPermit);
    }

    @PutMapping(path = "/permittypes/{permitTypeId}", consumes = APPLICATION_JSON_VALUE
                                            , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermitType(@PathVariable long permitTypeId, @RequestBody PermitType permitType){
        permitType.setId(permitTypeId);
        PermitType updatedPermitType = permitTypeService.update(permitType);
        return ResponseEntity.ok().body(updatedPermitType);
    }

    @DeleteMapping(path = "/permits/{permitId}")
    public ResponseEntity deletePermit(@PathVariable long permitId){
        permitService.delete(permitId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @DeleteMapping(path = "/permittypes/{permitTypeId}")
    public ResponseEntity deletePermitType(@PathVariable long permitTypeId){
        permitTypeService.delete(permitTypeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }
}
