package web.fidex.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import web.fidex.model.Papel;
import web.fidex.model.Usuario;
import web.fidex.repository.PapelRepository;
import web.fidex.service.CadastroUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	private final PapelRepository papelRepository;
	private final CadastroUsuarioService cadastroUsuarioService;
	private final PasswordEncoder passwordEncoder;

	public UsuarioController(PapelRepository papelRepository,
			CadastroUsuarioService cadastroUsuarioService,
			PasswordEncoder passwordEncoder) {
		this.papelRepository = papelRepository;
		this.cadastroUsuarioService = cadastroUsuarioService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/cadastrar")
	public String abrirCadastroUsuario(Usuario usuario, Model model) {
		List<Papel> papeis = papelRepository.findAll();
		model.addAttribute("todosPapeis", papeis);
		return "usuario/cadastrar";
	}

	@PostMapping("/cadastrar")
	public String cadastrarNovoUsuario(@Valid Usuario usuario, BindingResult resultado,
			Model model, RedirectAttributes redirectAttributes) {
		if (resultado.hasErrors()) {
			logger.info("O usuario recebido para cadastrar não é válido.");
			StringBuilder erros = new StringBuilder();
			for (FieldError erro : resultado.getFieldErrors()) {
				logger.info("{}", erro);
				erros.append(erro.getDefaultMessage()).append(" ");
			}
			redirectAttributes.addFlashAttribute("erro", erros.toString().trim());
			redirectAttributes.addFlashAttribute("mostrarCadastro", true);
			return "redirect:/login";
		}

		try {
			// Ensure it's a new user
			usuario.setCodigo(null);
			usuario.setAtivo(true);
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

			// Re-fetch papeis from database to avoid detached entity issues
			List<Papel> papeisManaged = new java.util.ArrayList<>();
			if (usuario.getPapeis() != null) {
				for (Papel papel : usuario.getPapeis()) {
					if (papel != null && papel.getCodigo() != null) {
						papelRepository.findById(papel.getCodigo()).ifPresent(papeisManaged::add);
					}
				}
			}
			usuario.setPapeis(papeisManaged);

			cadastroUsuarioService.salvar(usuario);
			redirectAttributes.addFlashAttribute("sucesso", "Conta criada com sucesso! Faça login.");
			return "redirect:/login";
		} catch (Exception e) {
			logger.error("Erro ao cadastrar usuário: ", e);

			// Get root cause for better debugging
			Throwable rootCause = e;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				rootCause = rootCause.getCause();
			}

			redirectAttributes.addFlashAttribute("erro", "Erro ao criar conta: " + rootCause.getMessage());
			redirectAttributes.addFlashAttribute("debugErro", rootCause.getMessage());
			redirectAttributes.addFlashAttribute("mostrarCadastro", true);
			return "redirect:/login";
		}
	}
}
