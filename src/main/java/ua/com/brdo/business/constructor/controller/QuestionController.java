package ua.com.brdo.business.constructor.controller;

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

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.service.OptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/questions", produces = APPLICATION_JSON_VALUE)
public class QuestionController {

    private QuestionService questionService;
    private OptionService optionService;

    @Autowired
    public QuestionController(QuestionService questionService, OptionService optionService) {
        this.questionService = questionService;
        this.optionService = optionService;
    }

    @ApiIgnore
    @ModelAttribute
    private Question lookupQuestionById(@PathVariable(value = "questionId", required = false) Long id) {
        Question question = new Question();
        if (id != null) {
            question = questionService.findById(id);
        }
        return question;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createQuestion(@Valid @RequestBody Question question) {
        Question createdQuestion = questionService.create(question);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/questions").path("/{id}")
                .buildAndExpand(question.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdQuestion);
    }

    @GetMapping
    public List<Question> listQuestions() {
        return questionService.findAll();
    }

    @GetMapping(path = "/{questionId}")
    public Question getQuestion(@ApiIgnore @ModelAttribute Question question, @PathVariable Long questionId) {
        return question;
    }

    @PutMapping(path = "/{questionId}", consumes = APPLICATION_JSON_VALUE)
    public Question updateQuestion(@ApiIgnore @ModelAttribute Question question, @Valid @RequestBody Question updatedQuestion) {
        Long questionId = question.getId();
        optionService.deleteByQuestionId(questionId);
        updatedQuestion.setId(questionId);
        return questionService.update(updatedQuestion);
    }

    @ApiOperation(value = "Delete a question with given id")
    @DeleteMapping(path = "/{questionId}")
    public ResponseEntity deleteQuestion(@ApiIgnore @ModelAttribute Question question) {
        questionService.delete(question);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping(path = "/{questionId}/options", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createOption(@ApiIgnore @ModelAttribute Question question, @Valid @RequestBody Option option) {
        questionService.addOption(question, option);
        question = questionService.update(question);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/questions/{questionId}/options").path("/{id}")
                .buildAndExpand(question.getId(), option.getId())
                .toUri();
        return ResponseEntity.created(location).body(option);
    }

    @GetMapping(path = "/{questionId}/options")
    public List<Option> listOptions(@ApiIgnore @ModelAttribute Question question) {
        return question.getOptions();
    }

    @GetMapping(path = "/{questionId}/options/{optionId}")
    public Option getOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId) {
        long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        return option;
    }

    @PutMapping(path = "/{questionId}/options/{optionId}", consumes = APPLICATION_JSON_VALUE)
    public Option updateOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId, @Valid @RequestBody Option modifiedOption) {
        long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        String modifiedTitle = modifiedOption.getTitle();
        Question modifiedNextQuestion = modifiedOption.getNextQuestion();
        Procedure modifiedProcedure = modifiedOption.getProcedure();
        option.setTitle(modifiedTitle); //TODO Don't forget set here modified procedure in future tasks
        option.setNextQuestion(modifiedNextQuestion);
        option.setProcedure(modifiedProcedure);
        return optionService.update(option);
    }

    @DeleteMapping(path = "/{questionId}/options/{optionId}")
    public ResponseEntity deleteOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId) {
        Long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        optionService.delete(option);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping(path = "/{questionId}/options/{optionId}/next-question")
    public Question getNextQuestionForOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId) {
        long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        return option.getNextQuestion();
    }

    @PostMapping(path = "/{questionId}/options/{optionId}/next-question", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity setNextQuestionForOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId, @RequestBody Question nextQuestion) {
        long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        option.setNextQuestion(nextQuestion);
        option = optionService.update(option);
        URI location = ServletUriComponentsBuilder
                .fromUriString("/api/questions/{questionId}/options/{optionId}")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(option);
    }

    @PutMapping(path = "/{questionId}/options/{optionId}/next-question", consumes = APPLICATION_JSON_VALUE)
    public Option updateNextQuestionForOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId, @RequestBody Question nextQuestion) {
        long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        option.setNextQuestion(nextQuestion);
        return optionService.update(option);
    }

    @DeleteMapping(path = "/{questionId}/options/{optionId}/next-question")
    public ResponseEntity deleteNextQuestionFromOption(@ApiIgnore @ModelAttribute Question question, @PathVariable Long optionId) {
        Long questionId = question.getId();
        Option option = optionService.findByQuestionIdAndOptionId(questionId, optionId);
        option.setNextQuestion(null);
        optionService.update(option);
        return ResponseEntity
                .noContent()
                .build();
    }
}