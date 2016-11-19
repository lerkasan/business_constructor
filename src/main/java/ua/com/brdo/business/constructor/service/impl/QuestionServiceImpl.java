package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.entity.Question;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private QuestionRepository questionRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo) {
        this.questionRepo = questionRepo;
    }

    @Transactional
    @Override
    public Question create(final Question question) {
        Objects.requireNonNull(question);
        return questionRepo.saveAndFlush(question);
    }

    @Transactional
    @Override
    public Question update(final Question question) {
        Objects.requireNonNull(question);
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

