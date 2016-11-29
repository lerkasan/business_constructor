package ua.com.brdo.business.constructor.controller;

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

import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.service.PermitService;
import ua.com.brdo.business.constructor.service.PermitTypeService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api")
public class PermitController {

    PermitTypeService permitTypeService;
    PermitService permitService;

    @Autowired
    public PermitController(PermitTypeService permitTypeService, PermitService permitService){
        this.permitService = permitService;
        this.permitTypeService = permitTypeService;
    }

    @GetMapping(path = "/permits/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getPermit(@PathVariable String id)throws Throwable {
        Permit permit = permitService.findById(Long.parseLong(id));
        if(permit != null){
            return ResponseEntity.ok().body(permit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT_FOUND");
        }

    }

    @GetMapping(path = "/permitstype/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getPermitType(@PathVariable String id) throws Throwable {
        PermitType permitType = permitTypeService.findById(Long.parseLong(id));
        if(permitType != null){
            return ResponseEntity.ok().body(permitType);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT_FOUND");
        }
    }

    @PostMapping(path = "/permits/permittype/{id}", produces = APPLICATION_JSON_VALUE
                                    ,consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createPermit(@PathVariable String id, @RequestBody Permit permit) throws Throwable {
        PermitType permitType = permitTypeService.findById(Long.parseLong(id));
        Permit addedPermit = permitService.create(permit, permitType);
        URI location = ServletUriComponentsBuilder
                .fromUriString("permit")
                .path("/{id}")
                .buildAndExpand(addedPermit.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedPermit);
    }

    @PostMapping(path = "/permitstype", consumes = APPLICATION_JSON_VALUE
                                            ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createPermitType(@RequestBody PermitType permitType){
        PermitType addedPermitType = permitTypeService.create(permitType);
        URI location = ServletUriComponentsBuilder
                .fromUriString("permittype")
                .path("/{id}")
                .buildAndExpand(addedPermitType.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedPermitType);
    }

    @PutMapping(path = "/permits", consumes = APPLICATION_JSON_VALUE
                                        ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermit(@RequestBody Permit permit){
        Permit updatedPermit = permitService.update(permit);
        return ResponseEntity.ok().body(updatedPermit);
    }

    @PutMapping(path = "/permitstype", consumes = APPLICATION_JSON_VALUE
                                            , produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updatePermitType(@RequestBody PermitType permitType){
        PermitType updatedPermitType = permitTypeService.update(permitType);
        return ResponseEntity.ok().body(updatedPermitType);
    }

    @DeleteMapping(path = "/permits/{id}")
    public ResponseEntity deletePermit(@PathVariable String id){
        permitService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @DeleteMapping(path = "/permitstype/{id}")
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
