package ua.com.brdo.business.constructor.controller.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.service.ProcedureService;
import ua.com.brdo.business.constructor.service.ProcedureTypeService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.Long.parseLong;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/procedures", produces = APPLICATION_JSON_VALUE)
public class ProcedureController {

    private ProcedureService procedureService;
    //private ProcedureTypeService procedureTypeService;

    @Autowired
    public ProcedureController(ProcedureService procedureService){
        this.procedureService = procedureService;
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getProcedure(@PathVariable String id){
        Procedure procedure = procedureService.findById(parseLong(id));
        if(procedure == null) throw new NotFoundException(String.format("Procedure with id=%s is not found", id));
        return ResponseEntity.ok().body(procedure);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getListProcedure(){
        List<Procedure> listProcedure = procedureService.getAll();
        return ResponseEntity.ok().body(listProcedure);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteProcedure(@PathVariable String id){
        procedureService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE
            ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateProcedure(@PathVariable String id, @RequestBody Procedure procedure){
        procedure.setId(Long.parseLong(id));
        Procedure updatedProcedure = procedureService.update(procedure);
        return ResponseEntity.ok().body(updatedProcedure);
    }


    @PostMapping(produces = APPLICATION_JSON_VALUE
            ,consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createProcedure(@Valid @RequestBody Procedure procedure){
        Procedure addedProcedure = procedureService.create(procedure);
        URI location = ServletUriComponentsBuilder
                .fromUriString("procedure")
                .path("/{id}")
                .buildAndExpand(procedure.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedProcedure);
    }


}
