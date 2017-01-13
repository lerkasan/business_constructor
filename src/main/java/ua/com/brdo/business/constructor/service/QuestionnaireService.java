package ua.com.brdo.business.constructor.service;

import java.util.List;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;

public interface QuestionnaireService {

    Questionnaire create(final Questionnaire questionnaire);

    Questionnaire update(final Questionnaire questionnaire);

    void delete(final long id);

    void delete(final Questionnaire questionnaire);

    Questionnaire findById(final long id);

    Questionnaire findByTitle(final String title);

    List<Questionnaire> findAll();

    List<Questionnaire> findByBusinessType(final BusinessType businessType);

    Questionnaire addQuestion(final Questionnaire questionnaire, final Question question);

    Questionnaire deleteQuestion(final Questionnaire questionnaire, final Question question);

    void deleteQuestions(final Questionnaire questionnaire);
}
