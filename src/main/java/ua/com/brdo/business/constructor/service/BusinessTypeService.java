package ua.com.brdo.business.constructor.service;

import java.util.List;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ua.com.brdo.business.constructor.constraint.Unique;
import ua.com.brdo.business.constructor.model.BusinessType;

@Validated
public interface BusinessTypeService {

    BusinessType create(final BusinessType businessType);

    BusinessType update(
//        @Unique.List(value = {
//        @Unique(field = "title", message = "Business type with specified title already exists in database. Title should be unique."),
//        @Unique(field = "codeKved", message = "Business type with specified KVED code already exists in database. KVED code should be unique.")
//    })
    @Valid final BusinessType businessType);

    void delete(final long id);

    void delete(final BusinessType businessType);

    BusinessType findById(final long id);

    BusinessType findByTitle(final String title);

    BusinessType findByCodeKved(final String codeKved);

    List<BusinessType> findAll();
}
