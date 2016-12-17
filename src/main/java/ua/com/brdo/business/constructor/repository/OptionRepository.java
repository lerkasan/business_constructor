package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    Optional<Option> findByTitle(String title);

    List<Option> findByQuestionId(long id);

    Optional<Option> findByIdAndQuestionId(long id, long questionId);

    Long deleteByQuestionId(Long questionId);
}