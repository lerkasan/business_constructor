package ua.com.brdo.business.constructor.service;

import java.util.List;
import ua.com.brdo.business.constructor.model.BusinessType;

public interface BusinessTypeService {

  BusinessType create(final BusinessType businessType);

  BusinessType update(final BusinessType businessType);

  void delete(final long id);

  void delete(final BusinessType businessType);

  BusinessType findById(final long id);

  BusinessType findByTitle(final String title);

  BusinessType findByCodeKved(final String codeKved);

  List<BusinessType> findAll();
}
