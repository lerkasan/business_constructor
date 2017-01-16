package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.service.impl.OptionServiceImpl;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class OptionServiceTest {

    private Option dummyOption;

    @Mock
    private OptionRepository optionRepo;

    @InjectMocks
    private OptionService serviceUnderTest = new OptionServiceImpl(optionRepo);

    @Before
    @SneakyThrows
    public void setUp() {
        dummyOption = new Option();
        dummyOption.setId(1L);
        dummyOption.setTitle("dummy option");
        dummyOption.setQuestion(new Question());

        when(optionRepo.findByIdAndQuestionId(1L,1L)).thenReturn(Optional.of(dummyOption));
        when(optionRepo.findByIdAndQuestionId(2L,2L)).thenReturn(Optional.empty());
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

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonexistentTest() {
        serviceUnderTest.findByQuestionIdAndOptionId(2L, 2L);
    }

    @Test
    @SneakyThrows
    public void shouldReturnExistentOptionOnSearchTest() {
        serviceUnderTest.findByQuestionIdAndOptionId(1L, 1L);
    }
}