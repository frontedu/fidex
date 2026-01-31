package web.fidex.model.fidex_model

import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Entity
@Table(name = "prize")
class Prize(
        @Id
        @SequenceGenerator(name = "gerador2", sequenceName = "prize_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "gerador2", strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "client_id")
        var client: Client? = null,
        var date: LocalDate? = null,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id")
        var product: Product? = null,
        @Enumerated(EnumType.STRING) var status: Status = Status.ATIVO,
        var createdBy: String? = null
) : Serializable {

    @get:JvmName("getFormattedDate")
    val formattedDate: String
        get() = date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""

    // Java-style getter for compatibility with Thymeleaf
    fun getDate(): String = formattedDate

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Prize
        return id == that.id
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
