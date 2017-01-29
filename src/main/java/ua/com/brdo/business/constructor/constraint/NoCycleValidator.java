package ua.com.brdo.business.constructor.constraint;


import ua.com.brdo.business.constructor.model.Option;
import ua.com.brdo.business.constructor.model.Question;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NoCycleValidator implements ConstraintValidator<NoCycle, Question> {

    @Override
    public void initialize(NoCycle constraintAnnotation) {
    }

    @Override
    public boolean isValid(Question question, ConstraintValidatorContext constraintContext) {
        if (null == question)
            return true;
        return !hasCycle(question);

    }

    private boolean hasCycle(Question question) {
        final List<Question> visitedNodes = new LinkedList<Question>();
        final List<Question> completedNodes = new LinkedList<Question>();
        return dfs(question, visitedNodes, completedNodes);
    }


    private boolean dfs(Question question, List<Question> visitedNodes, List<Question> completedNodes) {
        if (visitedNodes.contains(question)) {
            return !completedNodes.contains(question);
        }

        visitedNodes.add(question);
        Set<Option> iterator = question.getOptions();
        if (null != iterator) {
            for (Option option : iterator) {
                if (null != option.getNextQuestion())
                    if (dfs(option.getNextQuestion(), visitedNodes, completedNodes)) return true;
            }
        }
        completedNodes.add(question);

        return false;
    }
}

