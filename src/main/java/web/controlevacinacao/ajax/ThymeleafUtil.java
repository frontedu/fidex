package web.controlevacinacao.ajax;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ThymeleafUtil {

	@Autowired
	private TemplateEngine templateEngine;
	
	// @Autowired
	// private ServletContext servletContext; 
	// private Context context; // = new Context();

	public String processThymeleafTemplate(HttpServletRequest request,
										   HttpServletResponse response, 
										   Map<String, Object> atributos, 
										   String templateName) {
		return processThymeleafTemplate(request, response, atributos, templateName, null);
	}

	// public String processThymeleafTemplate(HttpServletRequest request,
	// 									   HttpServletResponse response, 
	// 									   Map<String, Object> atributos,
	// 									   String templateName,
	// 									   String fragmentName) {
	// 	WebContext context = new WebContext(request, response, servletContext);
	// 	context.setVariables(atributos);
	// 	RequestContext requestContext = new RequestContext(request, response, servletContext, atributos);
	// 	SpringWebMvcThymeleafRequestContext thymeleafRequestContext = new SpringWebMvcThymeleafRequestContext(requestContext, request);
	// 	context.setVariable("thymeleafRequestContext", thymeleafRequestContext);
	// 	String html;
	// 	if (fragmentName == null || fragmentName.isBlank()) {
	// 		html = templateEngine.process(templateName, context);
	// 	} else {
	// 		html = templateEngine.process(templateName, Set.of(fragmentName), context);
	// 	}
	// 	return html;
	// }

	public String processThymeleafTemplate(HttpServletRequest request,
										   HttpServletResponse response, 
										   Map<String, Object> atributos,
										   String templateName,
										   String fragmentName) {
		Context context = new Context();
		context.setVariables(atributos);
		String html;
		if (fragmentName == null || fragmentName.isBlank()) {
			html = templateEngine.process(templateName, context);
		} else {
			html = templateEngine.process(templateName, Set.of(fragmentName), context);
		}
		return html;
	}
	
}