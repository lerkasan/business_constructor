package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Flow;
import ua.com.brdo.business.constructor.repository.FlowRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

@Service
public class FlowService {

    private FlowRepository flowRepo;

    private static final String NOT_FOUND = "Specified flow stage was not found.";

    @Autowired
    public FlowService(FlowRepository flowRepo) {
        this.flowRepo = flowRepo;
    }

    @Transactional
    public Flow create(final Flow flow) {
        Objects.requireNonNull(flow);
        return flowRepo.saveAndFlush(flow);
    }

    @Transactional
    public Flow update(final Flow flow) {
        Objects.requireNonNull(flow);
        return flowRepo.saveAndFlush(flow);
    }

    @Transactional
    public void delete(final long id) {
        flowRepo.delete(id);
    }

    @Transactional
    public void delete(final Flow flow) {
        flowRepo.delete(flow);
    }

    public Flow findById(final long id) {
        Flow flow = flowRepo.findOne(id);
        if (flow == null) {
            throw new NotFoundException("Specified flow was not found.");
        }
        return flow;
    }

    public List<Flow> findAll() {
        return flowRepo.findAll();
    }

    public List<Flow> findByBusiness(final Business business) {
        return flowRepo.findByBusinessOrderByPriorityAsc(business);
    }

    public Flow findByIdAndBusinessId(final Long flowId, final Long businessId) {
        return flowRepo.findByIdAndBusinessId(flowId, businessId).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }
}
