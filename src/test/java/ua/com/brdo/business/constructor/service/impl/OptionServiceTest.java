package ua.com.brdo.business.constructor.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.SneakyThrows;
import ua.com.brdo.business.constructor.exception.NotFoundException;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.repository.OptionRepository;
import ua.com.brdo.business.constructor.service.OptionService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@Transactional
public class OptionServiceTest {

    private static final String optionTitle = "My option";

    private static final String newOptionTitle = "New option";

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
        dummyOption.setTitle(optionTitle);

        when(optionRepo.findByTitle(optionTitle)).thenReturn(Optional.of(dummyOption));
        when(optionRepo.findByTitle(newOptionTitle)).thenReturn(Optional.empty());
        when(optionRepo.findOne(1L)).thenReturn(dummyOption);
        when(optionRepo.findOne(2L)).thenReturn(null);
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToCreateNullTest() {
        serviceUnderTest.create(null);
    }

    @Test
    @SneakyThrows
    public void shouldNotCreateExistentOptionTest() {
        Option newOption = new Option();
        newOption.setTitle(optionTitle);
        Option createdOption = serviceUnderTest.create(newOption);

        verify(optionRepo, times(0)).saveAndFlush(newOption);
        assertEquals(dummyOption, createdOption);
    }

    @Test
    @SneakyThrows
    public void shouldCreateNonExistentOptionTest() {
        Option newOption = new Option();
        newOption.setTitle(newOptionTitle);
        serviceUnderTest.create(newOption);

        verify(optionRepo, times(1)).saveAndFlush(newOption);
    }

    @Test(expected = NullPointerException.class)
    @SneakyThrows
    public void shouldThrowNullPointerExceptionOnAttemptToUpdateNullTest() {
        serviceUnderTest.update(null);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToUpdateNonExistentIdTest() {
        Option option = new Option();
        option.setId(2L);
        option.setTitle(newOptionTitle);
        serviceUnderTest.update(option);
    }

    @Test
    @SneakyThrows
    public void shouldUpdateExistentTest() {
        dummyOption.setTitle(newOptionTitle);
        serviceUnderTest.update(dummyOption);

        verify(optionRepo, times(1)).saveAndFlush(dummyOption);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToDeleteNonExistentIdTest() {
        serviceUnderTest.delete(2L);
    }

    @Test
    @SneakyThrows
    public void shouldDeleteExistentIdTest() {
        serviceUnderTest.delete(1L);

        verify(optionRepo, times(1)).delete(1L);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentIdTest() {
        serviceUnderTest.findById(2L);
    }

    @Test
    @SneakyThrows
    public void shouldFindExistentIdTest() {
        Option foundOption = serviceUnderTest.findById(1L);

        verify(optionRepo, times(1)).findOne(1L);
        assertEquals(dummyOption, foundOption);
    }

    @Test(expected = NotFoundException.class)
    @SneakyThrows
    public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentTitleTest() {
        serviceUnderTest.findByTitle(newOptionTitle);
    }

    @Test
    @SneakyThrows
    public void shouldFindExistentTitleTest() {
        Option foundOption = serviceUnderTest.findByTitle(optionTitle);

        verify(optionRepo, times(1)).findByTitle(optionTitle);
        assertEquals(dummyOption, foundOption);
    }
}