package ua.com.brdo.business.constructor.constraint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.brdo.business.constructor.service.UserService;

import javax.validation.ConstraintValidatorContext;

@RunWith(MockitoJUnitRunner.class)
public class UniqueValidatorTest {

    private static final String TYPE_EMAIL = "email";
    private static final String TYPE_USERNAME = "username";
    private static final String UNSUPPORTED_TYPE = "someUnsupportedType";

    @Mock private UserService userService;
    @Mock private Unique annotation;
    @Mock private ConstraintValidatorContext context;
    @InjectMocks private UniqueValidator underTest = new UniqueValidator();

    @Test
    public void shouldInitializeIfTypeIsEmail() throws Exception {
        when(annotation.type()).thenReturn(TYPE_EMAIL);

        underTest.initialize(annotation);

        verify(annotation, times(1)).type();
    }

    @Test
    public void shouldInitializeIfTypeIsUsername() throws Exception {
        when(annotation.type()).thenReturn(TYPE_USERNAME);

        underTest.initialize(annotation);

        verify(annotation, times(1)).type();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnInitializationIfTypeIsNotAllowed() throws Exception {
        when(annotation.type()).thenReturn(UNSUPPORTED_TYPE);

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
        mockValidatorHasTypeOf(TYPE_EMAIL);
        when(userService.isEmailAvailable(param)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfEmailIsUnavailable() throws Exception {
        String param = "email";
        mockValidatorHasTypeOf(TYPE_EMAIL);
        when(userService.isEmailAvailable(param)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueIfUsernameIsAvailable() throws Exception {
        String param = "username";
        mockValidatorHasTypeOf(TYPE_USERNAME);
        when(userService.isUsernameAvailable(param)).thenReturn(true);

        boolean result = underTest.isValid(param, context);

        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfUsernameIsUnavailable() throws Exception {
        mockValidatorHasTypeOf(TYPE_USERNAME);
        String param = "username";
        when(userService.isUsernameAvailable(param)).thenReturn(false);

        boolean result = underTest.isValid(param, context);

        assertFalse(result);
    }

    private void mockValidatorHasTypeOf(String type) {
        when(annotation.type()).thenReturn(type);
        underTest.initialize(annotation);
    }
}
