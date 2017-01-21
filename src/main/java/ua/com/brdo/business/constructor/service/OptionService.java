package ua.com.brdo.business.constructor.service;

import java.util.List;
import ua.com.brdo.business.constructor.model.Option;

public interface OptionService {

    Option create(final Option option);

    Option update(final Option option);

    void delete(final long id);

    void delete(final Option option);

    Option findById(final long id);

    Option findByTitle(final String title);

    List<Option> findByQuestionId(final long id);

    Option findByQuestionIdAndOptionId(final long questionId, final long optionId);

    Long deleteByQuestionId(final long id);

    List<Option> findAll();
}
