package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.QuestionnaireRepository;
import ua.com.brdo.business.constructor.service.QuestionnaireService;

@Service("QuestionnaireService")
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private static final String NOT_FOUND = "Questionnaire was not found.";

  private QuestionnaireRepository questionnaireRepo;

  @Autowired
  public QuestionnaireServiceImpl(final QuestionnaireRepository questionnaireRepo) {
    this.questionnaireRepo = questionnaireRepo;
  }

  private Questionnaire addQuestions(final Questionnaire questionnaire) {
    Set<Question> questions = questionnaire.getQuestions();
    if (questions != null && !questions.isEmpty()) {
      questions.forEach(question -> {
        if (questions.contains(question)) {
          throw new IllegalArgumentException("Question with the same text already exists in this questionnaire.");
        }
        question.setQuestionnaire(questionnaire);
      });
    }
    return questionnaire;
  }

  private Questionnaire preprocess(final Questionnaire questionnaire) {
    return addQuestions(questionnaire);
  }

  @Override
  @Transactional
  public Questionnaire create(final Questionnaire questionnaire) {
    Objects.requireNonNull(questionnaire);
    Questionnaire processedQuestionnaire = preprocess(questionnaire);
    return questionnaireRepo.saveAndFlush(processedQuestionnaire);
  }

  @Override
  @Transactional
  public Questionnaire update(final Questionnaire questionnaire) {
    Objects.requireNonNull(questionnaire);
    Long id = questionnaire.getId();
    if (questionnaireRepo.findOne(id) == null) {
      throw new NotFoundException(NOT_FOUND);
    }
    Questionnaire processedQuestionnaire = preprocess(questionnaire);
    return questionnaireRepo.saveAndFlush(processedQuestionnaire);
  }

  @Override
  @Transactional
  public void delete(final long id) {
    if (questionnaireRepo.findOne(id) == null) {
      throw new NotFoundException(NOT_FOUND);
    }
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

  @Override
  public Questionnaire addQuestion(final Questionnaire questionnaire, final Question question) {
    Objects.requireNonNull(question);
    Objects.requireNonNull(questionnaire);
    question.setQuestionnaire(questionnaire);
    questionnaire.addQuestion(question);
    return questionnaire;
  }

  @Override
  public Questionnaire deleteQuestion(final Questionnaire questionnaire, final Question question) {
    Objects.requireNonNull(question);
    Objects.requireNonNull(questionnaire);
    questionnaire.deleteQuestion(question);
    return questionnaire;
  }

  @Override
  public void deleteQuestions(final Questionnaire questionnaire) {
    Objects.requireNonNull(questionnaire);
    Set<Question> questions = questionnaire.getQuestions();
    if (questions != null) {
      questions.clear();
    }
  }
}
