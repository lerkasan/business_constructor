package ua.com.brdo.business.constructor.constraint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.brdo.business.constructor.model.InputType;
import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NoCycleValidatorTest {

    private NoCycleValidator underTest;

    @Mock
    private ConstraintValidatorContext context;

    private Question question1;
    private Question question2;
    private Option option12;
    private Option option2;
    private Option option21;

    /*
    Question1(Option12->Q2)
    Question2(Option2->null,  -no cycle
              Option21->Q1) -here cycle
     */
    @Before
    public void setUp() throws Exception {
        underTest = new NoCycleValidator();

        question1 = new Question();
        question1.setText("question1");
        question1.setInputType(InputType.SINGLE_CHOICE);

        question1.setId(1L);

        question2 = new Question();
        question2.setText("question2");
        question2.setInputType(InputType.SINGLE_CHOICE);

        question2.setId(2L);

        option12 = new Option();
        option12.setTitle("option12");
        option12.setQuestion(question1);
        question1.addOption(option12);
        option12.setNextQuestion(question2);

        option12.setId(1L);

        option2 = new Option();
        option2.setTitle("option2");
        option2.setQuestion(question2);
        question2.addOption(option2);

        option2.setId(2L);

        option21 = new Option();
        option21.setTitle("option21");
        option21.setQuestion(question2);
        question2.addOption(option21);
        option21.setNextQuestion(question1);

        option2.setId(3L);
    }

    @Test
    public void shouldReturnFalseWhenOptionProduceCycle() throws Exception {
        final boolean result = underTest.isValid(option21.getNextQuestion(), context);

        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenOptionNotProduceCycle() throws Exception {
        final boolean result = underTest.isValid(option2.getNextQuestion(), context);

        assertTrue(result);
    }
}
