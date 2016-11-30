package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;
import ua.com.brdo.business.constructor.repository.PermitRepository;
import ua.com.brdo.business.constructor.repository.PermitTypeRepository;
import ua.com.brdo.business.constructor.service.PermitService;

import static java.util.Objects.isNull;

@Service
public class PermitServiceImpl implements PermitService {

    private PermitRepository permitRepository;
    private PermitTypeRepository permitTypeRepository;

    @Autowired
    public PermitServiceImpl(PermitRepository permitRepository, PermitTypeRepository permitTypeRepository) {
        this.permitRepository = permitRepository;
        this.permitTypeRepository = permitTypeRepository;
    }

    @Transactional
    @Override
    public Permit create(Permit permit, PermitType permitType) {
        Objects.requireNonNull(permit);
        Objects.requireNonNull(permitType);
        permit.setPermitTypeId(permitType.getId());
        return permitRepository.saveAndFlush(permit);
    }

    @Transactional
    @Override
    public Permit update(Permit permit) {
        Objects.requireNonNull(permit);
        return permitRepository.saveAndFlush(permit);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        permitRepository.delete(id);
    }

    @Override
    public Permit findById(Long id) {
        Permit permit = permitRepository.findOne(id);
        if(isNull(permit))  throw new NotFoundException(String.format("Permit with id=%s is not found", id));
        return permit;
    }

    @Override
    public Permit findByName(String name) {
        return permitRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Permit with given name wasn't found"));
    }

    @Override
    public List<Permit> findAll() {
        return permitRepository.findAll();
    }

}
