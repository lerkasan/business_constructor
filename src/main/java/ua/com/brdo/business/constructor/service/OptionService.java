package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Option;

public interface OptionService {

    Option create(Option Option);

    Option update(Option Option);

    void delete(Long id);

    Option findById(Long id);

    Option findByTitle(String title);

    List<Option> findAll();
}

