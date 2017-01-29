package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;
import ua.com.brdo.business.constructor.service.LegalDocumentService;
import ua.com.brdo.business.constructor.service.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service("LegalDocumentService")
public class LegalDocumentServiceImpl implements LegalDocumentService {
    private static final String ERROR_MESSAGE = "Legal Document was not found.";
    private final LegalDocumentRepository legalDocumentRepository;

    @Autowired
    public LegalDocumentServiceImpl(LegalDocumentRepository legalDocumentRepository) {
        this.legalDocumentRepository = legalDocumentRepository;
    }

    @Override
    @Transactional
    public LegalDocument create(final LegalDocument legalDocument) {
        Objects.requireNonNull(legalDocument, "Cannot create null instead LegalDocument");
        return legalDocumentRepository.saveAndFlush(legalDocument);
    }


    @Override
    public List<LegalDocument> findAll() {
        return legalDocumentRepository.findAll();
    }

    @Override
    public LegalDocument findById(final long id) {
        Optional<LegalDocument> legalDocument = Optional.ofNullable(legalDocumentRepository.findOne(id));
        return legalDocument.orElseThrow(() -> new NotFoundException(ERROR_MESSAGE));
    }


    @Transactional
    @Override
    public LegalDocument update(final LegalDocument legalDocument) {
        Objects.requireNonNull(legalDocument, "Cannot update by null");

        if (legalDocumentRepository.exists(legalDocument.getId()))
            return legalDocumentRepository.saveAndFlush(legalDocument);
        else
            throw new NotFoundException(ERROR_MESSAGE);
    }

    @Transactional
    @Override
    public void delete(final long id) {
        if (legalDocumentRepository.exists(id))
            legalDocumentRepository.delete(id);
        else
            throw new NotFoundException(ERROR_MESSAGE);
    }

    @Transactional
    @Override
    public void delete(final LegalDocument legalDocument) {
        Objects.requireNonNull(legalDocument, "Cannot delete Document if its null!");

        if (legalDocumentRepository.exists(legalDocument.getId()))
            legalDocumentRepository.delete(legalDocument);
        else
            throw new NotFoundException(ERROR_MESSAGE);
    }
}
