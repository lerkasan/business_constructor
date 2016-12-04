package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.QuestionOption;

@Repository
//@NamedQuery(name = "QuestionOptionRepository.removeByQuestionId", query = "DELETE FROM question_option q WHERE q.question_id = :questionId")
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    List<QuestionOption> findByQuestionId(long id);

    List<QuestionOption> findByOptionId(long id);

    Optional<QuestionOption> findByQuestionIdAndOptionId(long questionId, long optionId);

    @Transactional
    Long deleteByQuestionId(Long questionId);

    @Transactional
    Long deleteByQuestionIdAndOptionId(Long questionId, Long OptionId);
}
