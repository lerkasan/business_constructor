package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Option;

public interface OptionService {

    Option create(Option Option);

    Option update(Option Option);

    void delete(long id);

    void delete(Option option);

    Option findById(long id);

    Option findByTitle(String title);

    List<Option> findByQuestionId(long id);

    Option findByQuestionIdAndOptionId(long questionId, long optionId);

    Long deleteByQuestionId(long id);

    List<Option> findAll();
}


