package web.fidex.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PerfilController {

    private static final String CASHBACK_SESSION_KEY = "cashbackPercent";

    @PostMapping("/perfil/cashback")
    public String salvarCashback(@RequestParam("cashback") Double cashback,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // Validate range
        if (cashback < 0)
            cashback = 0.0;
        if (cashback > 100)
            cashback = 100.0;

        // Store in session (per-user setting)
        session.setAttribute(CASHBACK_SESSION_KEY, cashback);

        redirectAttributes.addFlashAttribute("sucesso", "Cashback atualizado para " + cashback + "%");
        return "redirect:/clientes";
    }

    // This makes cashbackPercent available to all views
    @ModelAttribute("cashbackPercent")
    public Double cashbackPercent(HttpSession session) {
        Object value = session.getAttribute(CASHBACK_SESSION_KEY);
        return value != null ? (Double) value : 5.0; // Default 5%
    }
}
