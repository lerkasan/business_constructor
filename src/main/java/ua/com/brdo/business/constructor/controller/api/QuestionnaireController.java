package ua.com.brdo.business.constructor.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;

import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.service.QuestionnaireService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/questionnaires", produces = APPLICATION_JSON_VALUE)
public class QuestionnaireController {

    private QuestionnaireService questionnaireService;

    @Autowired
    public QuestionnaireController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @ApiIgnore
    @ModelAttribute
    private Questionnaire lookupQuestionnaireById(@PathVariable(value = "questionnaireId", required = false) Long id) {
        Questionnaire questionnaire = new Questionnaire();
        if (id != null) {
            questionnaire = questionnaireService.findById(id);
        }
        return questionnaire;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createQuestionnaire(@Valid @RequestBody Questionnaire questionnaire) {
        Questionnaire createdQuestionnaire = questionnaireService.create(questionnaire);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/questionnaires").path("/{id}")
                .buildAndExpand(questionnaire.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdQuestionnaire);
    }

    @GetMapping
    public List<Questionnaire> listQuestionnaires() {
        return questionnaireService.findAll();
    }

    @GetMapping(path = "/{questionnaireId}")
    public Questionnaire getQuestionnaire(@ApiIgnore @ModelAttribute Questionnaire questionnaire, @PathVariable Long questionnaireId) {
        return questionnaire;
    }

    @PutMapping(path = "/{questionnaireId}")
    public Questionnaire updateQuestionnaire(@ApiIgnore @ModelAttribute Questionnaire questionnaire, @Valid @RequestBody Questionnaire updatedQuestionnaire) {
        Long questionnaireId = questionnaire.getId();
        updatedQuestionnaire.setId(questionnaireId);
        return questionnaireService.update(updatedQuestionnaire);
    }

    @DeleteMapping(path = "/{questionnaireId}")
    public ResponseEntity deleteQuestionnaire(@ApiIgnore @ModelAttribute Questionnaire questionnaire) {
        questionnaireService.delete(questionnaire);
        return ResponseEntity
                .noContent()
                .build();
    }
}
