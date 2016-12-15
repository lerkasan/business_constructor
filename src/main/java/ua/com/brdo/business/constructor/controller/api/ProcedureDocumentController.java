package ua.com.brdo.business.constructor.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.service.ProcedureDocumentService;

import javax.validation.Valid;

import java.net.URI;
import java.util.List;

import static java.lang.Long.parseLong;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/documents", produces = APPLICATION_JSON_VALUE)
public class ProcedureDocumentController {

    private ProcedureDocumentService procedureDocumentService;

    @Autowired
    public ProcedureDocumentController(ProcedureDocumentService procedureDocumentService){
        this.procedureDocumentService = procedureDocumentService;
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getProcedureDocument(@PathVariable String id){
        ProcedureDocument procedureDocument = procedureDocumentService.findById(parseLong(id));
        if(procedureDocument == null) throw new NotFoundException(String.format("procedure Document with id=%s is not found", id));
        return ResponseEntity.ok().body(procedureDocument);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getListProcedureDocument(){
        List<ProcedureDocument> listProcedureDocument = procedureDocumentService.getAll();
        return ResponseEntity.ok().body(listProcedureDocument);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deletelistProcedureDocument(@PathVariable String id){
        procedureDocumentService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("NO_CONTENT");
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE
            ,produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateProcedureDocument(@PathVariable String id, @RequestBody ProcedureDocument procedureDocument){
        procedureDocument.setId(Long.parseLong(id));
        ProcedureDocument updatedProcedureDocument = procedureDocumentService.update(procedureDocument);
        return ResponseEntity.ok().body(updatedProcedureDocument);
    }


    @PostMapping(produces = APPLICATION_JSON_VALUE
            ,consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createProcedure(@Valid @RequestBody ProcedureDocument procedureDocument){
        ProcedureDocument addedProcedureDocument = procedureDocumentService.create(procedureDocument);
        URI location = ServletUriComponentsBuilder
                .fromUriString("documents")
                .path("/{id}")
                .buildAndExpand(procedureDocument.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedProcedureDocument);
    }
}
