package ua.com.brdo.business.constructor.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.QuestionRepository;
import ua.com.brdo.business.constructor.repository.QuestionnaireRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.service.ProcedureService;
import ua.com.brdo.business.constructor.service.QuestionService;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class QuestionServiceTest {

    private static final String questionText = "That is this question about?";

    private static final String newQuestionText = "That is this new question about?";

    private BusinessType dummyBusinessType;

    private Questionnaire dummyQuestionnaire;

    private Question dummyQuestion;

    private Option dummyOption;

    @Mock
    private QuestionRepository questionRepo;

    @Mock
    private QuestionnaireRepository questionnaireRepo;

    @Mock
    private ProcedureService procedureService;

    @InjectMocks
    private QuestionService serviceUnderTest = new QuestionServiceImpl(questionRepo, questionnaireRepo, procedureService);

    @Before
    public void setUp() {
        dummyBusinessType = new BusinessType();
        dummyBusinessType.setId(1L);
        dummyBusinessType.setTitle("Business title");
        dummyBusinessType.setCodeKved("12.34");

        dummyQuestionnaire = new Questionnaire();
        dummyQuestionnaire.setId(1L);
        dummyQuestionnaire.setTitle("Questionnaire title");
        dummyQuestionnaire.setBusinessType(dummyBusinessType);

        dummyQuestion = new Question();
        dummyQuestion.setId(1L);
        dummyQuestion.setText(questionText);
        dummyQuestion.setQuestionnaire(dummyQuestionnaire);

        dummyOption = new Option();
        dummyOption.setId(1L);
        dummyOption.setTitle("dummy option");
        dummyOption.setQuestion(dummyQuestion);

        when(questionRepo.findByText(questionText)).thenReturn(Optional.of(dummyQuestion));
        when(questionRepo.findByText(newQuestionText)).thenReturn(Optional.empty());
        when(questionRepo.findOne(1L)).thenReturn(dummyQuestion);
        when(questionRepo.findOne(2L)).thenReturn(null);
        when(questionRepo.saveAndFlush(any(Question.class))).thenAnswer(method -> method.getArgumentAt(0, Question.class));
        when(questionnaireRepo.findOne(1L)).thenReturn(dummyQuestionnaire);
    }

    @Test
    public void shouldAddOptiontoQuestionTest() {
        serviceUnderTest.addOption(dummyQuestion, dummyOption);
        Set<Option> actualOptions = dummyQuestion.getOptions();
        boolean questionContainsOption = actualOptions.contains(dummyOption);
        assertTrue(questionContainsOption);
    }

    @Test
    public void shouldRemoveOptionFromQuestionTest() {
        serviceUnderTest.deleteOption(dummyQuestion, dummyOption);
        Set<Option> actualOptions = dummyQuestion.getOptions();
        boolean questionContainsOption = actualOptions.contains(dummyOption);

        assertFalse(questionContainsOption);
    }
    
    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToCreateNullTest() {
        serviceUnderTest.create(null);
    }

    @Test
    @SneakyThrows
    public void shouldCreateQuestionTest() {
        Question newQuestion = new Question();
        newQuestion.setText(questionText);
        newQuestion.setQuestionnaire(dummyQuestionnaire);
        serviceUnderTest.create(newQuestion);

        verify(questionRepo, times(1)).saveAndFlush(newQuestion);
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToUpdateNullTest() {
        serviceUnderTest.update(null);
    }

    @Test
    @SneakyThrows
    public void shouldUpdateExistentQuestionTest() {
        dummyQuestion.setText(newQuestionText);
        serviceUnderTest.update(dummyQuestion);

        verify(questionRepo, times(1)).saveAndFlush(dummyQuestion);
    }

    @Test
    @SneakyThrows
    public void shouldDeleteExistentIdTest() {
        serviceUnderTest.delete(1L);

        verify(questionRepo, times(1)).delete(1L);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentIdTest() {
        serviceUnderTest.findById(2L);
    }

    @Test
    @SneakyThrows
    public void shouldFindExistentIdTest() {
        Question foundQuestion = serviceUnderTest.findById(1L);

        verify(questionRepo, times(1)).findOne(1L);
        assertEquals(dummyQuestion, foundQuestion);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentTitleTest() {
        serviceUnderTest.findByText(newQuestionText);
    }

    @Test
    @SneakyThrows
    public void shouldFindExistentTitleTest() {
        Question foundQuestion = serviceUnderTest.findByText(questionText);

        verify(questionRepo, times(1)).findByText(questionText);
        assertEquals(dummyQuestion, foundQuestion);
    }
}
