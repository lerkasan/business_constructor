package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import ua.com.brdo.business.constructor.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByText(String text);
}