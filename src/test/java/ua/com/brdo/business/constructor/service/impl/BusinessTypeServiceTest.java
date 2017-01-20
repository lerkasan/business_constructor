package ua.com.brdo.business.constructor.service.impl;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.com.brdo.business.constructor.model.BusinessType;
import ua.com.brdo.business.constructor.repository.BusinessTypeRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class BusinessTypeServiceTest {

  private static final String TITLE = "My business";

  private static final String NEW_TITLE = "My new business title";

  private static final String NOT_EXISTENT_TITLE = "No business";

  private static final String CODE_KVED = "12.34";

  private BusinessType dummyBusinessType;

  @Mock
  private BusinessTypeRepository businessTypeRepo;

  @InjectMocks
  private BusinessTypeServiceImpl serviceUnderTest = new BusinessTypeServiceImpl(businessTypeRepo);

  @Before
  public void setUp() {
    dummyBusinessType = new BusinessType();
    dummyBusinessType.setId(1L);
    dummyBusinessType.setTitle(TITLE);
    dummyBusinessType.setCodeKved(CODE_KVED);

    when(businessTypeRepo.findOne(1L)).thenReturn(dummyBusinessType);
    when(businessTypeRepo.findOne(2L)).thenReturn(null);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionOnAttemptToCreateNullTest() {
    serviceUnderTest.create(null);
  }

  @Test
  public void shouldCreateBusinessTypeTest() {
    BusinessType newBusinessType = new BusinessType();
    newBusinessType.setTitle(TITLE);
    newBusinessType.setCodeKved(CODE_KVED);
    serviceUnderTest.create(newBusinessType);

    verify(businessTypeRepo, times(1)).saveAndFlush(newBusinessType);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNullPointerExceptionOnAttemptToUpdateNullTest() {
    serviceUnderTest.update(null);
  }

  @Test
  public void shouldUpdateExistentBusinessTypeTest() {
    dummyBusinessType.setTitle(NEW_TITLE);
    serviceUnderTest.update(dummyBusinessType);

    verify(businessTypeRepo, times(1)).saveAndFlush(dummyBusinessType);
  }

  @Test
  public void shouldDeleteExistentIdTest() {
    serviceUnderTest.delete(1L);

    verify(businessTypeRepo, times(1)).delete(1L);
  }

  @Test
  public void shouldDeleteBusinessTypeTest() {
    BusinessType businessType = new BusinessType();
    serviceUnderTest.delete(businessType);

    verify(businessTypeRepo, times(1)).delete(businessType);
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentIdTest() {
    serviceUnderTest.findById(2L);
  }

  @Test
  public void shouldFindAllTest() {
    serviceUnderTest.findAll();

    verify(businessTypeRepo, times(1)).findAll();
  }

  @Test
  public void shouldFindExistentIdTest() {
    BusinessType foundBusinessType = serviceUnderTest.findById(1L);

    verify(businessTypeRepo, times(1)).findOne(1L);
    assertEquals(dummyBusinessType, foundBusinessType);
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentTitleTest() {
    when(businessTypeRepo.findByTitle(NOT_EXISTENT_TITLE)).thenReturn(Optional.empty());
    serviceUnderTest.findByTitle(NOT_EXISTENT_TITLE);
  }

  @Test
  public void shouldFindByTitleTest() {
    when(businessTypeRepo.findByTitle(TITLE)).thenReturn(Optional.of(dummyBusinessType));
    BusinessType foundBusinessType = serviceUnderTest.findByTitle(TITLE);

    verify(businessTypeRepo, times(1)).findByTitle(TITLE);
    assertEquals(dummyBusinessType, foundBusinessType);
  }

  @Test(expected = NotFoundException.class)
  public void shouldThrowNotFoundExceptionOnAttemptToFindNonExistentCodeKvedTest() {
    when(businessTypeRepo.findByCodeKved(CODE_KVED)).thenReturn(Optional.empty());
    serviceUnderTest.findByCodeKved(CODE_KVED);
  }

  @Test
  public void shouldFindByCodeKvedTest() {
    when(businessTypeRepo.findByCodeKved(CODE_KVED)).thenReturn(Optional.of(dummyBusinessType));
    BusinessType foundBusinessType = serviceUnderTest.findByCodeKved(CODE_KVED);

    verify(businessTypeRepo, times(1)).findByCodeKved(CODE_KVED);
    assertEquals(dummyBusinessType, foundBusinessType);
  }

  @Test
  public void shouldReturnTrueIfCodekvedAvailableTest() {
    when(businessTypeRepo.codeKvedAvailable(CODE_KVED)).thenReturn(true);
    boolean result = serviceUnderTest.isAvailable("codeKved", CODE_KVED);

    verify(businessTypeRepo, times(1)).codeKvedAvailable(CODE_KVED);
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseIfCodekvedUnavailableTest() {
    when(businessTypeRepo.codeKvedAvailable(CODE_KVED)).thenReturn(false);
    boolean result = serviceUnderTest.isAvailable("codeKved", CODE_KVED);

    verify(businessTypeRepo, times(1)).codeKvedAvailable(CODE_KVED);
    assertFalse(result);
  }

  @Test
  public void shouldReturnTrueIfTitleAvailableTest() {
    when(businessTypeRepo.titleAvailable(TITLE)).thenReturn(true);
    boolean result = serviceUnderTest.isAvailable("title", TITLE);

    verify(businessTypeRepo, times(1)).titleAvailable(TITLE);
    assertTrue(result);
  }

  @Test
  public void shouldReturnFalseIfTitleUnavailableTest() {
    when(businessTypeRepo.titleAvailable(TITLE)).thenReturn(false);
    boolean result = serviceUnderTest.isAvailable("title", TITLE);

    verify(businessTypeRepo, times(1)).titleAvailable(TITLE);
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

  @Test
  public void shouldReturnFalseOnNullCodeKvedFieldTest() {
    boolean result = serviceUnderTest.isAvailable("codeKved", null);
    assertFalse(result);
  }
}
