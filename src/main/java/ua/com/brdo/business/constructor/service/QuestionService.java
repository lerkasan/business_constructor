package ua.com.brdo.business.constructor.service;

import java.util.List;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;

public interface QuestionService {

    Question create(Question question);

    Question update(Question question);

    void delete(long id);

    void delete(Question question);

    Question findById(long id);

    Question findByText(String text);

    List<Question> findByQuestionnaire(Questionnaire questionnaire);

    List<Question> findAll();

    Question addOption(Question question, Option option);

    Question deleteOption(Question question, Option option);

    void deleteOptions(Question question);
}
