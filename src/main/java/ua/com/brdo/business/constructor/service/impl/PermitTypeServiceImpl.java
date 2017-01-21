package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.repository.PermitTypeRepository;
import ua.com.brdo.business.constructor.service.PermitTypeService;

import static java.util.Objects.isNull;

@Service
public class PermitTypeServiceImpl implements PermitTypeService {
    private PermitTypeRepository repository;

    @Autowired
    public PermitTypeServiceImpl(PermitTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    @Override
    public PermitType create(PermitType permitType) {
        Objects.requireNonNull(permitType);
        return repository.saveAndFlush(permitType);
    }

    @Transactional
    @Override
    public PermitType update(PermitType permitType) {
        Objects.requireNonNull(permitType);
        return repository.saveAndFlush(permitType);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    @Override
    public PermitType findById(Long id){
        PermitType maybePermitType = repository.findOne(id);
        if(isNull(maybePermitType)) throw new NotFoundException(String.format("Permit type with id=%d is not found", id));
        return maybePermitType;
    }

    @Override
    public PermitType findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new NotFoundException(String.format("Permit type with name=%s is not found", name)));
    }

    @Override
    public List<PermitType> findAll() {
        return repository.findAll();
    }

}
