package web.controlevacinacao.repository.queries.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.model.filter.ClientFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class ClientQueriesImpl implements ClientQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Client> buscarComFiltro(ClientFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Client> criteriaQuery = builder.createQuery(Client.class);
        Root<Client> v = criteriaQuery.from(Client.class);
        TypedQuery<Client> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(v.<Long>get("id"), filtro.getId()));
        }

        if (StringUtils.hasText(filtro.getName())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("name")),
                    "%" + filtro.getName().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getCpf())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("cpf")),
                    "%" + filtro.getCpf().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getPhone())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("phone")),
                    "%" + filtro.getPhone().toLowerCase() + "%"));
        }

        if (filtro.getPoints() != null) {
            predicateList.add(builder.equal(v.<Long>get("points"), filtro.getPoints()));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(v).where(predArray);

        PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Client> Clients = typedQuery.getResultList();
        
        long totalClients = getTotalClients(filtro);
        return new PageImpl<>(Clients, pageable, totalClients); 
    }

    private Long getTotalClients(ClientFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Client> v = criteriaQuery.from(Client.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(v.<Long>get("id"), filtro.getId()));
        }

        if (StringUtils.hasText(filtro.getName())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("name")),
                    "%" + filtro.getName().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getCpf())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("cpf")),
                    "%" + filtro.getCpf().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getPhone())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("phone")),
                    "%" + filtro.getPhone().toLowerCase() + "%"));
        }

        if (filtro.getPoints() != null) {
            predicateList.add(builder.equal(v.<Long>get("points"), filtro.getPoints()));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(v)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

}
