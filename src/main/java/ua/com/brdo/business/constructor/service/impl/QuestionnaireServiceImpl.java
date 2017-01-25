package ua.com.brdo.business.constructor.service.impl;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.constraint.UniqueValidatable;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.QuestionnaireRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.service.QuestionnaireService;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService, UniqueValidatable {

  private static final String NOT_FOUND = "Questionnaire was not found.";

  private QuestionnaireRepository questionnaireRepo;

  @Autowired
  public QuestionnaireServiceImpl(final QuestionnaireRepository questionnaireRepo) {
    this.questionnaireRepo = questionnaireRepo;
  }

  @Override
  @Transactional
  public Questionnaire create(final Questionnaire questionnaire) {
    Objects.requireNonNull(questionnaire);
    return questionnaireRepo.saveAndFlush(questionnaire);
  }

  @Override
  @Transactional
  public Questionnaire update(final Questionnaire questionnaire) {
    Objects.requireNonNull(questionnaire);
    return questionnaireRepo.saveAndFlush(questionnaire);
  }

  @Override
  @Transactional
  public void delete(final long id) {
    questionnaireRepo.delete(id);
  }

  @Override
  @Transactional
  public void delete(final Questionnaire questionnaire) {
    questionnaireRepo.delete(questionnaire);
  }

  @Override
  public Questionnaire findById(final long id) {
    Questionnaire questionnaire = questionnaireRepo.findOne(id);
    if (questionnaire == null) {
      throw new NotFoundException(NOT_FOUND);
    }
    return questionnaire;
  }

  @Override
  public Questionnaire findByTitle(final String title) {
    return questionnaireRepo.findByTitle(title).orElseThrow(() -> new NotFoundException(NOT_FOUND));
  }

  @Override
  public List<Questionnaire> findAll() {
    return questionnaireRepo.findAll();
  }

  @Override
  public List<Questionnaire> findByBusinessType(final BusinessType businessType) {
    return questionnaireRepo.findByBusinessType(businessType);
  }

  public boolean isAvailable(String fieldName, String fieldValue, Long id) {
    if ("title".equals(fieldName)) {
      return nonNull(fieldValue) && questionnaireRepo.titleAvailable(fieldValue, id);
    } else {
      throw new IllegalArgumentException("Unexpected field was passed to isAvailable method.");
    }
  }
}
