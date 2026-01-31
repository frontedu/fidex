package web.fidex.model.fidex_model

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "product")
class Product(
        @Id
        @SequenceGenerator(name = "gerador3", sequenceName = "product_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "gerador3", strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @field:NotBlank(message = "O nome do produto é obrigatório") var name: String? = null,
        @field:Min(0) var price: Double? = null,
        @field:Min(0) var quantity: Long? = null,
        @Enumerated(EnumType.STRING) var status: Status = Status.ATIVO,
        var createdBy: String? = null
) : Serializable {

    val points: Int
        get() = (price ?: 0.0).toInt()

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Product
        return id == that.id
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic fun getSerialversionuid(): Long = serialVersionUID
    }
}
