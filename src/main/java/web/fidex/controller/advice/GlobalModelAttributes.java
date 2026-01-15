package web.fidex.controller.advice;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import web.fidex.model.Usuario;
import web.fidex.repository.UsuarioRepository;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UsuarioRepository usuarioRepository;

    public GlobalModelAttributes(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("cashbackPercent")
    public Double getCashbackPercent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return 5.0;
        }

        String currentUsername = auth.getName();
        Usuario usuario = usuarioRepository.findByNomeUsuarioIgnoreCase(currentUsername);

        if (usuario != null && usuario.getCashback() != null) {
            return usuario.getCashback();
        }

        return 5.0; // Default 5%
    }
}
