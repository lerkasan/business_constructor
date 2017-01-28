package ua.com.brdo.business.constructor.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.service.ProcedureDocumentService;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/documents", produces = APPLICATION_JSON_VALUE)
public class ProcedureDocumentController {

    private ProcedureDocumentService procedureDocumentService;

    @Autowired
    public ProcedureDocumentController(ProcedureDocumentService procedureDocumentService){
        this.procedureDocumentService = procedureDocumentService;
    }

    @ApiIgnore
    @ModelAttribute
    private ProcedureDocument lookUpProcedureDocumentById(@PathVariable(value = "id", required = false) Long id) {
        return id == null ? null : procedureDocumentService.findById(id);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ProcedureDocument getProcedureDocument(@ApiIgnore @ModelAttribute("id") ProcedureDocument procedureDocument) {
        return procedureDocument;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<ProcedureDocument> getListProcedureDocument() {
        return procedureDocumentService.getAll();
    }

    public ResponseEntity deleteProcedureDocument(@ApiIgnore @ModelAttribute("id") ProcedureDocument procedureDocument) {
        procedureDocumentService.delete(procedureDocument);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateProcedureDocument(@ApiIgnore @ModelAttribute("id") ProcedureDocument procedureDocument, @RequestBody ProcedureDocument updateProcedureDocument) {
        final long id = procedureDocument.getId();
        updateProcedureDocument.setId(id);
        final ProcedureDocument updatedProcDoc = procedureDocumentService.update(updateProcedureDocument);
        return ResponseEntity.ok().body(updatedProcDoc);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createProcedureDocument(@Valid @RequestBody ProcedureDocument procedureDocument) {
        ProcedureDocument addedProcedureDocument = procedureDocumentService.create(procedureDocument);
        URI location = ServletUriComponentsBuilder
                .fromUriString("documents")
                .path("/{id}")
                .buildAndExpand(procedureDocument.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedProcedureDocument);
    }
}
