package web.fidex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web.fidex.model.Usuario;

@Controller
public class IndexController {

	@GetMapping(value = { "/", "/index.html" })
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("usuario", new Usuario());
		return "login";
	}

}