package web.fidex.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Prize;
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Purchase;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.PrizeRepository;
import web.fidex.repository.ProductRepository;
import web.fidex.repository.PurchaseRepository;

@Service
public class RelatorioService {

	private static final Logger logger = LoggerFactory.getLogger(RelatorioService.class);

	private final RelatorioTemplateService templateService;
	private final ClientRepository clientRepository;
	private final ProductRepository productRepository;
	private final PurchaseRepository purchaseRepository;
	private final PrizeRepository prizeRepository;

	public RelatorioService(RelatorioTemplateService templateService,
			ClientRepository clientRepository,
			ProductRepository productRepository,
			PurchaseRepository purchaseRepository,
			PrizeRepository prizeRepository) {
		this.templateService = templateService;
		this.clientRepository = clientRepository;
		this.productRepository = productRepository;
		this.purchaseRepository = purchaseRepository;
		this.prizeRepository = prizeRepository;
	}

	private byte[] generatePdf(String templateName, Map<String, Object> variables) {
		long start = System.currentTimeMillis();
		String html = templateService.render(templateName, variables);
		long renderTime = System.currentTimeMillis();
		logger.info("Template render time: {}ms", renderTime - start);

		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(html, "");
			builder.toStream(os);
			builder.run();
			long pdfTime = System.currentTimeMillis();
			logger.info("PDF generation time: {}ms", pdfTime - renderTime);
			return os.toByteArray();
		} catch (Exception e) {
			logger.error("Error generating PDF from template: " + templateName, e);
			return null;
		}
	}

	public byte[] gerarCompras(String username) {
		List<Purchase> compras = purchaseRepository.findByCreatedBy(username, Pageable.unpaged()).getContent();
		Map<String, Object> vars = new HashMap<>();
		vars.put("compras", compras);
		vars.put("username", username);
		return generatePdf("compras", vars);
	}

	public byte[] gerarProdutos(String username) {
		List<Product> produtos = productRepository.findByCreatedBy(username, Pageable.unpaged()).getContent();
		Map<String, Object> vars = new HashMap<>();
		vars.put("produtos", produtos);
		vars.put("username", username);
		return generatePdf("produtos", vars);
	}

	public byte[] gerarPremios(String username) {
		List<Prize> premios = prizeRepository.findByCreatedBy(username, Pageable.unpaged()).getContent();
		Map<String, Object> vars = new HashMap<>();
		vars.put("premios", premios);
		vars.put("username", username);
		return generatePdf("premios", vars);
	}

	public byte[] gerarClientes(String username) {
		List<Client> clientes = clientRepository.findByCreatedBy(username, Pageable.unpaged()).getContent();
		Map<String, Object> vars = new HashMap<>();
		vars.put("clientes", clientes);
		vars.put("username", username);
		return generatePdf("clientes", vars);
	}
}