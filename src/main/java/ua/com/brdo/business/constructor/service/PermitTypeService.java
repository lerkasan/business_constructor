package ua.com.brdo.business.constructor.service;

import java.util.List;

import ua.com.brdo.business.constructor.model.PermitType;

public interface PermitTypeService {
    PermitType create(PermitType permitType);

    PermitType update(PermitType permiType);

    void delete(Long id);

    PermitType findById(Long id) throws Throwable;

    PermitType findByName(String name);

    List<PermitType> findAll();

//    List<PermitType> getTenWithOffset(Long offset);
}
