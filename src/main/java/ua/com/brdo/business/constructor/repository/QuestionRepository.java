package ua.com.brdo.business.constructor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByText(String text);

    List<Question> findByQuestionnaire(Questionnaire questionnaire);
}
