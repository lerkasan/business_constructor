package ua.com.brdo.business.constructor.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;
import ua.com.brdo.business.constructor.model.QuestionOption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class QuestionServiceTest {

    private Option dummyOption;

    private Question dummyQuestion;

    private QuestionOption dummyQuestionOption;

    @Autowired
    private QuestionService questionService;

    @Before
    public void init() {
        dummyOption = new Option();
        dummyOption.setTitle("dummy option");

        dummyQuestion = new Question();
        dummyQuestion.setText("dummy question");

        dummyQuestionOption = new QuestionOption();
        dummyQuestionOption.setQuestion(dummyQuestion);
        dummyQuestionOption.setOption(dummyOption);
    }

    @Test
    public void shouldAddOptiontoQuestionTest() {
        questionService.addOption(dummyQuestion, dummyOption);
        Set<QuestionOption> actualQuestionOptions = dummyQuestion.getQuestionOptions();

        assertTrue(actualQuestionOptions.contains(dummyQuestionOption));
    }

    @Test
    public void shouldRemoveOptionFromQuestionTest() {
        questionService.deleteOption(dummyQuestion, dummyOption);
        Set<QuestionOption> actualQuestionOptions = dummyQuestion.getQuestionOptions();

        assertFalse(actualQuestionOptions.contains(dummyQuestionOption));
    }

    @Test
    public void shouldSetDefaultInputTypeIfNoneSpecified() {
        dummyQuestion = questionService.create(dummyQuestion);

        assertEquals("SINGLE_CHOICE", dummyQuestion.getInputType());
    }
}
