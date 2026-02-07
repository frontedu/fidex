package web.fidex.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import web.fidex.model.Usuario;
import web.fidex.service.UsuarioService;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UsuarioService usuarioService;

    public GlobalModelAttributes(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @ModelAttribute("username")
    public String getUsername(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return "";
        }
        return authentication.getName();
    }

    @ModelAttribute("cashbackPercent")
    public Double getCashbackPercent(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return 5.0;
        }

        try {
            String currentUsername = authentication.getName();
            Usuario usuario = usuarioService.findByNomeUsuarioIgnoreCase(currentUsername);

            if (usuario != null && usuario.getCashback() != null) {
                return usuario.getCashback();
            }
        } catch (Exception e) {
            System.err.println("Error fetching user cashback: " + e.getMessage());
            e.printStackTrace();
        }

        return 5.0;
    }
}
