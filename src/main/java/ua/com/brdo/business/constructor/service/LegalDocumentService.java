package ua.com.brdo.business.constructor.service;


import ua.com.brdo.business.constructor.model.LegalDocument;

import java.util.List;

public interface LegalDocumentService {
    LegalDocument create (LegalDocument legalDocument);
    List<LegalDocument> findAll();
    LegalDocument findById(long id);
    LegalDocument update(LegalDocument legalDocument);
    void delete (long id);
    void delete (LegalDocument legalDocument);
}
