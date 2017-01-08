package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.ProcedureType;

import java.util.Optional;

@Repository
public interface ProcedureTypeRepository extends JpaRepository<ProcedureType, Long> {

    Optional<ProcedureType> findByName(String name);

   }
