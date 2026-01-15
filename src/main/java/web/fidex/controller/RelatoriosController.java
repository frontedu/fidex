package web.fidex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.fidex.service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

	private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);

	@Autowired
	private RelatorioService relatorioService;

	private boolean isAdmin() {
		return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

	@GetMapping("/compras")
	public Object gerarRelatorioCompras(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioSimplesVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");

		byte[] relatorio = relatorioService.gerarCompras();

		logger.debug("Relatório simples de todas as pessoas gerado");
		logger.debug("Retornando o relatório simples de todas as pessoas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Compras.pdf")
				.body(relatorio);
	}

	@GetMapping("/premios")
	public Object gerarRelatorioPremios(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioSimplesVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");

		byte[] relatorio = relatorioService.gerarPremios();

		logger.debug("Relatório simples de todas as pessoas gerado");
		logger.debug("Retornando o relatório simples de todas as pessoas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Premios.pdf")
				.body(relatorio);
	}

	@GetMapping("/produtos")
	public Object gerarRelatorioProdutos(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioSimplesVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");

		byte[] relatorio = relatorioService.gerarProdutos();

		logger.debug("Relatório simples de todas as pessoas gerado");
		logger.debug("Retornando o relatório simples de todas as pessoas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Produtos.pdf")
				.body(relatorio);
	}

	@GetMapping("/clientes")
	public Object gerarRelatorioClientes(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioSimplesVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");

		byte[] relatorio = relatorioService.gerarClientes();

		logger.debug("Relatório simples de todas as pessoas gerado");
		logger.debug("Retornando o relatório simples de todas as pessoas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Clientes.pdf")
				.body(relatorio);
	}

	@GetMapping("/vacinas")
	public Object gerarRelatorioSimplesVacinas(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioSimplesVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");

		byte[] relatorio = relatorioService.gerarRelatorioSimplesVacinas();

		logger.debug("Relatório simples de todas as pessoas gerado");
		logger.debug("Retornando o relatório simples de todas as pessoas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=VacinasSimples.pdf")
				.body(relatorio);
	}

	@GetMapping("/vacinascomlotes")
	public Object gerarRelatorioComplexoTodosLotes(jakarta.servlet.http.HttpServletRequest request,
			org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
		if (!isAdmin()) {
			String referer = request.getHeader("Referer");
			redirectAttributes.addFlashAttribute("erro", "Acesso negado: Requer privilégios de administrador.");
			return "redirect:" + (referer != null ? referer : "/clientes");
		}
		logger.trace("Entrou em gerarRelatorioComplexoTodosLotes");
		logger.debug("Gerando relatório complexo de todos os lotes");

		byte[] relatorio = relatorioService.gerarRelatorioComplexoTodosLotes();

		logger.debug("Relatório complexo de todos os lotes gerado");
		logger.debug("Retornando o relatório complexo de todos os lotes");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=LotesComplexo.pdf")
				.body(relatorio);
	}

}
