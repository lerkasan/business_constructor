package ua.com.brdo.business.constructor.service;

import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.model.BusinessType;

@Validated
public interface BusinessTypeService {

    BusinessType create(final BusinessType businessType);

    BusinessType update(@Valid final BusinessType businessType);

    void delete(final long id);

    void delete(final BusinessType businessType);

    BusinessType findById(final long id);

    BusinessType findByTitle(final String title);

    BusinessType findByCodeKved(final String codeKved);

    List<BusinessType> findAll();
}
