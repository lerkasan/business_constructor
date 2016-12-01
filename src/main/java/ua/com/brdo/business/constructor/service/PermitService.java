package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.Permit;
import ua.com.brdo.business.constructor.model.PermitType;

public interface PermitService {
    Permit create(Permit permit, PermitType permitType);

    Permit update(Permit permit);

    void delete(Long id);

    Permit findById(Long id);

    Permit findByName(String name);

    List<Permit> findAll();

//    byte[] getFileExample(Long id);
}
