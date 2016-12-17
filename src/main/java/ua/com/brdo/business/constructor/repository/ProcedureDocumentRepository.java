package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.ProcedureDocument;


import java.util.Optional;

@Repository
public interface ProcedureDocumentRepository extends JpaRepository<ProcedureDocument, Long>{

    Optional<ProcedureDocument> findByName(String name);

}
