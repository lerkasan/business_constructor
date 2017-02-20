package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.Answer;
import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Question;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByBusiness(final Business business);

    Optional<Answer> findByIdAndBusinessId(final Long answerId, final Long businessId);

    void deleteByIdAndBusinessId(final Long answerId, final Long businessId);

    @Query("SELECT count(a) > 0 FROM Answer a INNER JOIN a.option opt WHERE opt.question = :question AND a.business = :business")
    boolean isAnyAnswer (@Param("question") final Question question, @Param("business") final Business business);
}
