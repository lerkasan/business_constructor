package ua.com.brdo.business.constructor.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.ProcedureType;
import ua.com.brdo.business.constructor.repository.ProcedureRepository;
import ua.com.brdo.business.constructor.service.ProcedureService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("ProcedureService")
public class ProcedureServiceImpl implements ProcedureService {
    private static final String NOT_FOUND = "Procedure was not found.";
    private ProcedureRepository procedureRepository;

    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository){
        this.procedureRepository = procedureRepository;
    }
    @Transactional
    @Override
     public Procedure create(final Procedure procedure){
        return procedureRepository.saveAndFlush(Optional.ofNullable(procedure)
                .orElseThrow(() -> new NotFoundException("Cannot create Null procedure")));
    }

    @Override
    @Transactional
    public Procedure update(final Procedure procedure){
        Optional.ofNullable(procedure).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if(procedureRepository.exists(procedure.getId()))
            return procedureRepository.saveAndFlush(procedure);
        else throw new NotFoundException(NOT_FOUND);
        }

    @Override
    @Transactional
    public void delete(final Long id){
        if (procedureRepository.exists(id))
            procedureRepository.delete(id);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    public void delete(final Procedure procedure) {
        Optional.ofNullable(procedure).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if (procedureRepository.exists(procedure.getId()))
            procedureRepository.delete(procedure);
        else throw new NotFoundException(NOT_FOUND);
    }


    @Override
    public Procedure findById(final Long id){
        Optional<Procedure> procedure = Optional.ofNullable(procedureRepository.findOne(id));
        return procedure.orElseThrow(() -> new NotFoundException(NOT_FOUND));

    }

    @Override
    public Procedure findByName(final String name){
        return procedureRepository.findByName(name).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<Procedure> getAll(){
        return procedureRepository.findAll();
    }




}
