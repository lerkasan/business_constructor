package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.Procedure;


import java.util.Optional;


@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long>{
    Optional<Procedure> findByName(String name);


}
