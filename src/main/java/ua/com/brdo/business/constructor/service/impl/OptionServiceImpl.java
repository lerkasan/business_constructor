package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.service.OptionService;

@Service("OptionService")
public class OptionServiceImpl implements OptionService {

    private OptionRepository optionRepo;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepo) {
        this.optionRepo = optionRepo;
    }

    @Transactional
    @Override
    public Option create(final Option option) {
        Objects.requireNonNull(option);
        return optionRepo.saveAndFlush(option);
    }

    @Transactional
    @Override
    public Option update(final Option option) {
        Objects.requireNonNull(option);
        return optionRepo.saveAndFlush(option);
    }

    @Transactional
    @Override
    public void delete(final Long id) {
        optionRepo.delete(id);
    }

    @Override
    public Option findById(final Long id) {
        return optionRepo.findOne(id);
    }

    @Override
    public Option findByTitle(final String title) {
        return optionRepo.findByTitle(title).orElseThrow(() -> new NotFoundException("Option with given title was not found."));
    }

    @Override
    public List<Option> findAll() {
        return optionRepo.findAll();
    }
}