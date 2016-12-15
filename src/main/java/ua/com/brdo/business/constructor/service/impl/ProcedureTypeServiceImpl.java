package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.repository.ProcedureTypeRepository;
import ua.com.brdo.business.constructor.service.ProcedureTypeService;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("ProcedureTypeService")
public class ProcedureTypeServiceImpl implements ProcedureTypeService {

    private ProcedureTypeRepository procedureTypeRepository;

    @Autowired
    public ProcedureTypeServiceImpl(ProcedureTypeRepository procedureTypeRepository) {
        this.procedureTypeRepository = procedureTypeRepository;
    }

    @Override
    @Transactional
    public ProcedureType create(ProcedureType procedureType){
        Objects.requireNonNull(procedureType);
        Optional<ProcedureType> procedureTypeFromDb = procedureTypeRepository.findByName(procedureType.getName());
        if (procedureTypeFromDb.isPresent()) {
            return procedureTypeFromDb.get();
        }
        return procedureTypeRepository.saveAndFlush(procedureType);
    }
    @Override
    @Transactional
    public ProcedureType update(ProcedureType procedureType) {
        Objects.requireNonNull(procedureType);
        return procedureTypeRepository.saveAndFlush(procedureType);
    }

    @Override
    @Transactional
    public void delete(Long id){
        procedureTypeRepository.delete(id);
    }

    @Override
    public ProcedureType findById(Long id){
        return procedureTypeRepository.findOne(id);
    }

    @Override
    public ProcedureType findByName(String name){
        return procedureTypeRepository.findByName(name).orElseThrow(() -> new NotFoundException("Procedure Type with given name was not found."));
    }

    @Override
    public List<ProcedureType> getAll(){
        return procedureTypeRepository.findAll();
    }




}
