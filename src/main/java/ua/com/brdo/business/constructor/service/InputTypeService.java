package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.InputType;

public interface InputTypeService {

    InputType create(InputType InputType);

    InputType update(InputType InputType);

    void delete(Long id);

    InputType findById(Long id);

    InputType findByTitle(String title);

    List<InputType> findAll();
}
