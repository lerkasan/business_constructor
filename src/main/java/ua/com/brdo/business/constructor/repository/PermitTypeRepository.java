package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.PermitType;

@Repository
public interface PermitTypeRepository extends JpaRepository<PermitType, Long> {
    Optional<PermitType> findByName(String name);
}
