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

@Service("ProcedureService")
public class ProcedureServiceImpl implements ProcedureService {

    private ProcedureRepository procedureRepository;

    @Autowired
    public ProcedureServiceImpl(ProcedureRepository procedureRepository){
        this.procedureRepository = procedureRepository;
    }
    @Transactional
    @Override
     public Procedure create(Procedure procedure){
        Objects.requireNonNull(procedure);
        return procedureRepository.saveAndFlush(procedure);
    }

    @Override
    @Transactional
    public Procedure update(Procedure procedure){
        Objects.requireNonNull(procedure);
        return procedureRepository.saveAndFlush(procedure);
        }

    @Override
    @Transactional
    public void delete(Long id){
        procedureRepository.delete(id);
    }

    @Override
    public Procedure findById(Long id){
        return procedureRepository.findOne(id);

    }

    @Override
    public Procedure findByName(String name){
        return procedureRepository.findByName(name).orElseThrow(() -> new NotFoundException("Procedure Document with given name was not found."));
    }

    @Override
    public List<Procedure> getAll(){
        return procedureRepository.findAll();
    }


}
