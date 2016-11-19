package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.entity.InputType;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.repository.InputTypeRepository;
import ua.com.brdo.business.constructor.service.InputTypeService;

@Service("InputTypeService")
public class InputTypeServiceImpl implements InputTypeService {

    private InputTypeRepository inputTypeRepo;

    @Autowired
    public InputTypeServiceImpl(InputTypeRepository inputTypeRepo) {
        this.inputTypeRepo = inputTypeRepo;
    }

    @Transactional
    @Override
    public InputType create(final InputType inputType) {
        Objects.requireNonNull(inputType);
        return inputTypeRepo.saveAndFlush(inputType);
    }

    @Transactional
    @Override
    public InputType update(final InputType inputType) {
        Objects.requireNonNull(inputType);
        return inputTypeRepo.saveAndFlush(inputType);
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        inputTypeRepo.delete(id);
    }

    @Override
    public InputType findById(final Long id) {
        return inputTypeRepo.findOne(id);
    }

    @Override
    public InputType findByTitle(final String title) {
        return inputTypeRepo.findByTitle(title).orElseThrow(() -> new NotFoundException("InputType with given title was not found."));
    }

    @Override
    public List<InputType> findAll() {
        return inputTypeRepo.findAll();
    }
}

