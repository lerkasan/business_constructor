package ua.com.brdo.business.constructor.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import org.apache.catalina.core.ApplicationContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.brdo.business.constructor.model.User;
import ua.com.brdo.business.constructor.repository.UserRepository;

@RunWith(MockitoJUnitRunner.class)
public class UniqueValidatorTest {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_USERNAME = "username";
    private static final String UNSUPPORTED_FIELD = "someUnsupportedField";
    private static final Long DEFAULT_ID = 0L;

    @Mock private UniqueValidatable service;
    @Mock private Unique annotation;
    @Mock private ConstraintValidatorContext context;
    @InjectMocks private UniqueValidator underTest = new UniqueValidator();

    @Test
    public void shouldInitializeIfFieldIsEmail() throws Exception {
        when(annotation.field()).thenReturn(FIELD_EMAIL);

        underTest.initialize(annotation);

        verify(annotation, times(1)).field();
        verify(annotation, times(1)).service();
    }

    @Test
    public void shouldInitializeIfFieldIsUsername() throws Exception {
        when(annotation.field()).thenReturn(FIELD_USERNAME);

        underTest.initialize(annotation);

        verify(annotation, times(1)).field();
        verify(annotation, times(1)).service();
    }

    @Ignore("New implementation does not throw this exception. Switch check was removed to avoid necessity to add each new field type to switch case in validator.")
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnInitializationIfFieldIsNotAllowed() throws Exception {
        when(annotation.field()).thenReturn(UNSUPPORTED_FIELD);

        underTest.initialize(annotation);
    }

    @Test
    public void shouldReturnTrueIfUserServiceIsNull() throws Exception {
        String param = "";
        underTest = new UniqueValidator();  //set UserService to null

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueIfParamIsNull() throws Exception {
        String param = null;

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnTrueIfEmailIsAvailable() throws Exception {
        String param = "email";
        mockValidatorHasTypeOf(User.class, FIELD_EMAIL);
        when(service.isAvailable(FIELD_EMAIL, param, DEFAULT_ID)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Ignore("Method returns true if application context is null. In unit tests this context is unavailable.")
    @Test
    public void shouldReturnFalseIfEmailIsUnavailable() throws Exception {
        String param = "email";
        mockValidatorHasTypeOf(User.class, FIELD_EMAIL);
        when(service.isAvailable(FIELD_EMAIL, param, DEFAULT_ID)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }


    @Test
    public void shouldReturnTrueIfUsernameIsAvailable() throws Exception {
        String param = "username";
        mockValidatorHasTypeOf(User.class, FIELD_USERNAME);
        when(service.isAvailable(FIELD_USERNAME, param, DEFAULT_ID)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Ignore("Method returns true if application context is null. In unit tests this context is unavailable.")
    @Test
    public void shouldReturnFalseIfUsernameIsUnavailable() throws Exception {
        mockValidatorHasTypeOf(User.class, FIELD_USERNAME);
        String param = "username";
        when(service.isAvailable(FIELD_USERNAME, param, DEFAULT_ID)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }

    private void mockValidatorHasTypeOf(Class object, String field) {
        when(annotation.field()).thenReturn(field);
        when(annotation.service()).thenReturn(object);
        underTest.initialize(annotation);
    }
}
