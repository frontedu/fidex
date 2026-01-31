package web.fidex.service

import java.time.LocalDateTime
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import web.fidex.repository.ClientRepository
import web.fidex.repository.PrizeRepository
import web.fidex.repository.ProductRepository
import web.fidex.repository.PurchaseRepository

/**
 * Service for generating PDF reports with user-based filtering. Uses Flying Saucer + Thymeleaf for
 * HTML-to-PDF conversion.
 */
@Service
class RelatorioServiceKt(
        private val pdfService: PdfService,
        private val clientRepository: ClientRepository,
        private val purchaseRepository: PurchaseRepository,
        private val productRepository: ProductRepository,
        private val prizeRepository: PrizeRepository
) {

    /** Check if current user is admin. */
    private fun isAdmin(): Boolean {
        return SecurityContextHolder.getContext().authentication?.authorities?.any {
            it.authority == "ROLE_ADMIN"
        }
                ?: false
    }

    /** Get the current authenticated username. */
    private fun getCurrentUserId(): String {
        return SecurityContextHolder.getContext().authentication?.name ?: ""
    }

    /** Generate Clientes report. Admin sees all, regular user sees only their clients. */
    fun gerarClientes(): ByteArray? {
        val userId = getCurrentUserId()
        val isAdmin = isAdmin()

        val clientes =
                if (isAdmin) {
                    clientRepository.findAll()
                } else {
                    clientRepository.findByCreatedBy(userId, Pageable.unpaged()).content
                }

        val totalPontos = clientes.mapNotNull { it.points }.sum()

        val variables =
                mapOf(
                        "clientes" to clientes,
                        "totalPontos" to totalPontos,
                        "dataGeracao" to LocalDateTime.now(),
                        "usuario" to if (isAdmin) "Administrador" else userId
                )

        return pdfService.gerarPdf("clientes", variables)
    }

    /** Generate Compras report. Admin sees all, regular user sees only their purchases. */
    fun gerarCompras(): ByteArray? {
        val userId = getCurrentUserId()
        val isAdmin = isAdmin()

        val compras =
                if (isAdmin) {
                    purchaseRepository.findAll()
                } else {
                    purchaseRepository.findByCreatedBy(userId, Pageable.unpaged()).content
                }

        val totalValor = compras.mapNotNull { it.price }.sumOf { it.toDouble() }
        val totalPontos = compras.mapNotNull { it.points }.sum()

        val variables =
                mapOf(
                        "compras" to compras,
                        "totalValor" to totalValor,
                        "totalPontos" to totalPontos,
                        "dataGeracao" to LocalDateTime.now(),
                        "usuario" to if (isAdmin) "Administrador" else userId
                )

        return pdfService.gerarPdf("compras", variables)
    }

    /** Generate Produtos report. Admin sees all, regular user sees only their products. */
    fun gerarProdutos(): ByteArray? {
        val userId = getCurrentUserId()
        val isAdmin = isAdmin()

        val produtos =
                if (isAdmin) {
                    productRepository.findAll()
                } else {
                    productRepository.findByCreatedBy(userId, Pageable.unpaged()).content
                }

        val totalEstoque = produtos.mapNotNull { it.quantity }.sum()

        val variables =
                mapOf(
                        "produtos" to produtos,
                        "totalEstoque" to totalEstoque,
                        "dataGeracao" to LocalDateTime.now(),
                        "usuario" to if (isAdmin) "Administrador" else userId
                )

        return pdfService.gerarPdf("produtos", variables)
    }

    /** Generate Premios report. Admin sees all, regular user sees only their prizes. */
    fun gerarPremios(): ByteArray? {
        val userId = getCurrentUserId()
        val isAdmin = isAdmin()

        val premios =
                if (isAdmin) {
                    prizeRepository.findAll()
                } else {
                    prizeRepository.findByCreatedBy(userId, Pageable.unpaged()).content
                }

        val variables =
                mapOf(
                        "premios" to premios,
                        "dataGeracao" to LocalDateTime.now(),
                        "usuario" to if (isAdmin) "Administrador" else userId
                )

        return pdfService.gerarPdf("premios", variables)
    }
}
