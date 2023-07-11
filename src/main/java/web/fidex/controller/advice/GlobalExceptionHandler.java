package web.fidex.controller.advice;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

//Vai afetar todos os controladores
@ControllerAdvice  
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public String handleAllExceptions(HttpServletRequest request, Exception exception) {
        // ESQUEMA DO ID DO ERRO
        logger.error("A requisição {} lançou uma {}", request.getRequestURL(), exception);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        logger.error("Stack trace da exceção: {}", sw.toString());
        return "error";
    }

}