package ua.com.brdo.business.constructor.service;


import ua.com.brdo.business.constructor.model.ProcedureDocument;


import java.util.List;

public interface ProcedureDocumentService {

    ProcedureDocument create(ProcedureDocument procedureDocument);

    ProcedureDocument update(ProcedureDocument procedureDocument);

    void delete(Long id);

    ProcedureDocument findById(Long id);

    ProcedureDocument findByName(String name);

    List<ProcedureDocument> getAll();

    void delete(ProcedureDocument procedureDocument);
}
