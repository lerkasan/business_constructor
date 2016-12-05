package ua.com.brdo.business.constructor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.service.OptionService;

@Service("OptionService")
public class OptionServiceImpl implements OptionService {

    private static final String ROLE_EXPERT = "ROLE_EXPERT";

    private static final String NOT_FOUND = "Option was not found.";

    private OptionRepository optionRepo;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepo) {
        this.optionRepo = optionRepo;
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public Option create(final Option option) {
        Objects.requireNonNull(option);
        Optional<Option> optionFromDb = optionRepo.findByTitle(option.getTitle());
        if (optionFromDb.isPresent()) {
            return optionFromDb.get();
        }
        return optionRepo.saveAndFlush(option);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public Option update(final Option option) {
        Long id = option.getId();
        Objects.requireNonNull(option);
        if (optionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return optionRepo.saveAndFlush(option);
    }

    @Override
    @Transactional
    @Secured(ROLE_EXPERT)
    public void delete(final long id) {
        if (optionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        optionRepo.delete(id);
    }

    @Override
    public Option findById(final long id) {
        Option option = optionRepo.findOne(id);
        if (option == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return option;
    }

    @Override
    public Option findByTitle(final String title) {
        return optionRepo.findByTitle(title).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<Option> findAll() {
        return optionRepo.findAll();
    }
}