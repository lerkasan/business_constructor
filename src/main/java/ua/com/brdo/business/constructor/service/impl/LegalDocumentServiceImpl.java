package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;
import ua.com.brdo.business.constructor.service.LegalDocumentService;

import java.util.List;
import java.util.Objects;


@Service("LegalDocumentService")
public class LegalDocumentServiceImpl implements LegalDocumentService {
    private LegalDocumentRepository legalDocumentRepository;
    @Autowired
    public LegalDocumentServiceImpl (LegalDocumentRepository legalDocumentRepository){
        this.legalDocumentRepository = legalDocumentRepository;
    }
    @Override
    @Transactional
    public LegalDocument create(final LegalDocument legalDocument) {
        Objects.requireNonNull(legalDocument);
        return legalDocumentRepository.saveAndFlush(legalDocument);
    }
    @Override
    public List<LegalDocument> findAll() {
        return legalDocumentRepository.findAll();
    }
    @Override
    public LegalDocument findById(final long id) {
        return legalDocumentRepository.findOne(id);
    }
    @Transactional
    @Override
    public LegalDocument update(final LegalDocument legalDocument){
        Objects.requireNonNull(legalDocument);
        return legalDocumentRepository.saveAndFlush(legalDocument);
    }
    @Transactional
    @Override
    public void delete (long id){
        legalDocumentRepository.delete(id);
    }
}
