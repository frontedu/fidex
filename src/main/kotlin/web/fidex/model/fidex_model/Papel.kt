package web.fidex.model.fidex_model

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "papel")
class Papel(
        @Id
        @SequenceGenerator(name = "gerador", sequenceName = "papel_codigo_seq", allocationSize = 1)
        @GeneratedValue(generator = "gerador", strategy = GenerationType.SEQUENCE)
        var codigo: Long? = null,
        var nome: String? = null
) : Serializable {

    override fun hashCode(): Int = Objects.hash(codigo)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Papel
        return codigo == that.codigo
    }

    override fun toString(): String = "codigo: $codigo\nnome: $nome"

    companion object {
        private const val serialVersionUID = 3377158425416402634L
    }
}
