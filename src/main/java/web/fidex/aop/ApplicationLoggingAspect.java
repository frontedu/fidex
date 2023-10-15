package web.fidex.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationLoggingAspect {

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controllersPointcut() {
	}

	@Pointcut("within(@org.springframework.stereotype.Service *)")
	public void servicesPointcut() {
	}

	@Around("controllersPointcut()")
	public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		return result;
	}

	@Around("servicesPointcut()")
	public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = joinPoint.proceed();
		return result;
	}

}

