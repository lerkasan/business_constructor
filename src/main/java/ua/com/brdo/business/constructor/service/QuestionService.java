package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Question;

public interface QuestionService {

    Question create(Question Question);

    Question update(Question Question);

    void delete(Long id);

    Question findById(Long id);

    Question findByText(String text);

    List<Question> findAll();
}
