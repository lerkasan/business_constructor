package ua.com.brdo.business.constructor.service.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.QuestionOption;
import ua.com.brdo.business.constructor.repository.QuestionOptionRepository;
import ua.com.brdo.business.constructor.service.QuestionOptionService;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Transactional
public class QuestionOptionServiceTest {

    private QuestionOption dummyQuestionOption;

    @Mock
    private QuestionOptionRepository questionOptionRepo;

    @InjectMocks
    private QuestionOptionService serviceUnderTest = new QuestionOptionServiceImpl(questionOptionRepo);

    @Before
    @SneakyThrows
    public void setUp() {
//        when(questionOptionRepo.findByQuestionIdAndOptionId(1L,1L)).thenReturn(Optional.of(dummyQuestionOption));
//        when(questionOptionRepo.findByQuestionIdAndOptionId(2L,2L)).thenReturn(Optional.empty());
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToCreateNullTest() {
        serviceUnderTest.create(null);
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToUpdateNullTest() {
        serviceUnderTest.update(null);
    }

    @Ignore
    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonexistentTest() {
        serviceUnderTest.findByQuestionIdAndOptionId(2L, 2L);
    }

    @Ignore
    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToDeleteNonexistentTest() {
        serviceUnderTest.deleteByQuestionIdAndOptionId(2L, 2L);
    }
}