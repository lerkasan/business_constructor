package ua.com.brdo.business.constructor.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.model.Questionnaire;
import ua.com.brdo.business.constructor.repository.QuestionnaireRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class QuestionnaireServiceTest {

        private Questionnaire dummyQuestionnaire;

        @Mock
        private QuestionnaireRepository questionnaireRepo;

        @InjectMocks
        private QuestionnaireServiceImpl serviceUnderTest = new QuestionnaireServiceImpl(questionnaireRepo);

        private static final String NOT_EXISTENT_TITLE = "No questionnaire";

        private static final String TITLE = "My questionnaire";

        @Before
        public void setUp() {
            dummyQuestionnaire = new Questionnaire();
            dummyQuestionnaire.setId(1L);
            dummyQuestionnaire.setTitle("dummy Questionnaire");
            dummyQuestionnaire.setBusinessType(new BusinessType());

            when(questionnaireRepo.findOne(1L)).thenReturn(dummyQuestionnaire);
            when(questionnaireRepo.findOne(2L)).thenReturn(null);
        }

        @Test
        public void shouldCreateQuestionnaireTest() {
            serviceUnderTest.create(dummyQuestionnaire);

            verify(questionnaireRepo, times(1)).saveAndFlush(dummyQuestionnaire);
        }

        @Test(expected = NullPointerException.class)
        public void shouldThrowNullPointerExceptionOnAttemptToCreateNullTest() {
            serviceUnderTest.create(null);
        }

        @Test
        public void shouldUpdateQuestionnaireTest() {
            serviceUnderTest.update(dummyQuestionnaire);

            verify(questionnaireRepo, times(1)).saveAndFlush(dummyQuestionnaire);
        }

        @Test(expected = NullPointerException.class)
        public void shouldThrowNullPointerExceptionOnAttemptToUpdateNullTest() {
            serviceUnderTest.update(null);
        }

        @Test
        public void shouldDeleteQuestionnaireTest() {
            serviceUnderTest.delete(dummyQuestionnaire);

            verify(questionnaireRepo, times(1)).delete(dummyQuestionnaire);
        }

        @Test
        public void shouldDeleteQuestionnaireByIdTest() {
            serviceUnderTest.delete(1L);

            verify(questionnaireRepo, times(1)).delete(1L);
        }

        @Test
        public void shouldFindAllQuestionnairesTest() {
            serviceUnderTest.findAll();

            verify(questionnaireRepo, times(1)).findAll();
        }

        @Test(expected = NotFoundException.class)
        public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentIdTest() {
            serviceUnderTest.findById(2L);
        }

        @Test
        public void shouldFindExistentIdTest() {
            Questionnaire foundQuestionnaire = serviceUnderTest.findById(1L);

            verify(questionnaireRepo, times(1)).findOne(1L);
            assertEquals(dummyQuestionnaire, foundQuestionnaire);
        }

        @Test(expected = NotFoundException.class)
        public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentTitleTest() {
            when(questionnaireRepo.findByTitle(NOT_EXISTENT_TITLE)).thenReturn(Optional.empty());
            serviceUnderTest.findByTitle(NOT_EXISTENT_TITLE);
        }

        @Test
        public void shouldFindByTitleTest() {
            when(questionnaireRepo.findByTitle(TITLE)).thenReturn(Optional.of(dummyQuestionnaire));
            Questionnaire foundQuestionnaire = serviceUnderTest.findByTitle(TITLE);

            verify(questionnaireRepo, times(1)).findByTitle(TITLE);
            assertEquals(dummyQuestionnaire, foundQuestionnaire);
        }

        @Test
        public void shouldReturnTrueIfTitleAvailableTest() {
            when(questionnaireRepo.titleAvailable(TITLE)).thenReturn(true);
            boolean result = serviceUnderTest.isAvailable("title", TITLE);

            verify(questionnaireRepo, times(1)).titleAvailable(TITLE);
            assertTrue(result);
        }

        @Test
        public void shouldReturnFalseIfTitleUnavailableTest() {
            when(questionnaireRepo.titleAvailable(TITLE)).thenReturn(false);
            boolean result = serviceUnderTest.isAvailable("title", TITLE);

            verify(questionnaireRepo, times(1)).titleAvailable(TITLE);
            assertFalse(result);
        }

        @Test(expected = IllegalArgumentException.class)
        public void shouldThrowIllegalArgumentExceptionOnWrongFieldTest() {
            String wrongField = "text";
            serviceUnderTest.isAvailable(wrongField, TITLE);
        }

        @Test
        public void shouldReturnFalseOnNullTitleFieldTest() {
            boolean result = serviceUnderTest.isAvailable("title", null);
            assertFalse(result);
        }
}
