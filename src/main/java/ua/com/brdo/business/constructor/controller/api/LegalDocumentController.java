package ua.com.brdo.business.constructor.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.service.LegalDocumentService;

import java.net.URI;
import java.util.List;

import static java.lang.Long.parseLong;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/laws")
public class LegalDocumentController {
    private LegalDocumentService legalDocumentService;
    @Autowired
    public LegalDocumentController(LegalDocumentService legalDocumentService) {
        this.legalDocumentService = legalDocumentService;
    }
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createLegalDocument(@RequestBody LegalDocument legalDocument) {
        LegalDocument createdLegalDocument = legalDocumentService.create(legalDocument);
        URI location = ServletUriComponentsBuilder
                .fromUriString("laws").path("/{id}")
                .buildAndExpand(legalDocument.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdLegalDocument);
    }
    @GetMapping
    public List<LegalDocument> listLegalDocuments() {
        return legalDocumentService.findAll();
    }

    @GetMapping(path="/{id}")
    public LegalDocument getLegalDocument(@PathVariable String id){
        long longId = parseLong(id);
        return legalDocumentService.findById(longId);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateLegalDocument(@PathVariable String id, @RequestBody LegalDocument legalDocument){
        legalDocument.setId(Long.parseLong(id));
        LegalDocument updatedLegalDocument = legalDocumentService.update(legalDocument);
        return ResponseEntity.ok().body(updatedLegalDocument);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteLegalDocument(@PathVariable String id){
        legalDocumentService.delete(Long.parseLong(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
