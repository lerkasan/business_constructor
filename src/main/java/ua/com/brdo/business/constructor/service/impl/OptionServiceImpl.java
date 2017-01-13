package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.service.OptionService;

@Service("OptionService")
public class OptionServiceImpl implements OptionService {

    private static final String OPTION_NOT_FOUND = "Option was not found.";

    private static final String NOT_FOUND = "Question or option was not found.";

    private OptionRepository optionRepo;

    @Autowired
    public OptionServiceImpl(OptionRepository optionRepo) {
        this.optionRepo = optionRepo;
    }

    @Override
    @Transactional
    public Option create(final Option option) {
        Objects.requireNonNull(option);
        return optionRepo.saveAndFlush(option);
    }

    @Override
    @Transactional
    public Option update(final Option option) {
        Long id = option.getId();
        Objects.requireNonNull(option);
        if (optionRepo.findOne(id) == null) {
            throw new NotFoundException(OPTION_NOT_FOUND);
        }
        return optionRepo.saveAndFlush(option);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        if (optionRepo.findOne(id) == null) {
            throw new NotFoundException(OPTION_NOT_FOUND);
        }
        optionRepo.delete(id);
    }

    @Override
    @Transactional
    public void delete(final Option option) {
        Objects.requireNonNull(option);
        optionRepo.delete(option);
    }

    @Override
    public Option findById(final long id) {
        Option option = optionRepo.findOne(id);
        if (option == null) {
            throw new NotFoundException(OPTION_NOT_FOUND);
        }
        return option;
    }

    @Override
    public Option findByTitle(final String title) {
        return optionRepo.findByTitle(title).orElseThrow(() -> new NotFoundException(OPTION_NOT_FOUND));
    }

    @Override
    public List<Option> findByQuestionId(final long id) {
        return optionRepo.findByQuestionId(id);
    }

    @Override
    public Option findByQuestionIdAndOptionId(final long questionId, final long optionId) {
        return optionRepo.findByIdAndQuestionId(optionId, questionId).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    @Transactional
    public Long deleteByQuestionId(final long id) {
        return optionRepo.deleteByQuestionId(id);
    }

    @Override
    public List<Option> findAll() {
        return optionRepo.findAll();
    }
}
