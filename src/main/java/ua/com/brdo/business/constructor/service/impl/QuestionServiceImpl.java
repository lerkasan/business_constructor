package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.InputType;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.InputTypeRepository;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepo;

    private InputTypeRepository inputTypeRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, InputTypeRepository inputTypeRepo) {
        this.questionRepo = questionRepo;
        this.inputTypeRepo = inputTypeRepo;
    }

    @Transactional
    @Override
    public Question create(final Question question) {
        Objects.requireNonNull(question);
        InputType inputType = question.getInputType();
        Objects.requireNonNull(inputType);
        if (!inputTypeRepo.findByTitle(inputType.getTitle()).isPresent()) {
            inputTypeRepo.saveAndFlush(inputType);
        }
        return questionRepo.saveAndFlush(question);
    }

    @Transactional
    @Override
    public Question update(final Question question) {
        Objects.requireNonNull(question);
        InputType inputType = question.getInputType();
        Objects.requireNonNull(inputType);
        if (!inputTypeRepo.findByTitle(inputType.getTitle()).isPresent()) {
            inputTypeRepo.saveAndFlush(inputType);
        }
        return questionRepo.saveAndFlush(question);
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        questionRepo.delete(id);
    }

    @Override
    public Question findById(final Long id) {
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
}

