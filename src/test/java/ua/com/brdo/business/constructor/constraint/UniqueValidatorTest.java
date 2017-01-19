package ua.com.brdo.business.constructor.constraint;

import org.apache.catalina.core.ApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import ua.com.brdo.business.constructor.model.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UniqueValidatorTest {

    private static final String FIELD_EMAIL = "email";
    private static final String FIELD_USERNAME = "username";
    private static final String UNSUPPORTED_FIELD = "someUnsupportedField";

    @Mock private UniqueValidatable service;
    @Mock private Unique annotation;
    @Mock private ConstraintValidatorContext context;
    @Mock private ApplicationContext applicationContext;
    @InjectMocks private UniqueValidator underTest = new UniqueValidator();

    @Test
    public void shouldInitializeIfFieldIsEmail() throws Exception {
        when(annotation.field()).thenReturn(FIELD_EMAIL);
        when(annotation.object()).thenReturn(User.class);

        underTest.initialize(annotation);

        verify(annotation, times(2)).field();
    }

    @Test
    public void shouldInitializeIfFieldIsUsername() throws Exception {
        when(annotation.field()).thenReturn(FIELD_USERNAME);
        when(annotation.object()).thenReturn(User.class);

        underTest.initialize(annotation);

        verify(annotation, times(2)).field();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnInitializationIfFieldIsNotAllowed() throws Exception {
        when(annotation.field()).thenReturn(UNSUPPORTED_FIELD);
        when(annotation.object()).thenReturn(User.class);

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
        when(service.isAvailable(FIELD_EMAIL, param)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfEmailIsUnavailable() throws Exception {
        String param = "email";
        mockValidatorHasTypeOf(User.class, FIELD_EMAIL);
        when(service.isAvailable(FIELD_EMAIL, param)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueIfUsernameIsAvailable() throws Exception {
        String param = "username";
        mockValidatorHasTypeOf(User.class, FIELD_USERNAME);
        when(service.isAvailable(FIELD_USERNAME, param)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfUsernameIsUnavailable() throws Exception {
        mockValidatorHasTypeOf(User.class, FIELD_USERNAME);
        String param = "username";
        when(service.isAvailable(FIELD_USERNAME, param)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }

    private void mockValidatorHasTypeOf(Class object, String field) {
        when(annotation.field()).thenReturn(field);
        when(annotation.object()).thenReturn(object);
        underTest.initialize(annotation);
    }
}
