package ua.com.brdo.business.constructor.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.service.ProcedureService;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/procedures", produces = APPLICATION_JSON_VALUE)
public class ProcedureController {

    private ProcedureService procedureService;

    @Autowired
    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @ApiIgnore
    @ModelAttribute
    private Procedure lookUpProcedureById(@PathVariable(value = "id", required = false) Long id) {
        return id == null ? null : procedureService.findById(id);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Procedure getProcedure(@ApiIgnore @ModelAttribute("id") Procedure procedure) {
        return procedure;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<Procedure> getListProcedure() {
        return procedureService.getAll();
    }

    public ResponseEntity deleteProcedure(@ApiIgnore @ModelAttribute("id") Procedure procedure) {
        procedureService.delete(procedure);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE)
    public ResponseEntity updateProcedure(@ApiIgnore @ModelAttribute("id") Procedure procedure, @RequestBody Procedure updateProcedure) {
        final long id = procedure.getId();
        updateProcedure.setId(id);
        final Procedure updatedProc = procedureService.update(updateProcedure);
        return ResponseEntity.ok().body(updatedProc);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createProcedure(@Valid @RequestBody Procedure procedure) {
        Procedure addedProcedure = procedureService.create(procedure);
        URI location = ServletUriComponentsBuilder
                .fromUriString("procedure")
                .path("/{id}")
                .buildAndExpand(procedure.getId())
                .toUri();
        return ResponseEntity.created(location).body(addedProcedure);
    }
}
