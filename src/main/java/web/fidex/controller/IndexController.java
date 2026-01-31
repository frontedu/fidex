package web.fidex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.fidex.model.fidex_model.Usuario;

@Controller
public class IndexController {

	@GetMapping(value = { "/", "/index.html" })
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(Model model, jakarta.servlet.http.HttpServletRequest request) {
		if (request.getParameter("error") != null) {
			model.addAttribute("erro", "Usuário ou senha inválidos. Verifique seus dados e tente novamente.");
		}
		if (!model.containsAttribute("usuario")) {
			model.addAttribute("usuario", new Usuario());
		}
		return "login";
	}

}