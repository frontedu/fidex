package web.controlevacinacao.repository.pagination;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public class PaginacaoUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PaginacaoUtil.class);

	public static void prepararIntervalo(TypedQuery<?> typedQuery, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		logger.debug("Filtrando a página {}, registros entre {} e {}", paginaAtual, primeiroRegistro, primeiroRegistro + totalRegistrosPorPagina);
		typedQuery.setFirstResult(primeiroRegistro);
		typedQuery.setMaxResults(totalRegistrosPorPagina);
	}
	
	public static void prepararOrdem(Root<?> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder builder, Pageable pageable) {
		String atributo;
		Sort sort = pageable.getSort();
		Order order;
		List<Order> ordenacoes = new ArrayList<>();
		if (sort != null && !sort.isEmpty()) {
			for (Sort.Order o : sort) {
				logger.debug("Ordenando o resultado da pesquisa por {}, {}", o.getProperty(), o.getDirection());
			    atributo = o.getProperty();
			    order = o.isAscending() ? builder.asc(root.get(atributo)) : builder.desc(root.get(atributo));
			    ordenacoes.add(order);
			}
		}
		criteriaQuery.orderBy(ordenacoes);
	}
	
	// public static long getTotalRegistros(Root<?> root, Predicate[] predicateArray, CriteriaBuilder builder, EntityManager manager) {
	// 	logger.debug("Calculando o total de registros que o filtro retornará.");
	// 	CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
	// 	criteriaQuery.select(builder.count(criteriaQuery.from(root.getJavaType())));
	// 	criteriaQuery.where(predicateArray);
	// 	TypedQuery<Long> typedQueryTotal = manager.createQuery(criteriaQuery);
	// 	long totalRegistros = typedQueryTotal.getSingleResult();
	// 	logger.debug("O filtro retornará {} registros.", totalRegistros);	
	// 	return totalRegistros;
	// }


}
