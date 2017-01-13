package ua.com.brdo.business.constructor.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.BusinessType;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {

  Optional<BusinessType> findByTitle(final String title);

  BusinessType findByCodeKved(final String codeKved);
}
