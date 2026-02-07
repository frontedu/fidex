package web.fidex.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ProductFilter;
import web.fidex.repository.jooq.JooqSortUtil;

@Repository
public class ProductRepository {

	private static final Table<?> PRODUCT = DSL.table(DSL.name("product"));
	private static final Field<Long> ID = DSL.field(DSL.name("id"), Long.class);
	private static final Field<String> NAME = DSL.field(DSL.name("name"), String.class);
	private static final Field<Integer> QUANTITY = DSL.field(DSL.name("quantity"), Integer.class);
	private static final Field<Double> PRICE = DSL.field(DSL.name("price"), Double.class);
	private static final Field<String> CREATED_BY = DSL.field(DSL.name("createdby"), String.class);
	private static final Field<String> STATUS = DSL.field(DSL.name("status"), String.class);

	private static final Map<String, Field<?>> SORT_MAPPING = Map.of(
			"id", ID,
			"name", NAME,
			"quantity", QUANTITY,
			"price", PRICE);

	private final DSLContext dsl;

	public ProductRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public Optional<Product> findById(Long id) {
		return dsl.select(ID, NAME, QUANTITY, PRICE, CREATED_BY, STATUS)
				.from(PRODUCT)
				.where(ID.eq(id))
				.fetchOptional(this::mapProduct);
	}

	public List<Product> findByStatus(Status status) {
		return dsl.select(ID, NAME, QUANTITY, PRICE, CREATED_BY, STATUS)
				.from(PRODUCT)
				.where(STATUS.eq(status.name()))
				.fetch(this::mapProduct);
	}

	public List<Product> findByCreatedByAndStatus(String createdBy, Status status) {
		return dsl.select(ID, NAME, QUANTITY, PRICE, CREATED_BY, STATUS)
				.from(PRODUCT)
				.where(CREATED_BY.eq(createdBy).and(STATUS.eq(status.name())))
				.fetch(this::mapProduct);
	}

	public List<Product> findByPriceLessThanEqual(double price) {
		return dsl.select(ID, NAME, QUANTITY, PRICE, CREATED_BY, STATUS)
				.from(PRODUCT)
				.where(PRICE.le(price).and(STATUS.eq(Status.ATIVO.name())))
				.fetch(this::mapProduct);
	}

	public Page<Product> findByCreatedBy(String userId, Pageable pageable) {
		ProductFilter filtro = new ProductFilter();
		filtro.setCreatedBy(userId);
		return buscarComFiltro(filtro, pageable);
	}

	@Transactional
	public Product save(Product product) {
		Status status = product.getStatus() != null ? product.getStatus() : Status.ATIVO;
		Integer quantity = product.getQuantity() != null ? product.getQuantity().intValue() : null;
		Double price = product.getPrice();

		if (product.getId() == null) {
			Long id = dsl.insertInto(PRODUCT)
					.set(NAME, product.getName())
					.set(QUANTITY, quantity)
					.set(PRICE, price)
					.set(CREATED_BY, product.getCreatedBy())
					.set(STATUS, status.name())
					.returningResult(ID)
					.fetchOne(ID);
			product.setId(id);
			product.setStatus(status);
			return product;
		}

		dsl.update(PRODUCT)
				.set(NAME, product.getName())
				.set(QUANTITY, quantity)
				.set(PRICE, price)
				.set(CREATED_BY, product.getCreatedBy())
				.set(STATUS, status.name())
				.where(ID.eq(product.getId()))
				.execute();
		product.setStatus(status);
		return product;
	}

	@Transactional
	public void deleteById(Long id) {
		dsl.deleteFrom(PRODUCT)
				.where(ID.eq(id))
				.execute();
	}

	public Page<Product> buscarComFiltro(ProductFilter filtro, Pageable pageable) {
		Condition condition = DSL.trueCondition();
		if (filtro.getId() != null) {
			condition = condition.and(ID.eq(filtro.getId()));
		}
		if (StringUtils.hasText(filtro.getName())) {
			condition = condition.and(DSL.lower(NAME).like("%" + filtro.getName().toLowerCase() + "%"));
		}
		if (filtro.getPrice() != null) {
			condition = condition.and(PRICE.eq(filtro.getPrice()));
		}
		if (filtro.getQuantity() != null) {
			condition = condition.and(QUANTITY.eq(filtro.getQuantity().intValue()));
		}
		condition = condition.and(STATUS.eq(Status.ATIVO.name()));
		if (StringUtils.hasText(filtro.getCreatedBy())) {
			condition = condition.and(CREATED_BY.eq(filtro.getCreatedBy()));
		}

		List<SortField<?>> sortFields = JooqSortUtil.toSortFields(pageable.getSort(), SORT_MAPPING, ID.desc());

		var query = dsl.select(ID, NAME, QUANTITY, PRICE, CREATED_BY, STATUS)
				.from(PRODUCT)
				.where(condition)
				.orderBy(sortFields);

		List<Product> products;
		if (pageable.isPaged()) {
			products = query.limit(pageable.getPageSize())
					.offset((int) pageable.getOffset())
					.fetch(this::mapProduct);
		} else {
			products = query.fetch(this::mapProduct);
		}

		Long total = dsl.selectCount()
				.from(PRODUCT)
				.where(condition)
				.fetchOne(0, Long.class);

		return new PageImpl<>(products, pageable, total != null ? total : 0L);
	}

	private Product mapProduct(Record record) {
		Product product = new Product();
		product.setId(record.get(ID));
		product.setName(record.get(NAME));
		Integer quantity = record.get(QUANTITY);
		product.setQuantity(quantity != null ? quantity.longValue() : null);
		product.setPrice(record.get(PRICE));
		product.setCreatedBy(record.get(CREATED_BY));
		String status = record.get(STATUS);
		if (status != null) {
			product.setStatus(Status.valueOf(status));
		}
		return product;
	}
}