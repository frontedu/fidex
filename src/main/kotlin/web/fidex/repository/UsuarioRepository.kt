package web.fidex.repository

import org.springframework.data.jpa.repository.JpaRepository
import web.fidex.model.fidex_model.Usuario

interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByNomeUsuarioIgnoreCase(nomeUsuario: String): Usuario?
}
