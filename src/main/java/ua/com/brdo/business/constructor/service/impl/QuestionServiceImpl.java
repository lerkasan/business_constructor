package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.repository.QuestionnaireRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.service.ProcedureService;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private static final String NOT_FOUND = "Question was not found.";
    private static final String QUESTIONNAIRE_NOT_FOUND = "Questionnaire was not found.";
    private static final String QUESTIONNAIRE_REQUIRED = "Questionnaire id is required.";

    private QuestionRepository questionRepo;
    private ProcedureService procedureService;
    private QuestionnaireRepository questionnaireRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, QuestionnaireRepository questionnaireRepo, ProcedureService procedureService) {
        this.questionRepo = questionRepo;
        this.questionnaireRepo = questionnaireRepo;
        this.procedureService = procedureService;
    }

    private Question preprocess(Question question) {
        if (question.getId() == null) {
            isQuestionUniqueInQuestionnaire(question);
        }
        return addOptions(question);
    }

    private boolean isQuestionUniqueInQuestionnaire(Question question) {
        Objects.requireNonNull(question);
        Questionnaire questionnaire = question.getQuestionnaire();
        Objects.requireNonNull(questionnaire, QUESTIONNAIRE_REQUIRED);
        Long questionnaireId = questionnaire.getId();
        Objects.requireNonNull(questionnaireId, QUESTIONNAIRE_REQUIRED);
        Questionnaire persistedQuestionnaire = questionnaireRepo.findOne(questionnaireId);
        if (persistedQuestionnaire == null) {
            throw new NotFoundException(QUESTIONNAIRE_NOT_FOUND);
        }
        Set<Question> questions = persistedQuestionnaire.getQuestions();
        if (questions.contains(question)) {
            throw new IllegalArgumentException("Question with the same text already exists in this questionnaire.");
        }
        return true;
    }

    private Question addOptions(Question question) {
        Set<Option> options = question.getOptions();
        if (options != null && !options.isEmpty()) {
            options.forEach(option -> {
                option.setQuestion(question);
                Question nextQuestion = option.getNextQuestion();
                Procedure procedure = option.getProcedure();
                if (nextQuestion != null) {
                    Question persistedNextQuestion = findById(nextQuestion.getId());
                    option.checkLinkBetweenQuestionAndNextQuestion(persistedNextQuestion);
                }
                if (procedure != null) {
                    Long id = procedure.getId();
                    procedureService.findById(id);
                }
            });
        }
        return question;
    }

    @Override
    @Transactional
    public Question create(final Question question) {
        Objects.requireNonNull(question);
        Question processedQuestion = preprocess(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    public Question update(final Question question) {
        Objects.requireNonNull(question);
        Question processedQuestion = preprocess(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        questionRepo.delete(id);
    }

    @Override
    @Transactional
    public void delete(Question question) {
        questionRepo.delete(question);
    }

    @Override
    public Question findById(final long id) {
        Question question = questionRepo.findOne(id);
        if (question == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        return question;
    }

    @Override
    public Question findByText(final String text) {
        return questionRepo.findByText(text).orElseThrow(() -> new NotFoundException(NOT_FOUND));
    }

    @Override
    public List<Question> findByQuestionnaire(Questionnaire questionnaire) {
        return questionRepo.findByQuestionnaire(questionnaire);
    }

    @Override
    public List<Question> findAll() {
        return questionRepo.findAll();
    }

    @Override
    public Question addOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        option.setQuestion(question);
        question.addOption(option);
        return question;
    }

    @Override
    public Question deleteOption(Question question, Option option) {
        Objects.requireNonNull(question);
        Objects.requireNonNull(option);
        question.deleteOption(option);
        return question;
    }

    @Override
    public void deleteOptions(Question question) {
        Objects.requireNonNull(question);
        Set<Option> options = question.getOptions();
        if (options != null) {
            options.clear();
        }
    }
}
