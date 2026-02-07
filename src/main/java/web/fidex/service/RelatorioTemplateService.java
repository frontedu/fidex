package web.fidex.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class RelatorioTemplateService {

    private final TemplateEngine templateEngine;

    public RelatorioTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process("relatorios/" + templateName, context);
    }
}
