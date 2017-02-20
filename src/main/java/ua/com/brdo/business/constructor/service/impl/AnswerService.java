package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.Answer;
import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.AnswerRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.service.OptionService;

@Service
public class AnswerService {

    private AnswerRepository answerRepo;
    private OptionService optionService;
    private static final String NOT_FOUND = "Specified answer was not found.";

    @Autowired
    public AnswerService(AnswerRepository answerRepo, OptionService optionService) {
        this.answerRepo = answerRepo;
        this.optionService = optionService;
    }

    @Transactional
    public Answer create(final Answer answer) {
        Objects.requireNonNull(answer);
        return answerRepo.saveAndFlush(answer);
    }

    @Transactional
    public Answer update(final Answer answer) {
        Objects.requireNonNull(answer);
        return answerRepo.saveAndFlush(answer);
    }

    @Transactional
    public void delete(final long id) {
        answerRepo.delete(id);
    }

    @Transactional
    public void delete(final Answer answer) {
        answerRepo.delete(answer);
    }

    @Transactional
    public void deleteByIdAndBusinessId(final Long answerId, final Long businessId) {
        answerRepo.deleteByIdAndBusinessId(answerId, businessId);
    }

    public Answer findById(final long id) {
        Answer answer = answerRepo.findOne(id);
        if (answer == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return answer;
    }

    public Answer findByIdAndBusinessId(final Long answerId, final Long businessId) {
        return answerRepo.findByIdAndBusinessId(answerId, businessId).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    public List<Answer> findByBusiness(final Business business) {
        return answerRepo.findByBusiness(business);
    }

    public List<Answer> findAll() {
        return answerRepo.findAll();
    }

    private boolean isAnyAnswer(final Question question, final Business business) {
        return answerRepo.isAnyAnswer(question, business);
    }

    public void checkUniqueAnswer(final Question question, final Business business) {
        if (isAnyAnswer(question, business)) {
            throw new IllegalArgumentException("This question is single choice, so only one option is accepted as an answer to this question.");
        }
    }

    public Question getQuestion(final Answer answer) {
        Option option = answer.getOption();
        Long optionId = option.getId();
        Option persistedOption = optionService.findById(optionId);
        return persistedOption.getQuestion();
    }
}