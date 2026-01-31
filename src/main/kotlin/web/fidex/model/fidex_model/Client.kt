package web.fidex.model.fidex_model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "client")
class Client(
        @Id
        @SequenceGenerator(name = "gerador1", sequenceName = "client_id_seq", allocationSize = 1)
        @GeneratedValue(generator = "gerador1", strategy = GenerationType.SEQUENCE)
        var id: Long? = null,
        @field:NotBlank(message = "O CPF é obrigatório") var cpf: String? = null,
        @field:NotBlank(message = "O nome da pessoa é obrigatório") var name: String? = null,
        @field:NotBlank(message = "O WhatsApp é obrigatório") var phone: String? = null,
        var points: Double? = null,
        var createdBy: String? = null,
        @Enumerated(EnumType.STRING) var status: Status = Status.ATIVO
) : Serializable {

    @get:JvmName("getMaskedCpf")
    val maskedCpf: String
        get() {
            if (cpf.isNullOrEmpty()) return ""
            val clean = cpf!!.replace(Regex("\\D"), "")
            if (clean.length < 11) return clean
            return "${clean.substring(0, 3)}.${clean.substring(3, 6)}.${clean.substring(6, 9)}-${clean.substring(9, 11)}"
        }

    @get:JvmName("getMaskedPhone")
    val maskedPhone: String
        get() {
            if (phone.isNullOrEmpty()) return ""
            val clean = phone!!.replace(Regex("\\D"), "")
            if (clean.length < 11) return clean
            return "(${clean.substring(0, 2)}) ${clean.substring(2, 3)} ${clean.substring(3, 7)}-${clean.substring(7)}"
        }

    // Compatibilidade com Java/Thymeleaf que espera getCpf() e getPhone() com máscaras
    @JvmName("getCpfWithMask") fun getCpf(): String = maskedCpf

    @JvmName("getPhoneWithMask") fun getPhone(): String = maskedPhone

    // Setter customizado para CPF para remover caracteres não numéricos
    @JvmName("setCpfCustom")
    fun setCpf(cpf: String?) {
        this.cpf = cpf?.replace(Regex("\\D"), "")
    }

    // Setter customizado para Telefone para remover caracteres não numéricos
    @JvmName("setPhoneCustom")
    fun setPhone(phone: String?) {
        this.phone = phone?.replace(Regex("\\D"), "")
    }

    override fun hashCode(): Int = Objects.hash(id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as Client
        return id == that.id
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}
