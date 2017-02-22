package ua.com.brdo.business.constructor.utils;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ua.com.brdo.business.constructor.model.Stage;

@Service
public class HtmlRender {

    private static final String TEMPLATE_FILE = "flow";
    private TemplateEngine templateEngine;

    @Autowired
    public HtmlRender(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String renderFlow(List<Stage> stages) {
        final Context context = new Context();
        context.setVariable("stages", stages);
        return templateEngine.process(TEMPLATE_FILE, context);
    }
}
