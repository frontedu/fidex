package web.fidex.model.fidex_model

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import java.io.Serializable
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(name = "purchase")
class Purchase(
        @Id
        @SequenceGenerator(name = "gerador4", sequenceName = "purchase_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "gerador4", strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @field:Min(0) var price: Double? = null,
        var date: LocalDate? = null,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "client_id")
        var client: Client? = null,
        @Enumerated(EnumType.STRING) var status: Status = Status.ATIVO,
        var createdBy: String? = null,
        var points: Int? = null
) : Serializable {

    // Formatted versions for Thymeleaf
    fun getFormattedPrice(): String = DecimalFormat("R$ #,##0.00").format(price ?: 0.0)
    fun getFormattedDate(): String = date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""

    fun calculatePoints(): Int = points ?: (((price ?: 0.0) * 5) / 100).toInt()

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return id == (other as Purchase).id
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
