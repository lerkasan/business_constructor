package ua.com.brdo.business.constructor.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.BusinessType;

@Repository
public interface BusinessTypeRepository extends JpaRepository<BusinessType, Long> {

    Optional<BusinessType> findByTitle(final String title);

    Optional<BusinessType> findByCodeKved(final String codeKved);

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE LOWER(b.title) = LOWER(?)")
    boolean titleAvailable(String title);

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE LOWER(b.title) = LOWER(:title) AND b.id <> :id")
    boolean titleAvailable(@Param("title") String title, @Param("id") Long id );

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE b.codeKved = ?")
    boolean codeKvedAvailable(String codeKved);

    @Query("SELECT count(b) = 0 FROM BusinessType b WHERE b.codeKved = :codeKved AND b.id <> :id")
    boolean codeKvedAvailable(@Param("codeKved") String codeKved, @Param("id") Long id);
}
