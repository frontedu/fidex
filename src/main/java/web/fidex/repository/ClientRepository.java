package web.fidex.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Status;
import web.fidex.repository.queries.client.ClientQueries;

public interface ClientRepository extends JpaRepository<Client, Long>, ClientQueries {
    List<Client> findByStatus(Status status);
    Page<Client> findByCreatedBy(String userId, Pageable pageable);

}
