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

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/laws")
public class LegalDocumentController {
    private LegalDocumentService legalDocumentService;

    @Autowired
    public LegalDocumentController(LegalDocumentService legalDocumentService) {
        this.legalDocumentService = legalDocumentService;
    }

    @ModelAttribute
    private LegalDocument lookUpLegalDocumentById(@PathVariable(value = "id", required = false) Long id) {
        LegalDocument legalDocument = null;
        if (id != null) legalDocument = legalDocumentService.findById(id);
        return legalDocument;
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

    @GetMapping(path = "/{id}")
    public LegalDocument getLegalDocument(@ModelAttribute("id") LegalDocument legalDocument) {
        return legalDocument;
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateLegalDocument(@ModelAttribute("id") LegalDocument legalDocument,@RequestBody LegalDocument updatedLegalDocument) {
        updatedLegalDocument.setId(legalDocument.getId());
        return ResponseEntity.ok().body(legalDocumentService.update(updatedLegalDocument));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteLegalDocument(@ModelAttribute("id") LegalDocument legalDocument) {
        legalDocumentService.delete(legalDocument);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
