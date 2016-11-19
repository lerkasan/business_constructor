package ua.com.brdo.business.constructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import ua.com.brdo.business.constructor.entity.Question;
import ua.com.brdo.business.constructor.service.QuestionService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
//@PreAuthorize("hasRole('ROLE_EXPERT')")
@RequestMapping(path = "/questions")
public class QuestionController {

    private QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(method = {POST}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createQuestion(@RequestBody Question question) {
        Question createdQuestion = questionService.create(question);
        URI location = ServletUriComponentsBuilder.fromUriString("questions").path("/{id}")
                .buildAndExpand(question.getId()).toUri();
        return ResponseEntity.created(location).body(createdQuestion);
    }

    @RequestMapping(method = {GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Question> listQuestions() {
        return questionService.findAll();
    }

    @RequestMapping(path = "/{id}", method = {GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public Question getQuestion(@PathVariable String id) {
        return questionService.findById(Integer.valueOf(id).longValue());
    }

    @RequestMapping(path = "/{id}", method = {POST}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Question updateQuestion(@PathVariable String id, @Valid Question question) {
        return questionService.update(question);
    }
}
