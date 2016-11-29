package ua.com.brdo.business.constructor.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.Permit;

@Repository
public interface PermitRepository extends JpaRepository<Permit, Long> {
    Optional<Permit> findByName(String name);
}
