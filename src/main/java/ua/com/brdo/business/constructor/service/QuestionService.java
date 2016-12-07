package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;

public interface QuestionService {

    Question create(Question Question);

    Question update(Question Question);

    void delete(long id);

    void delete(Question question);

    Question findById(long id);

    Question findByText(String text);

    List<Question> findAll();

    Question addOption(Question question, Option option);

    Question deleteOption(Question question, Option option);

    Long deleteByQuestionIdAndOptionId(Long questionId, Long optionId);
}
