package web.fidex.service

import java.io.ByteArrayOutputStream
import java.util.Locale
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.xhtmlrenderer.pdf.ITextRenderer

/**
 * Service for generating PDF reports using Flying Saucer + Thymeleaf. Replaces Jasper for more
 * flexible HTML-based report generation.
 */
@Service
class PdfService(private val templateEngine: TemplateEngine) {

    /**
     * Generates a PDF from a Thymeleaf template.
     *
     * @param templateName Name of the template in /templates/relatorios/ (without .html)
     * @param variables Variables to pass to the Thymeleaf template
     * @return PDF as byte array, or null if generation fails
     */
    fun gerarPdf(templateName: String, variables: Map<String, Any>): ByteArray? {
        return try {
            // Create Thymeleaf context with variables
            val context = Context(Locale("pt", "BR")).apply { setVariables(variables) }

            // Render HTML from template
            val html = templateEngine.process("relatorios/$templateName", context)

            // Convert HTML to PDF using Flying Saucer
            val renderer = ITextRenderer()
            renderer.setDocumentFromString(html)
            renderer.layout()

            ByteArrayOutputStream().use { outputStream ->
                renderer.createPDF(outputStream)
                outputStream.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Generates a PDF with additional base URL for resource loading. */
    fun gerarPdfComBaseUrl(
            templateName: String,
            variables: Map<String, Any>,
            baseUrl: String
    ): ByteArray? {
        return try {
            val context = Context(Locale("pt", "BR")).apply { setVariables(variables) }

            val html = templateEngine.process("relatorios/$templateName", context)

            val renderer = ITextRenderer()
            renderer.setDocumentFromString(html, baseUrl)
            renderer.layout()

            ByteArrayOutputStream().use { outputStream ->
                renderer.createPDF(outputStream)
                outputStream.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
