package web.fidex.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import web.fidex.service.CadastroUsuarioService;

@Controller
public class PerfilController {

    private final CadastroUsuarioService cadastroUsuarioService;

    public PerfilController(CadastroUsuarioService cadastroUsuarioService) {
        this.cadastroUsuarioService = cadastroUsuarioService;
    }

    @PostMapping("/perfil/cashback")
    public String salvarCashback(@RequestParam("cashback") Double cashback,
            jakarta.servlet.http.HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal(expression = "username") String username) {
        if (cashback == null) {
            cashback = 5.0;
        }
        if (cashback < 0) {
            cashback = 0.0;
        }
        if (cashback > 100) {
            cashback = 100.0;
        }

        if (username == null || username.isBlank()) {
            redirectAttributes.addFlashAttribute("erro", "Voce precisa estar logado para alterar as configuracoes.");
            return "redirect:/login";
        }

        cadastroUsuarioService.atualizarCashback(username, cashback);
        redirectAttributes.addFlashAttribute("sucesso",
                "Sucesso! Cashback de " + cashback + "% salvo para o usuario " + username);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/clientes");
    }
}
