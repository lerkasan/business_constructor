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

    QuestionOption findByQuestionAndOptionId(long questionId, long optionId);

    List<QuestionOption> findAll();
}
