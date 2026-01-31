package web.fidex.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import web.fidex.service.RelatorioServiceKt

/**
 * Controller for PDF report generation. Uses Flying Saucer + Thymeleaf for modern HTML-to-PDF
 * conversion.
 *
 * All authenticated users can access reports.
 * - Admin: sees all data
 * - Regular user: sees only their own data
 */
@Controller
@RequestMapping("/relatorios")
class RelatorioController(private val relatorioService: RelatorioServiceKt) {

    @GetMapping("/clientes")
    fun gerarRelatorioClientes(): ResponseEntity<ByteArray> {
        val pdf =
                relatorioService.gerarClientes()
                        ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Clientes.pdf")
                .body(pdf)
    }

    @GetMapping("/compras")
    fun gerarRelatorioCompras(): ResponseEntity<ByteArray> {
        val pdf =
                relatorioService.gerarCompras()
                        ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Compras.pdf")
                .body(pdf)
    }

    @GetMapping("/produtos")
    fun gerarRelatorioProdutos(): ResponseEntity<ByteArray> {
        val pdf =
                relatorioService.gerarProdutos()
                        ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Produtos.pdf")
                .body(pdf)
    }

    @GetMapping("/premios")
    fun gerarRelatorioPremios(): ResponseEntity<ByteArray> {
        val pdf =
                relatorioService.gerarPremios()
                        ?: return ResponseEntity.internalServerError().build()

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Premios.pdf")
                .body(pdf)
    }
}
