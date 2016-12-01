package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping(path = "/permits/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getPermit(@PathVariable String id){
        Permit permit = permitService.findById(Long.parseLong(id));
        if(permit == null) throw new NotFoundException(String.format("Permit with id=%s is not found", id));
        return ResponseEntity.ok().body(permit);
    }

    @GetMapping(path = "/permittypes/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getPermitType(@PathVariable String id){
        PermitType permitType = permitTypeService.findById(Long.parseLong(id));
        if(permitType == null) throw new NotFoundException(String.format("PermitType with id=%s is not found", id));
        return ResponseEntity.ok().body(permitType);
    }

    @PostMapping(path = "/permits/permittypes/{id}", produces = APPLICATION_JSON_VALUE
                                    ,consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createPermit(@PathVariable String id, @RequestBody Permit permit){
        PermitType permitType = permitTypeService.findById(Long.parseLong(id));
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

    @PutMapping(path = "/permits/{id}", consumes = APPLICATION_JSON_VALUE
                                        ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermit(@PathVariable String id, @RequestBody Permit permit){
        permit.setId(Long.parseLong(id));
        Permit updatedPermit = permitService.update(permit);
        return ResponseEntity.ok().body(updatedPermit);
    }

    @PutMapping(path = "/permittypes/{id}", consumes = APPLICATION_JSON_VALUE
                                            , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermitType(@PathVariable String id, @RequestBody PermitType permitType){
        permitType.setId(Long.parseLong(id));
        PermitType updatedPermitType = permitTypeService.update(permitType);
        return ResponseEntity.ok().body(updatedPermitType);
    }

    @DeleteMapping(path = "/permits/{id}")
    public ResponseEntity deletePermit(@PathVariable String id){
        permitService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @DeleteMapping(path = "/permittypes/{id}")
    public ResponseEntity deletePermitType(@PathVariable String id){
        permitTypeService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @GetMapping(path = "/permits", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getListPermits(){
        List<Permit> listPermits = permitService.findAll();
        return ResponseEntity.ok().body(listPermits);
    }
}
