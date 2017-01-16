package ua.com.brdo.business.constructor.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.repository.ProcedureDocumentRepository;
import ua.com.brdo.business.constructor.service.ProcedureDocumentService;

import java.util.List;
import java.util.Objects;

@Service("ProcedureDocumentService")
public class ProcedureDocumentServiceImpl implements ProcedureDocumentService {

    private ProcedureDocumentRepository procedureDocumentRepository;

    @Autowired
    public ProcedureDocumentServiceImpl(ProcedureDocumentRepository procedureDocumentRepository){
        this.procedureDocumentRepository = procedureDocumentRepository;
    }

    @Override
    @Transactional
    public ProcedureDocument create(ProcedureDocument procedureDocument){
        Objects.requireNonNull(procedureDocument);
        return procedureDocumentRepository.saveAndFlush(procedureDocument);
    }

    @Override
    @Transactional
    public ProcedureDocument update(ProcedureDocument procedureDocument){
        Objects.requireNonNull(procedureDocument);
        return procedureDocumentRepository.saveAndFlush(procedureDocument);
    }

    @Override
    @Transactional
    public void delete(Long id){
        procedureDocumentRepository.delete(id);
    }

    @Override
    public ProcedureDocument findById(Long id){
        return procedureDocumentRepository.findOne(id);
    }

    @Override
    public ProcedureDocument findByName(String name){
        return procedureDocumentRepository.findByName(name).orElseThrow(() -> new NotFoundException("Procedure Document with given name was not found."));
    }

    @Override
    public List<ProcedureDocument> getAll(){
        return procedureDocumentRepository.findAll();
    }
}
