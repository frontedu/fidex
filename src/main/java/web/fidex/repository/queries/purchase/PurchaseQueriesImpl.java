package web.fidex.repository.queries.purchase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PurchaseFilter;
import web.fidex.repository.pagination.PaginacaoUtil;

public class PurchaseQueriesImpl implements PurchaseQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Purchase> buscarComFiltro(PurchaseFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Purchase> criteriaQuery = builder.createQuery(Purchase.class);
        Root<Purchase> p = criteriaQuery.from(Purchase.class);
        TypedQuery<Purchase> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(p.<Long>get("price"), filtro.getId()));
        }

        if (filtro.getClientName() != null) {
            predicateList.add(builder.like(
                builder.upper(p.<Client>get("client").<String>get("name")), "%" + 
                filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getPrice() != null) {
            predicateList.add(builder.equal(p.<Long>get("price"), filtro.getPrice()));
        }

        if (filtro.getDate() != null) {
            predicateList.add(builder.equal(
                 p.<LocalDate>get("date"), filtro.getDate()));
        }

        predicateList.add(builder.equal(p.<Status>get("status"), Status.ATIVO));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        predicateList.add(builder.equal(p.get("createdBy"), userId));


        p.fetch("client");

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(p).where(predArray);
        PaginacaoUtil.prepararOrdem(p, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);
        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Purchase> Purchases = typedQuery.getResultList();

        long totalPurchases = getTotalPurchases(filtro);

        Page<Purchase> page = new PageImpl<>(Purchases, pageable, totalPurchases);
        return page;
    }

    private Long getTotalPurchases(PurchaseFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Purchase> p = criteriaQuery.from(Purchase.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(p.<Long>get("id"), filtro.getId()));
        }

        if (filtro.getClientName() != null) {
            predicateList.add(builder.like(
                builder.upper(p.<Client>get("client").<String>get("name")), "%" + 
                filtro.getClientName().toUpperCase() + "%"));
        }

        if (filtro.getPrice() != null) {
            predicateList.add(builder.equal(p.<Long>get("price"), filtro.getPrice()));
        }

        if (filtro.getDate() != null) {
            predicateList.add(builder.equal(
                 p.<LocalDate>get("date"), filtro.getDate()));
        }

        predicateList.add(builder.equal(p.<Status>get("status"), Status.ATIVO));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        predicateList.add(builder.equal(p.get("createdBy"), userId));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(p)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

}
