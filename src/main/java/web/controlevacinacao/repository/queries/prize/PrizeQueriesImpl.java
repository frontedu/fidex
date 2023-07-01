package web.controlevacinacao.repository.queries.prize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.fidex_model.Prize;
import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.model.filter.PrizeFilter;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class PrizeQueriesImpl implements PrizeQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Prize> buscarComFiltro(PrizeFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Prize> criteriaQuery = builder.createQuery(Prize.class);
        Root<Prize> p = criteriaQuery.from(Prize.class);
        TypedQuery<Prize> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(p.<Long>get("price"), filtro.getId()));
        }

        if (filtro.getClientName() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("name")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getClientCpf() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("cpf")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getClientPhone() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("phone")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getDate() != null) {
            predicateList.add(builder.equal(
                    p.<LocalDate>get("date"), filtro.getDate()));
        }

        predicateList.add(builder.equal(p.<Status>get("status"), Status.ATIVO));

        p.fetch("client");
        p.fetch("product");

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(p).where(predArray);
        PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Prize> Prizes = typedQuery.getResultList();

        long totalPrizes = getTotalPrizes(filtro);

        Page<Prize> page = new PageImpl<>(Prizes, pageable, totalPrizes);
        return page;
    }

    private Long getTotalPrizes(PrizeFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Prize> p = criteriaQuery.from(Prize.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(p.<Long>get("id"), filtro.getId()));
        }

        if (filtro.getClientName() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("name")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getClientCpf() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("cpf")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getClientPhone() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Client>get("client").<String>get("phone")), "%" +
                            filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getProductName() != null) {
            predicateList.add(builder.like(
                    builder.upper(p.<Product>get("product").<String>get("name")), "%" +
                            filtro.getProductName().toUpperCase() + "%"));
        }

        if (filtro.getDate() != null) {
            predicateList.add(builder.equal(
                    p.<LocalDate>get("date"), filtro.getDate()));
        }

        predicateList.add(builder.equal(p.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(p)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

}
