package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.QuestionOption;
import ua.com.brdo.business.constructor.repository.QuestionOptionRepository;
import ua.com.brdo.business.constructor.service.QuestionOptionService;

@Service("QuestionOptionService")
public class QuestionOptionServiceImpl implements QuestionOptionService {

    private final String ROLE_EXPERT = "ROLE_EXPERT";

    private QuestionOptionRepository questionOptionRepo;

    @Autowired
    public QuestionOptionServiceImpl(QuestionOptionRepository questionOptionRepo) {
        this.questionOptionRepo = questionOptionRepo;
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public QuestionOption create(final QuestionOption questionOption) {
        Objects.requireNonNull(questionOption);
        return questionOptionRepo.saveAndFlush(questionOption);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public QuestionOption update(final QuestionOption questionOption) {
        Objects.requireNonNull(questionOption);
        return questionOptionRepo.saveAndFlush(questionOption);
    }

    @Override
    @Secured(ROLE_EXPERT)
    @Transactional
    public void delete(final long id) {
        questionOptionRepo.delete(id);
    }

    @Override
    public QuestionOption findById(final long id) {
        return questionOptionRepo.findOne(id);
    }

    @Override
    public List<QuestionOption> findByQuestionId(long id) {
        return questionOptionRepo.findByQuestionId(id);
    }

    @Override
    public List<QuestionOption> findByOptionId(long id) {
        return questionOptionRepo.findByOptionId(id);
    }

    @Override
    public QuestionOption findByQuestionAndOptionId(long questionId, long optionId) {
        return questionOptionRepo.findByQuestionIdAndOptionId(questionId, optionId).orElseThrow(() -> new NotFoundException("Specified question_id and option_id weren't found."));
    }

    @Override
    public List<QuestionOption> findAll() {
        return questionOptionRepo.findAll();
    }
}