package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.BusinessType;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {

    Optional<BusinessType> findByTitle(final String title);

    BusinessType findByCodeKved(final String codeKved);

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE LOWER(b.title) = LOWER(?)")
    boolean titleAvailable(String title);

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE b.codeKved = ?")
    boolean codeKvedAvailable(String codeKved);
}
