package ua.com.brdo.business.constructor.constraint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AsciiValidatorTest {

    private AsciiValidator underTest;
    private char[] param;

    @Mock
    private ConstraintValidatorContext context;

    @Before
    public void setUp() throws Exception {
        underTest = new AsciiValidator();
    }

    @Test
    public void shouldReturnFalseIfParamIsNull() throws Exception {
        final boolean result = underTest.isValid(param, context);

        assertFalse("Array of chars 'param' is null.", result);
    }

    @Test
    public void shouldReturnFalseIfParamContainsCharLowerThanSpace() throws Exception {
        char lowerThanSpace = ' ' - 1;
        param = new char[]{'a', 'b', 'c', lowerThanSpace};

        final boolean result = underTest.isValid(param, context);

        assertFalse("Array 'param' should not be valid with char, lower than space.", result);
    }

    @Test
    public void shouldReturnFalseIfParamContainsCharGreaterThanTilda() throws Exception {
        char greaterThanTilda = '~' + 1;
        param = new char[]{'a', 'b', 'c', greaterThanTilda};

        final boolean result = underTest.isValid(param, context);

        assertFalse("Array 'param' should not be valid with char, greater than tilda.", result);
    }

    @Test
    public void shouldReturnTrueIfParamContainsValidChars() throws Exception {
        param = new char[] {'a', 'b', 'c'};

        final boolean result = underTest.isValid(param, context);

        assertTrue("Array 'param' should be considered valid, if contains allowed chars.", result);
    }
}
