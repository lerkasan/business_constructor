package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.repository.ProcedureTypeRepository;
import ua.com.brdo.business.constructor.service.ProcedureTypeService;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("ProcedureTypeService")
public class ProcedureTypeServiceImpl implements ProcedureTypeService {

    private static final String NOT_FOUND = "Procedure Type was not found.";
    private ProcedureTypeRepository procedureTypeRepository;

    @Autowired
    public ProcedureTypeServiceImpl(ProcedureTypeRepository procedureTypeRepository) {
        this.procedureTypeRepository = procedureTypeRepository;
    }

    @Transactional
    @Override
    public ProcedureType create(final ProcedureType procedureType){
        return procedureTypeRepository.saveAndFlush(Optional.ofNullable(procedureType)
                .orElseThrow(() -> new NotFoundException("Cannot create Null procedure type")));
    }

    @Override
    @Transactional
    public ProcedureType update(final ProcedureType procedureType){
        Optional.ofNullable(procedureType).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if(procedureTypeRepository.exists(procedureType.getId()))
            return procedureTypeRepository.saveAndFlush(procedureType);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    @Transactional
    public void delete(final Long id){
        if (procedureTypeRepository.exists(id))
            procedureTypeRepository.delete(id);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    public void delete(final ProcedureType procedureType) {
        Optional.ofNullable(procedureType).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if (procedureTypeRepository.exists(procedureType.getId()))
            procedureTypeRepository.delete(procedureType);
        else throw new NotFoundException(NOT_FOUND);
    }


    @Override
    public ProcedureType findById(final Long id){
        Optional<ProcedureType> procedureType = Optional.ofNullable(procedureTypeRepository.findOne(id));
        return procedureType.orElseThrow(() -> new NotFoundException(NOT_FOUND));

    }

    @Override
    public ProcedureType findByName(final String name){
        return procedureTypeRepository.findByName(name).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<ProcedureType> getAll(){
        return procedureTypeRepository.findAll();
    }
}
