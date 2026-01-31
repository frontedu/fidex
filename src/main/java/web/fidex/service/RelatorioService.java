package web.fidex.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {

	private static final Logger logger = LoggerFactory.getLogger(RelatorioService.class);

	@Autowired
	private DataSource dataSource;

	public byte[] gerarCompras() {
		try (InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/compras.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: /relatorios/compras.jasper");
				return null;
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório de compras: ", e);
		}
		return null;
	}

	public byte[] gerarProdutos() {
		try (InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/produtos.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: /relatorios/produtos.jasper");
				return null;
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório de produtos: ", e);
		}
		return null;
	}

	public byte[] gerarPremios() {
		try (InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/premios.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: /relatorios/premios.jasper");
				return null;
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório de prêmios: ", e);
		}
		return null;
	}

	public byte[] gerarClientes() {
		try (InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/clientes.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: /relatorios/clientes.jasper");
				return null;
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório de clientes: ", e);
		}
		return null;
	}

	public byte[] gerarRelatorioSimplesVacinas() {
		try (InputStream arquivoJasper = getClass()
				.getResourceAsStream("/relatorios/RelatorioSQLDiretoSimplesVacina.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: RelatorioSQLDiretoSimplesVacina.jasper");
				return null;
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório de vacinas: ", e);
		}
		return null;
	}

	public byte[] gerarRelatorioComplexoTodosLotes() {
		try (InputStream arquivoJasper = getClass()
				.getResourceAsStream("/relatorios/RelatorioSQLDiretoComplexoVacina.jasper");
				Connection conexao = dataSource.getConnection()) {
			if (arquivoJasper == null) {
				logger.error("Arquivo jasper não encontrado: RelatorioSQLDiretoComplexoVacina.jasper");
				return null;
			}
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("TITULO", "Outra coisa");
			JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, parametros, conexao);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException | SQLException | java.io.IOException e) {
			logger.error("Erro na geração do relatório complexo: ", e);
		}
		return null;
	}
}