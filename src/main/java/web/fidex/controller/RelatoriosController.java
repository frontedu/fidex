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

	@GetMapping("/compras")
	public ResponseEntity<byte[]> gerarRelatorioCompras() {
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
	public ResponseEntity<byte[]> gerarRelatorioPremios() {
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
	public ResponseEntity<byte[]> gerarRelatorioProdutos() {
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
	public ResponseEntity<byte[]> gerarRelatorioClientes() {
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
	public ResponseEntity<byte[]> gerarRelatorioSimplesVacinas() {
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
	public ResponseEntity<byte[]> gerarRelatorioComplexoTodosLotes() {
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