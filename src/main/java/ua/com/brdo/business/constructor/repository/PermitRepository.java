package ua.com.brdo.business.constructor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.Permit;

@Repository
public interface PermitRepository extends JpaRepository<Permit, Long> {

    Optional<Permit> findByName(String name);

    @Query("SELECT count(p) = 0 FROM Permit p WHERE LOWER(p.name) = LOWER(:name) AND p.id != :id")
    boolean nameAvailable(@Param("name") String name, @Param("id") Long id );
}
