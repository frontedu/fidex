package web.fidex.model.fidex_model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.io.Serializable
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "usuario")
class Usuario(
        @Id
        @SequenceGenerator(
                name = "gerador55",
                sequenceName = "usuario_codigo_seq",
                allocationSize = 1
        )
        @GeneratedValue(generator = "gerador55", strategy = GenerationType.SEQUENCE)
        var codigo: Long? = null,
        @field:NotBlank(message = "O nome é obrigatório") var nome: String? = null,
        @field:NotBlank(message = "O e-mail é obrigatório")
        @field:Email(message = "O e-mail é inválido")
        var email: String? = null,
        @field:NotBlank(message = "A senha é obrigatória")
        @field:Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        var senha: String? = null,
        @Column(name = "nome_usuario")
        @field:NotBlank(message = "O nome de usuário é obrigatório")
        @field:Size(min = 4, message = "O usuário deve ter no mínimo 4 caracteres")
        var nomeUsuario: String? = null,
        @Column(name = "data_nascimento") var dataNascimento: LocalDate? = null,
        var ativo: Boolean = false,
        var cashback: Double? = null,
        @ManyToMany
        @JoinTable(
                name = "usuario_papel",
                joinColumns = [JoinColumn(name = "codigo_usuario")],
                inverseJoinColumns = [JoinColumn(name = "codigo_papel")]
        )
        @field:Size(min = 1, message = "O usuário deve ter ao menos um papel no sistema")
        var papeis: MutableList<Papel> = mutableListOf()
) : Serializable {

    fun adicionarPapel(papel: Papel) {
        papeis.add(papel)
    }

    fun removerPapel(papel: Papel) {
        papeis.remove(papel)
    }

    override fun hashCode(): Int = Objects.hash(codigo)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Usuario
        return codigo == that.codigo
    }

    override fun toString(): String {
        return "codigo: $codigo\nnome: $nome\nemail: $email\nsenha: $senha\nusuario: $nomeUsuario\ndataNascimento: $dataNascimento\nativo: $ativo"
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
