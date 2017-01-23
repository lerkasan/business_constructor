package ua.com.brdo.business.constructor.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.constraint.UniqueValidatable;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.repository.BusinessTypeRepository;
import ua.com.brdo.business.constructor.service.BusinessTypeService;
import ua.com.brdo.business.constructor.service.NotFoundException;

@Service
@Validated
public class BusinessTypeServiceImpl implements BusinessTypeService, UniqueValidatable {

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
//    @Unique.List(value = {
//        @Unique(field = "title", message = "Business type with specified title already exists in database. Title should be unique."),
//        @Unique(field = "codeKved", message = "Business type with specified KVED code already exists in database. KVED code should be unique.")
//    })
    public BusinessType update(
//        @Unique.List(value = {
//        @Unique(field = "title", message = "Business type with specified title already exists in database. Title should be unique."),
//        @Unique(field = "codeKved", message = "Business type with specified KVED code already exists in database. KVED code should be unique.")
//    })
        @Valid final BusinessType businessType) {
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
        BusinessType businessType = businessTypeRepo.findOne(id);
        if (businessType == null) {
            throw new NotFoundException("Specified business type was not found.");
        }
        return businessType;
    }

    @Override
    public BusinessType findByTitle(final String title) {
        return businessTypeRepo.findByTitle(title)
            .orElseThrow(
                () -> new NotFoundException("Business type with given title was not found."));
    }

    @Override
    public BusinessType findByCodeKved(final String codeKved) {
        return businessTypeRepo.findByCodeKved(codeKved)
            .orElseThrow(
                () -> new NotFoundException("Business type with given code kved was not found."));
    }

    @Override
    public List<BusinessType> findAll() {
        return businessTypeRepo.findAll();
    }

    public boolean isAvailable(String field, String value, Long id) {
        if (isNull(value)) {
            return false;
        }
        switch (field) {
            case "title":
                return nonNull(id) ? businessTypeRepo.titleAvailable(value, id) : businessTypeRepo.titleAvailable(value);
            case "codeKved":
                return nonNull(id) ? businessTypeRepo.codeKvedAvailable(value, id) : businessTypeRepo.codeKvedAvailable(value);
            default:
                throw new IllegalArgumentException(
                    "Unexpected field was passed to isAvailable method.");
        }
    }
}
