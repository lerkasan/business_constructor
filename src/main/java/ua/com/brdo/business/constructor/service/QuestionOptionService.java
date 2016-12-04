package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.QuestionOption;

public interface QuestionOptionService {

    QuestionOption create(QuestionOption questionOption);

    QuestionOption update(QuestionOption questionOption);

    void delete(long id);

    QuestionOption findById(long id);

    List<QuestionOption> findByQuestionId(long id);

    List<QuestionOption> findByOptionId(long id);

    Long deleteByQuestionIdAndOptionId(long questionId, long optionId);

    QuestionOption findByQuestionIdAndOptionId(long questionId, long optionId);

    Long deleteByQuestionId(long questionId);

    List<QuestionOption> findAll();
}
