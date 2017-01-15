package ua.com.brdo.business.constructor.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.ProcedureRepository;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.service.QuestionService;

@Service("QuestionService")
public class QuestionServiceImpl implements QuestionService {

    private static final String NOT_FOUND = "Question was not found.";
    private static final String NEXT_QUESTION_NOT_FOUND = "Specified next question was not found.";
    private static final String PROCEDURE_NOT_FOUND = "Specified procedure was not found.";

    private QuestionRepository questionRepo;
    private ProcedureRepository procedureRepo;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepo, ProcedureRepository procedureRepo) {
        this.questionRepo = questionRepo;
        this.procedureRepo = procedureRepo;
    }

    private Question preprocess(Question question) {
        return addOptions(question);
    }

    private Question addOptions(Question question) {
        Set<Option> options = question.getOptions();
        if (options != null && !options.isEmpty()) {
            options.forEach(option -> {
                option.setQuestion(question);
                Question nextQuestion = option.getNextQuestion();
                Procedure procedure = option.getProcedure();
                checkIfNextQuestionExistsInDB(nextQuestion);
                option.checkLinkBetweenQuestionAndNextQuestion(nextQuestion);
                checkIfProcedureExistsInDB(procedure);
            });
        }
        return question;
    }

    private void checkIfProcedureExistsInDB(Procedure procedure) {
        if ((procedure != null) && (procedureRepo.findOne(procedure.getId()) == null)) {
            throw new NotFoundException(PROCEDURE_NOT_FOUND);
        }
    }

    private void checkIfNextQuestionExistsInDB(Question nextQuestion) {
        if ((nextQuestion != null) && (questionRepo.findOne(nextQuestion.getId()) == null)) {
            throw new NotFoundException(NEXT_QUESTION_NOT_FOUND);
        }
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
        Long id = question.getId();
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
        Question processedQuestion = preprocess(question);
        return questionRepo.saveAndFlush(processedQuestion);
    }

    @Override
    @Transactional
    public void delete(final long id) {
        if (questionRepo.findOne(id) == null) {
            throw new NotFoundException(NOT_FOUND);
        }
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
