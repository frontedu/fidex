package web.fidex.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import web.fidex.model.fidex_model.Client
import web.fidex.model.fidex_model.Status
import web.fidex.repository.queries.client.ClientQueries

interface ClientRepository : JpaRepository<Client, Long>, ClientQueries {
    fun findByStatus(status: Status): List<Client>
    fun findByCreatedBy(userId: String, pageable: Pageable): Page<Client>
}
