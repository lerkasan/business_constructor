package ua.com.brdo.business.constructor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Questionnaire;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Optional<Questionnaire> findByTitle(final String title);

    List<Questionnaire> findByBusinessType(final BusinessType businessType);

    @Query("SELECT count(q) = 0 FROM Questionnaire q WHERE LOWER(q.title) = LOWER(:title) AND q.id != :id")
    boolean titleAvailable(@Param("title") String title, @Param("id") Long id);
}
