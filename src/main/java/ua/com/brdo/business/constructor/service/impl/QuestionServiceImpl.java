package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.QuestionOption;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionOptionService;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private final String ROLE_EXPERT = "ROLE_EXPERT";

    private final String ROLE_USER = "ROLE_USER";

    private final String NOT_FOUND = "Question was not found.";

    private QuestionRepository questionRepo;

    private OptionRepository optionRepo;

    private QuestionOptionService questionOptionService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, OptionRepository optionRepo, QuestionOptionService questionOptionService) {
        this.questionRepo = questionRepo;
        this.optionRepo = optionRepo;
        this.questionOptionService = questionOptionService;
    }

    private Question processOptionsBeforePersist(Question question) {
        Set<QuestionOption> questionOptions = question.getQuestionOptions();
        if (questionOptions != null) {
            questionOptions.forEach(questionOption -> {
                if (questionOption != null && questionOption.getOption() != null) {
                    questionOption.setQuestion(question);
                    String optionTitle = questionOption.getOption().getTitle();
                    Optional<Option> optionOptional = optionRepo.findByTitle(optionTitle);
                    if (optionOptional.isPresent()) {
                        questionOption.setOption(optionOptional.get());
                    }
                }
            });
        }
        return question;
    }

    @Override
    @Secured(ROLE_EXPERT)
    @Transactional
    public Question create(final Question question) {
        Question processedQuestion = processOptionsBeforePersist(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public Question update(Question question) {
        Question processedQuestion = processOptionsBeforePersist(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public void delete(final long id) {
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        questionRepo.delete(id);
    }

    @Override
    public Question findById(final long id) {
        return questionRepo.findOne(id);
    }

    @Override
    public Question findByText(final String text) {
        return questionRepo.findByText(text).orElseThrow(() -> new NotFoundException(NOT_FOUND));
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

    @Override
    public Long deleteByQuestionIdAndOptionId(Long questionId, Long optionId) {
        return questionOptionService.deleteByQuestionIdAndOptionId(questionId, optionId);
    }
}

