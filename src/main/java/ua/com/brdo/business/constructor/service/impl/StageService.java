package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.Business;
import ua.com.brdo.business.constructor.model.Stage;
import ua.com.brdo.business.constructor.repository.StageRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

@Service
public class StageService {

    private StageRepository stageRepo;

    private static final String NOT_FOUND = "Specified flow stage was not found.";

    @Autowired
    public StageService(StageRepository stageRepo) {
        this.stageRepo = stageRepo;
    }

    @Transactional
    public Stage create(final Stage stage) {
        Objects.requireNonNull(stage);
        return stageRepo.saveAndFlush(stage);
    }

    @Transactional
    public Stage update(final Stage stage) {
        Objects.requireNonNull(stage);
        return stageRepo.saveAndFlush(stage);
    }

    @Transactional
    public void delete(final long id) {
        stageRepo.delete(id);
    }

    @Transactional
    public void delete(final Stage stage) {
        stageRepo.delete(stage);
    }

    public Stage findById(final long id) {
        Stage stage = stageRepo.findOne(id);
        if (stage == null) {
            throw new NotFoundException("Specified stage was not found.");
        }
        return stage;
    }

    public List<Stage> findAll() {
        return stageRepo.findAll();
    }

    public List<Stage> findByBusiness(final Business business) {
        return stageRepo.findByBusiness(business);
    }

    public Stage findByIdAndBusiness(final Long stageId, final Business business) {
        return stageRepo.findByIdAndBusiness(stageId, business).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }
}
