package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private static final String NOT_FOUND = "Question was not found.";

    private QuestionRepository questionRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    private Question addOptions(Question question) {
        List<Option> options = question.getOptions();
        if (options != null && !options.isEmpty()) {
           options.forEach(option -> option.setQuestion(question));
        }
        return question;
    }

    private Question preprocess(Question question) {
        return addOptions(question);
    }

    @Override
    @Transactional
    public Question create(final Question question) {
        Objects.requireNonNull(question);
        Question processedQuestion = preprocess(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    public Question update(final Question question) {
        Objects.requireNonNull(question);
        Long id = question.getId();
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        Question processedQuestion = preprocess(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        questionRepo.delete(id);
    }

    @Override
    @Transactional
    public void delete(Question question) {
        questionRepo.delete(question);
    }

    @Override
    public Question findById(final long id) {
        Question question = questionRepo.findOne(id);
        if (question == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return question;
    }

    @Override
    public Question findByText(final String text) {
        return questionRepo.findByText(text).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<Question> findAll() {
        return questionRepo.findAll();
    }

    @Override
    public Question addOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        option.setQuestion(question);
        question.addOption(option);
        return question;
    }

    @Override
    public Question deleteOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        question.deleteOption(option);
        return question;
    }

    @Override
    public void deleteOptions(Question question) {
        Objects.requireNonNull(question);
        List<Option> options = question.getOptions();
        if (options != null) {
            options.clear();
        }
    }
}

