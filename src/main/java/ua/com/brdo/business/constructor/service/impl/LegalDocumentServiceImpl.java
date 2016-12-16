package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;
import ua.com.brdo.business.constructor.service.LegalDocumentService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service("LegalDocumentService")
public class LegalDocumentServiceImpl implements LegalDocumentService {
    private static final String NOT_FOUND = "Legal Document was not found.";
    private final LegalDocumentRepository legalDocumentRepository;

    @Autowired
    public LegalDocumentServiceImpl(LegalDocumentRepository legalDocumentRepository) {
        this.legalDocumentRepository = legalDocumentRepository;
    }

    @Override
    @Transactional
    public LegalDocument create( LegalDocument legalDocument) {
        Objects.requireNonNull (legalDocument);
        return legalDocumentRepository.saveAndFlush(legalDocument);
    }


    @Override
    public List<LegalDocument> findAll() {
        return  legalDocumentRepository.findAll();
    }

    //    @Override
//    public LegalDocument findById(long id) {
//        Optional<LegalDocument> legalDocument = Optional.ofNullable(legalDocumentRepository.findOne(id));
//        return legalDocument.orElseThrow(() -> new NotFoundException(NOT_FOUND));
//    }
    @Override
    public LegalDocument findById(long id) {
        Optional<LegalDocument> legalDocument = Optional.ofNullable(legalDocumentRepository.findOne(id));
        return legalDocument.orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }


    @Transactional
    @Override
    public LegalDocument update(final LegalDocument legalDocument) {
        Optional<LegalDocument> existingLegalDocument = Optional.ofNullable(legalDocumentRepository.findOne(legalDocument.getId()));
        if (!existingLegalDocument.isPresent()) throw new NotFoundException(NOT_FOUND);
        return legalDocumentRepository.saveAndFlush(legalDocument);
    }

    @Transactional
    @Override
    public void delete(long id) {
        Optional<LegalDocument> legalDocument = Optional.ofNullable(legalDocumentRepository.findOne(id));
        if (!legalDocument.isPresent()) throw new NotFoundException(NOT_FOUND);
        else legalDocumentRepository.delete(id);
    }

    @Transactional
    @Override
    public void delete(LegalDocument legalDocument) {
        legalDocumentRepository.delete(legalDocument);
    }
}
