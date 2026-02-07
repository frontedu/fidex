package web.fidex.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.fidex.service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

	private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);

	private final RelatorioService relatorioService;

	public RelatoriosController(RelatorioService relatorioService) {
		this.relatorioService = relatorioService;
	}

	@GetMapping("/compras")
	public ResponseEntity<byte[]> gerarRelatorioCompras(
			@AuthenticationPrincipal(expression = "username") String username) {
		byte[] relatorio = relatorioService.gerarCompras(username);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Compras.pdf")
				.body(relatorio);
	}

	@GetMapping("/premios")
	public ResponseEntity<byte[]> gerarRelatorioPremios(
			@AuthenticationPrincipal(expression = "username") String username) {
		byte[] relatorio = relatorioService.gerarPremios(username);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Premios.pdf")
				.body(relatorio);
	}

	@GetMapping("/produtos")
	public ResponseEntity<byte[]> gerarRelatorioProdutos(
			@AuthenticationPrincipal(expression = "username") String username) {
		byte[] relatorio = relatorioService.gerarProdutos(username);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Produtos.pdf")
				.body(relatorio);
	}

	@GetMapping("/clientes")
	public ResponseEntity<byte[]> gerarRelatorioClientes(
			@AuthenticationPrincipal(expression = "username") String username) {
		byte[] relatorio = relatorioService.gerarClientes(username);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Clientes.pdf")
				.body(relatorio);
	}
}
