package web.fidex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {

    private final web.fidex.repository.UsuarioRepository usuarioRepository;
    private final web.fidex.service.CadastroUsuarioService cadastroUsuarioService;

    public PerfilController(web.fidex.repository.UsuarioRepository usuarioRepository,
            web.fidex.service.CadastroUsuarioService cadastroUsuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @PostMapping("/perfil/cashback")
    @org.springframework.transaction.annotation.Transactional
    public String salvarCashback(@RequestParam("cashback") Double cashback,
            jakarta.servlet.http.HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        // Validate range
        if (cashback == null)
            cashback = 5.0;
        if (cashback < 0)
            cashback = 0.0;
        if (cashback > 100)
            cashback = 100.0;

        // Get current user
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            redirectAttributes.addFlashAttribute("erro", "Você precisa estar logado para alterar as configurações.");
            return "redirect:/login";
        }

        String currentUsername = auth.getName();
        web.fidex.model.Usuario usuario = usuarioRepository.findByNomeUsuarioIgnoreCase(currentUsername);

        if (usuario != null) {
            usuario.setCashback(cashback);
            cadastroUsuarioService.salvar(usuario);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Sucesso! Cashback de " + cashback + "% salvo para o usuário " + currentUsername);
        } else {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro: Usuário '" + currentUsername + "' não encontrado no banco de dados.");
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/clientes");
    }
}
