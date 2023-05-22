package web.controlevacinacao.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

//Talvez alterar para nao mostrar Model e BindingResult
//Acho que vai dar problema quando colocar AJAX na jogada

@Aspect
@Component
public class ApplicationLoggingAspect {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationLoggingAspect.class);

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controllersPointcut() {
	}

	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void servicesPointcut() {
	}

	@Around("controllersPointcut()")
	public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {

		//Um monte de informacao extra que poderiamos mostrar
//		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = attributes.getRequest();
//		logger.info("URL            : {}", request.getRequestURL().toString());
//		logger.info("HTTP Method    : {}", request.getMethod());
//		logger.info("IP             : {}", request.getRemoteAddr());
//		Enumeration<String> nomesParametros = request.getParameterNames();
//		String nomeParametro2;
//		while (nomesParametros.hasMoreElements()) {
//			nomeParametro2 = nomesParametros.nextElement();
//			logger.info("PARAMETRO      : {}", nomeParametro2);
//			logger.info("VALOR PARAMETRO: {}", request.getParameter(nomeParametro2));
//		}
//		Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
//		Map<String, ?> outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
//		if (inputFlashMap != null) {
//			if (!inputFlashMap.isEmpty()) {
//				logger.info("inputFlashMap      :", inputFlashMap);
//				for (String nomeAtributo : inputFlashMap.keySet()) {
//					logger.info("\tAtributo       : {}", nomeAtributo);
//					logger.info("\tValor Atributo : {}", inputFlashMap.get(nomeAtributo));
//				}
//			}
//		}
//		if (outputFlashMap != null) {
//			if (!outputFlashMap.isEmpty()) {
//				logger.info("outputFlashMap      :", outputFlashMap);
//				for (String nomeAtributo : outputFlashMap.keySet()) {
//					logger.info("\tAtributo       : {}", nomeAtributo);
//					logger.info("\tValor Atributo : {}", outputFlashMap.get(nomeAtributo));
//				}
//			}
//		}

		logMethodName(joinPoint);
		logParametersReceived(joinPoint);
		Object result = joinPoint.proceed();
		String nomeDaView = getViewName(result);

        if (nomeDaView != null) {
			if (nomeDaView.startsWith("redirect:")) {
				logger.trace("Redirecionando para a URL: {}", nomeDaView.substring(9));
			} else {
				logger.trace("Encaminhando para a view: {}", nomeDaView);
			}
		}

		return result;
	}

	@Around("servicesPointcut()")
	public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
		logMethodName(joinPoint);
		logParametersReceived(joinPoint);
		Object result = joinPoint.proceed();
		logger.trace("Retornando: {}", result);
		return result;
	}

	private Map<String, Object> getParameters(JoinPoint joinPoint) {
		CodeSignature signature = (CodeSignature) joinPoint.getSignature();
		HashMap<String, Object> map = new HashMap<>();
		String[] parameterNames = signature.getParameterNames();
		for (int i = 0; i < parameterNames.length; i++) {
			map.put(parameterNames[i], joinPoint.getArgs()[i]);
		}
		return map;
	}
	
	private void logMethodName(ProceedingJoinPoint joinPoint) {
		String nomeCompletoClasse = joinPoint.getSignature().getDeclaringTypeName();
		int posicaoPonto = nomeCompletoClasse.lastIndexOf(".");
		String nomeClasseApenas = (posicaoPonto != -1) ? nomeCompletoClasse.substring(posicaoPonto + 1) : nomeCompletoClasse;
		logger.trace("Entrou no método: {}.{}", nomeClasseApenas, joinPoint.getSignature().getName());
	}
	
	private void logParametersReceived(ProceedingJoinPoint joinPoint) {
		Map<String, Object> parametros = getParameters(joinPoint);
		if (!parametros.isEmpty()) {
			logger.debug("Parâmetros recebidos:");
			for (String nomeParametro : parametros.keySet()) {
				logger.debug("\t{}: {}", nomeParametro, parametros.get(nomeParametro));
			}
		}
	}
	
	private String getViewName(Object result) {
		String nomeDaView = null;
		if (result instanceof ModelAndView) {
			ModelAndView retorno = (ModelAndView) result;
			nomeDaView = retorno.getViewName();
		} else {
			if (result instanceof String) {
				nomeDaView = (String) result;
			}
		}
		return nomeDaView;
	}

}

