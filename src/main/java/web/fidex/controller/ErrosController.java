package web.fidex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrosController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrosController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String url = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                if (!url.contains(".well-known") && !url.contains("favicon.ico")) {
                    logger.warn("A URL {} foi acessada mas não existe.", url);
                }
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                logger.warn("Tentaram acessar a URL {} sem permissão.", url);
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("Ocorreu um erro interno no servidor.");
                return "error/500";
            }
        }
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
