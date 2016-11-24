package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.InputType;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.InputTypeRepository;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private final String ROLE_EXPERT = "ROLE_EXPERT";

    private QuestionRepository questionRepo;

    private InputTypeRepository inputTypeRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, InputTypeRepository inputTypeRepo) {
        this.questionRepo = questionRepo;
        this.inputTypeRepo = inputTypeRepo;
    }

    private void beforePersist(final Question question) {
        Objects.requireNonNull(question);
        InputType inputType = question.getInputType();
        Objects.requireNonNull(inputType);
        if (!inputTypeRepo.findByTitle(inputType.getTitle()).isPresent()) {
            inputTypeRepo.saveAndFlush(inputType);
        }
    }

    @Override
    @Secured(ROLE_EXPERT)
    @Transactional
    public Question create(final Question question) {
        beforePersist(question);
        Question savedQuestion = questionRepo.saveAndFlush(question);
        return savedQuestion;
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public Question update(final Question question) {
        beforePersist(question);
        Question savedQuestion = questionRepo.saveAndFlush(question);
        return savedQuestion;
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public void delete(final long id) {
        questionRepo.delete(id);
    }

    @Override
    public Question findById(final long id) {
        return questionRepo.findOne(id);
    }

    @Override
    public Question findByText(final String text) {
        return questionRepo.findByText(text).orElseThrow(() -> new NotFoundException("Question with given text was not found."));
    }

    @Override
    public List<Question> findAll() {
        return questionRepo.findAll();
    }

    @Override
    @Secured(ROLE_EXPERT)
    public Question addOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        question.addOption(option);
        return question;
    }

    @Override
    @Secured(ROLE_EXPERT)
    public Question addOptions(Question question, List<Option> options) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(options);
        options.forEach(option -> addOption(question, option));
        return question;
    }

    @Override
    @Secured(ROLE_EXPERT)
    public Question deleteOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        question.deleteOption(option);
        return question;
    }

    @Override
    @Secured(ROLE_EXPERT)
    public Question deleteOptions(Question question, List<Option> options) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(options);
        options.forEach(option -> deleteOption(question, option));
        return question;
    }
}

