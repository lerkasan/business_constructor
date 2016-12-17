package ua.com.brdo.business.constructor.service;


import ua.com.brdo.business.constructor.model.ProcedureType;


import java.util.List;


public interface ProcedureTypeService {

    ProcedureType create(ProcedureType procedureType);

    ProcedureType update(ProcedureType procedureType);

    void delete(Long id);

    ProcedureType findById(Long id);

    ProcedureType findByName(String name);

    List<ProcedureType> getAll();
}
