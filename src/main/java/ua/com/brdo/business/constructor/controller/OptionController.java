package ua.com.brdo.business.constructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.service.OptionService;

import static java.lang.Long.parseLong;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/options", produces = APPLICATION_JSON_VALUE)
public class OptionController {

    private OptionService optionService;

    @Autowired
    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createOption(@Valid @RequestBody Option option) {
        Option createdOption = optionService.create(option);
        URI location = ServletUriComponentsBuilder
                .fromUriString("options").path("/{id}")
                .buildAndExpand(option.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdOption);
    }

    @GetMapping
    public List<Option> listOptions() {
        return optionService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Option getOption(@PathVariable String id) {
        long longId = parseLong(id);
        return optionService.findById(longId);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public Option updateOption(@PathVariable String id, @Valid @RequestBody Option option) {
        long longId = parseLong(id);
        if (optionService.findById(longId) == null) {
            throw new NotFoundException("Option with id = " + id + " does not exist.");
        }
        option.setId(Long.valueOf(id));
        return optionService.update(option);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteOption(@PathVariable String id) {
        long longId = parseLong(id);
        optionService.delete(longId);
        return ResponseEntity
                .noContent()
                .build();
    }
}