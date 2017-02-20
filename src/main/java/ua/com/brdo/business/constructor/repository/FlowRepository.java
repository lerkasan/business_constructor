package ua.com.brdo.business.constructor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Flow;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Long> {

    List<Flow> findByBusinessOrderByPriorityAsc(final Business business);

    Optional<Flow> findByIdAndBusinessId(final Long flowId, final Long businessId);
}
