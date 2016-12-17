package ua.com.brdo.business.constructor.service;

import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.ProcedureType;


import java.util.List;

public interface ProcedureService {

    Procedure create(Procedure procedure);

    Procedure update(Procedure procedure);

    void delete(Long id);

    Procedure findById(Long id);

    Procedure findByName(String name);

    List<Procedure> getAll();

    //Procedure addProcedureType(Procedure procedure, ProcedureType procedureType);

    //Procedure deleteProcedureType(Procedure procedure, ProcedureType procedureType);
}
