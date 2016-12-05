package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.LegalDocument;

@Repository
public interface LegalDocumentRepository extends JpaRepository<LegalDocument, Long>{


}
