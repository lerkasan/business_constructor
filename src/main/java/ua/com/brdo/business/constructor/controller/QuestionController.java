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
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.QuestionOption;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.QuestionOptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

import static java.lang.Long.parseLong;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/api/questions", produces = APPLICATION_JSON_VALUE)
public class QuestionController {

    private QuestionService questionService;
    private OptionService optionService;
    private QuestionOptionService questionOptionService;

    @Autowired
    public QuestionController(QuestionService questionService, OptionService optionService, QuestionOptionService questionOptionService) {
        this.questionService = questionService;
        this.optionService = optionService;
        this.questionOptionService = questionOptionService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createQuestion(@Valid @RequestBody Question question) {
        Question createdQuestion = questionService.create(question);
        URI location = ServletUriComponentsBuilder
                .fromUriString("questions").path("/{id}")
                .buildAndExpand(question.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdQuestion);
    }

    @GetMapping
    public List<Question> listQuestions() {
        return questionService.findAll();
    }

    @GetMapping(path = "/{id}")
    public Question getQuestion(@PathVariable String id) {
        long longId = parseLong(id);
        return questionService.findById(longId);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public Question updateQuestion(@PathVariable String id, @Valid @RequestBody Question question) {
        long longId = parseLong(id);
        if (questionService.findById(longId) == null) {
            throw new NotFoundException("Question with id = " + id + " does not exist.");
        }
        question.setId(Long.valueOf(id));
        return questionService.update(question);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteQuestion(@PathVariable String id) {
        long longId = parseLong(id);
        questionService.delete(longId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping(path = "/{questionId}/options", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createOption(@PathVariable String questionId, @Valid @RequestBody Option option) {
        Option createdOption = optionService.create(option);
        Question question = questionService.findById(parseLong(questionId));
        questionService.addOption(question, createdOption);
        question = questionService.update(question);
        URI location = ServletUriComponentsBuilder
                .fromUriString("questions/{questionId}/options").path("/{id}")
                .buildAndExpand(question.getId(), option.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdOption);
    }

    @GetMapping(path = "/{questionId}/options")
    public List<Option> listOptions(@PathVariable String questionId) {
        List<QuestionOption> questionOptions = questionOptionService.findByQuestionId(parseLong(questionId));
        List<Option> options = new ArrayList<>();
        questionOptions.forEach(questionOption ->
                options.add(optionService
                        .findById(questionOption
                                .getOption().getId())));
        return options;
    }

    @DeleteMapping(path = "/{questionId}/options/{optionId}")
    public ResponseEntity deleteOption(@PathVariable String optionId, @PathVariable String questionId) {
        long questionIdL = parseLong(questionId);
        long optionIdL = parseLong(optionId);
        questionOptionService.delete(questionOptionService.findByQuestionAndOptionId(questionIdL, optionIdL).getId());
        return ResponseEntity
                .noContent()
                .build();
    }
}