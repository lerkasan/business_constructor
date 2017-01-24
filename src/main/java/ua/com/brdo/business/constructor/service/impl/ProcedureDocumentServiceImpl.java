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
import java.util.Optional;

@Service("ProcedureDocumentService")
public class ProcedureDocumentServiceImpl implements ProcedureDocumentService {

    private static final String NOT_FOUND = "Procedure Document was not found.";
    private ProcedureDocumentRepository procedureDocumentRepository;

    @Autowired
    public ProcedureDocumentServiceImpl(ProcedureDocumentRepository procedureDocumentRepository){
        this.procedureDocumentRepository = procedureDocumentRepository;
    }

    @Override
    @Transactional
    public ProcedureDocument create(final ProcedureDocument procedureDocument){
        return procedureDocumentRepository.saveAndFlush(Optional.ofNullable(procedureDocument)
                .orElseThrow(() -> new NotFoundException("Cannot create Null procedure document")));
    }

    @Override
    @Transactional
    public ProcedureDocument update(final ProcedureDocument procedureDocument){
        Optional.ofNullable(procedureDocument).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if(procedureDocumentRepository.exists(procedureDocument.getId()))
            return procedureDocumentRepository.saveAndFlush(procedureDocument);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    @Transactional
    public void delete(final Long id){
        if (procedureDocumentRepository.exists(id))
            procedureDocumentRepository.delete(id);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    public void delete(final ProcedureDocument procedureDocument) {
        Optional.ofNullable(procedureDocument).orElseThrow(()->new NotFoundException(NOT_FOUND));
        if (procedureDocumentRepository.exists(procedureDocument.getId()))
            procedureDocumentRepository.delete(procedureDocument);
        else throw new NotFoundException(NOT_FOUND);
    }

    @Override
    public ProcedureDocument findById(final Long id){
        Optional<ProcedureDocument> procedure = Optional.ofNullable(procedureDocumentRepository.findOne(id));
        return procedure.orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public ProcedureDocument findByName(final String name){
        return procedureDocumentRepository.findByName(name).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<ProcedureDocument> getAll(){
        return procedureDocumentRepository.findAll();
    }


}
