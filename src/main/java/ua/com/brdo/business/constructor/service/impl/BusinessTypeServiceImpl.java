package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.repository.BusinessTypeRepository;
import ua.com.brdo.business.constructor.service.BusinessTypeService;

@Service("BusinessTypeService")
public class BusinessTypeServiceImpl implements BusinessTypeService {

  private BusinessTypeRepository businessTypeRepo;

  @Autowired
  public BusinessTypeServiceImpl(BusinessTypeRepository businessTypeRepo) {
    this.businessTypeRepo = businessTypeRepo;
  }

  @Transactional
  @Override
  public BusinessType create(final BusinessType businessType) {
    Objects.requireNonNull(businessType);
    return businessTypeRepo.saveAndFlush(businessType);
  }

  @Transactional
  @Override
  public BusinessType update(final BusinessType businessType) {
    Objects.requireNonNull(businessType);
    return businessTypeRepo.saveAndFlush(businessType);
  }

  @Transactional
  @Override
  public void delete(final long id) {
    businessTypeRepo.delete(id);
  }

  @Override
  public void delete(final BusinessType businessType) {
    businessTypeRepo.delete(businessType);
  }

  @Override
  public BusinessType findById(final long id) {
    return businessTypeRepo.findOne(id);
  }

  @Override
  public BusinessType findByTitle(final String title) {
    return businessTypeRepo.findByTitle(title)
        .orElseThrow(() -> new NotFoundException("Business type with given title was not found."));
  }

  @Override
  public BusinessType findByCodeKved(final String codeKved) {
    return businessTypeRepo.findByCodeKved(codeKved);
  }

  @Override
  public List<BusinessType> findAll() {
    return businessTypeRepo.findAll();
  }
}
