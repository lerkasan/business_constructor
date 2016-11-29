package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.InputType;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.InputTypeRepository;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private final String ROLE_EXPERT = "ROLE_EXPERT";

    private final String ROLE_USER = "ROLE_USER";

    private QuestionRepository questionRepo;

    private InputTypeRepository inputTypeRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, InputTypeRepository inputTypeRepo) {
        this.questionRepo = questionRepo;
        this.inputTypeRepo = inputTypeRepo;
    }

    @Transactional
    private void beforePersist(final Question question) {
        InputType defaultInputType = new InputType();
        Objects.requireNonNull(question);
        Long questionId = question.getId();
        InputType inputType = question.getInputType();
        Optional<InputType> defaultInputTypeOptional = inputTypeRepo.findByTitle("checkbox");
        if (defaultInputTypeOptional.isPresent()) {
            defaultInputType = defaultInputTypeOptional.get();
        }
        if (inputType == null) {
            if (questionId != null) { //not to set default inputType when updating existing question if already set inputType field isn't repeated in json
                question.setInputType(questionRepo.findOne(questionId)
                        .getInputType());
            } else {
                question.setInputType(defaultInputType);
            }
        } else if (!inputTypeRepo.findByTitle(inputType.getTitle()).isPresent()) {
            inputTypeRepo.saveAndFlush(inputType);
        } else {
            question.setInputType(inputTypeRepo.findByTitle(inputType.getTitle()).get());
        }
    }

    @Override
    @Secured(ROLE_EXPERT)
    @Transactional
    public Question create(final Question question) {
        beforePersist(question);
        return questionRepo.saveAndFlush(question);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public Question update(final Question question) {
        beforePersist(question);
        return questionRepo.saveAndFlush(question);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public void delete(final long id) {
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException("Question with id = " + id + " does not exist.");
        }
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
    @Secured({ROLE_EXPERT, ROLE_USER})
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
    public Question deleteOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        question.deleteOption(option);
        return question;
    }
}

