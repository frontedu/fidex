package web.fidex.repository.queries.product;

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
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ProductFilter;
import web.fidex.repository.pagination.PaginacaoUtil;

public class ProductQueriesImpl implements ProductQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Product> buscarComFiltro(ProductFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
        Root<Product> v = criteriaQuery.from(Product.class);
        TypedQuery<Product> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(v.<Long>get("id"), filtro.getId()));
        }

        if (StringUtils.hasText(filtro.getName())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("name")),
                    "%" + filtro.getName().toLowerCase() + "%"));
        }

        if (filtro.getPrice() != null) {
            predicateList.add(builder.equal(v.<Long>get("price"), filtro.getPrice()));
        }

        if (filtro.getQuantity() != null) {
            predicateList.add(builder.equal(v.<Long>get("quantity"), filtro.getQuantity()));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(v).where(predArray);

        PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Product> Products = typedQuery.getResultList();
        
        long totalProducts = getTotalProducts(filtro);
        return new PageImpl<>(Products, pageable, totalProducts); 
    }

    private Long getTotalProducts(ProductFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Product> v = criteriaQuery.from(Product.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getId() != null) {
            predicateList.add(builder.equal(v.<Long>get("id"), filtro.getId()));
        }

        if (StringUtils.hasText(filtro.getName())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("name")),
                    "%" + filtro.getName().toLowerCase() + "%"));
        }

        if (filtro.getPrice() != null) {
            predicateList.add(builder.equal(v.<Long>get("price"), filtro.getPrice()));
        }

        if (filtro.getQuantity() != null) {
            predicateList.add(builder.equal(v.<Long>get("quantity"), filtro.getQuantity()));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(v)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

}
