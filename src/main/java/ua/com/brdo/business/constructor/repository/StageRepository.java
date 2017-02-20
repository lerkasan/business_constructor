package ua.com.brdo.business.constructor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Stage;

@Repository
public interface StageRepository extends JpaRepository<Stage, Long> {

    @Query("SELECT s FROM Stage s INNER JOIN s.answer a WHERE a.business = :business")
    List<Stage> findByBusiness(@Param("business") final Business business);

    @Query("SELECT s FROM Stage s INNER JOIN s.answer a WHERE a.business = :business AND s.id = :stageId")
    Optional<Stage> findByIdAndBusiness(@Param("stageId") final Long stageId, @Param("business") final Business business);
}
