package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.QuestionOption;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestionId(long id);

    List<QuestionOption> findByOptionId(long id);

    Optional<QuestionOption> findByQuestionIdAndOptionId(long questionId, long optionId);
}
