package web.fidex.repository;

import java.time.LocalDate;
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

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Prize;
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PrizeFilter;
import web.fidex.repository.jooq.JooqSortUtil;

@Repository
public class PrizeRepository {

	private static final Table<?> PRIZE = DSL.table(DSL.name("prize"));
	private static final Table<?> CLIENT = DSL.table(DSL.name("client"));
	private static final Table<?> PRODUCT = DSL.table(DSL.name("product"));

	private static final Field<Long> P_ID = DSL.field(DSL.name("prize", "id"), Long.class);
	private static final Field<LocalDate> P_DATE = DSL.field(DSL.name("prize", "date"), LocalDate.class);
	private static final Field<Long> P_CLIENT_ID = DSL.field(DSL.name("prize", "client_id"), Long.class);
	private static final Field<Long> P_PRODUCT_ID = DSL.field(DSL.name("prize", "product_id"), Long.class);
	private static final Field<String> P_STATUS = DSL.field(DSL.name("prize", "status"), String.class);
	private static final Field<String> P_CREATED_BY = DSL.field(DSL.name("prize", "createdby"), String.class);

	private static final Field<Long> C_ID = DSL.field(DSL.name("client", "id"), Long.class);
	private static final Field<String> C_NAME = DSL.field(DSL.name("client", "name"), String.class);
	private static final Field<String> C_CPF = DSL.field(DSL.name("client", "cpf"), String.class);
	private static final Field<String> C_PHONE = DSL.field(DSL.name("client", "phone"), String.class);
	private static final Field<Integer> C_POINTS = DSL.field(DSL.name("client", "points"), Integer.class);
	private static final Field<String> C_CREATED_BY = DSL.field(DSL.name("client", "createdby"), String.class);
	private static final Field<String> C_STATUS = DSL.field(DSL.name("client", "status"), String.class);

	private static final Field<Long> PR_ID = DSL.field(DSL.name("product", "id"), Long.class);
	private static final Field<String> PR_NAME = DSL.field(DSL.name("product", "name"), String.class);
	private static final Field<Integer> PR_QUANTITY = DSL.field(DSL.name("product", "quantity"), Integer.class);
	private static final Field<Double> PR_PRICE = DSL.field(DSL.name("product", "price"), Double.class);
	private static final Field<String> PR_CREATED_BY = DSL.field(DSL.name("product", "createdby"), String.class);
	private static final Field<String> PR_STATUS = DSL.field(DSL.name("product", "status"), String.class);

	private static final Map<String, Field<?>> SORT_MAPPING = Map.of(
			"id", P_ID,
			"date", P_DATE,
			"client", P_CLIENT_ID,
			"product", P_PRODUCT_ID);

	private final DSLContext dsl;

	public PrizeRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public Optional<Prize> findById(Long id) {
		return dsl.select(P_ID, P_DATE, P_CLIENT_ID, P_PRODUCT_ID, P_STATUS, P_CREATED_BY,
				C_ID, C_NAME, C_CPF, C_PHONE, C_POINTS, C_CREATED_BY, C_STATUS,
				PR_ID, PR_NAME, PR_QUANTITY, PR_PRICE, PR_CREATED_BY, PR_STATUS)
				.from(PRIZE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.leftJoin(PRODUCT).on(P_PRODUCT_ID.eq(PR_ID))
				.where(P_ID.eq(id))
				.fetchOptional(this::mapPrize);
	}

	public List<Prize> findByStatus(Status status) {
		return dsl.select(P_ID, P_DATE, P_CLIENT_ID, P_PRODUCT_ID, P_STATUS, P_CREATED_BY,
				C_ID, C_NAME, C_CPF, C_PHONE, C_POINTS, C_CREATED_BY, C_STATUS,
				PR_ID, PR_NAME, PR_QUANTITY, PR_PRICE, PR_CREATED_BY, PR_STATUS)
				.from(PRIZE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.leftJoin(PRODUCT).on(P_PRODUCT_ID.eq(PR_ID))
				.where(P_STATUS.eq(status.name()))
				.fetch(this::mapPrize);
	}

	public Page<Prize> findByCreatedBy(String userId, Pageable pageable) {
		PrizeFilter filtro = new PrizeFilter();
		filtro.setCreatedBy(userId);
		return buscarComFiltro(filtro, pageable);
	}

	@Transactional
	public Prize save(Prize prize) {
		Status status = prize.getStatus() != null ? prize.getStatus() : Status.ATIVO;
		Long clientId = prize.getClientId();
		if (clientId == null && prize.getClient() != null) {
			clientId = prize.getClient().getId();
			prize.setClientId(clientId);
		}
		Long productId = prize.getProductId();
		if (productId == null && prize.getProduct() != null) {
			productId = prize.getProduct().getId();
			prize.setProductId(productId);
		}
		LocalDate date = prize.getDateValue();

		if (prize.getId() == null) {
			Long id = dsl.insertInto(PRIZE)
					.set(P_DATE, date)
					.set(P_CLIENT_ID, clientId)
					.set(P_PRODUCT_ID, productId)
					.set(P_CREATED_BY, prize.getCreatedBy())
					.set(P_STATUS, status.name())
					.returningResult(P_ID)
					.fetchOne(P_ID);
			prize.setId(id);
			prize.setStatus(status);
			return prize;
		}

		dsl.update(PRIZE)
				.set(P_DATE, date)
				.set(P_CLIENT_ID, clientId)
				.set(P_PRODUCT_ID, productId)
				.set(P_CREATED_BY, prize.getCreatedBy())
				.set(P_STATUS, status.name())
				.where(P_ID.eq(prize.getId()))
				.execute();
		prize.setStatus(status);
		return prize;
	}

	@Transactional
	public void deleteById(Long id) {
		dsl.deleteFrom(PRIZE)
				.where(P_ID.eq(id))
				.execute();
	}

	public Page<Prize> buscarComFiltro(PrizeFilter filtro, Pageable pageable) {
		Condition condition = DSL.trueCondition();
		if (filtro.getId() != null) {
			condition = condition.and(P_ID.eq(filtro.getId()));
		}
		if (StringUtils.hasText(filtro.getClientName())) {
			condition = condition.and(DSL.upper(C_NAME).like("%" + filtro.getClientName().toUpperCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getClientCpf())) {
			condition = condition.and(DSL.upper(C_CPF).like("%" + filtro.getClientCpf().toUpperCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getClientPhone())) {
			condition = condition.and(DSL.upper(C_PHONE).like("%" + filtro.getClientPhone().toUpperCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getProductName())) {
			condition = condition.and(DSL.upper(PR_NAME).like("%" + filtro.getProductName().toUpperCase() + "%"));
		}
		if (filtro.getDate() != null) {
			condition = condition.and(P_DATE.eq(filtro.getDate()));
		}
		condition = condition.and(P_STATUS.eq(Status.ATIVO.name()));
		if (StringUtils.hasText(filtro.getCreatedBy())) {
			condition = condition.and(P_CREATED_BY.eq(filtro.getCreatedBy()));
		}

		List<SortField<?>> sortFields = JooqSortUtil.toSortFields(pageable.getSort(), SORT_MAPPING, P_ID.desc());

		var query = dsl.select(P_ID, P_DATE, P_CLIENT_ID, P_PRODUCT_ID, P_STATUS, P_CREATED_BY,
				C_ID, C_NAME, C_CPF, C_PHONE, C_POINTS, C_CREATED_BY, C_STATUS,
				PR_ID, PR_NAME, PR_QUANTITY, PR_PRICE, PR_CREATED_BY, PR_STATUS)
				.from(PRIZE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.leftJoin(PRODUCT).on(P_PRODUCT_ID.eq(PR_ID))
				.where(condition)
				.orderBy(sortFields);

		List<Prize> prizes;
		if (pageable.isPaged()) {
			prizes = query.limit(pageable.getPageSize())
					.offset((int) pageable.getOffset())
					.fetch(this::mapPrize);
		} else {
			prizes = query.fetch(this::mapPrize);
		}

		Long total = dsl.selectCount()
				.from(PRIZE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.leftJoin(PRODUCT).on(P_PRODUCT_ID.eq(PR_ID))
				.where(condition)
				.fetchOne(0, Long.class);

		return new PageImpl<>(prizes, pageable, total != null ? total : 0L);
	}

	private Prize mapPrize(Record record) {
		Prize prize = new Prize();
		prize.setId(record.get(P_ID));
		prize.setDate(record.get(P_DATE));
		prize.setClientId(record.get(P_CLIENT_ID));
		prize.setProductId(record.get(P_PRODUCT_ID));
		prize.setCreatedBy(record.get(P_CREATED_BY));
		String status = record.get(P_STATUS);
		if (status != null) {
			prize.setStatus(Status.valueOf(status));
		}

		Client client = mapClient(record);
		if (client != null) {
			prize.setClient(client);
		}
		Product product = mapProduct(record);
		if (product != null) {
			prize.setProduct(product);
		}
		return prize;
	}

	private Client mapClient(Record record) {
		Long clientId = record.get(C_ID);
		if (clientId == null) {
			return null;
		}
		Client client = new Client();
		client.setId(clientId);
		client.setName(record.get(C_NAME));
		client.setCpf(record.get(C_CPF));
		client.setPhone(record.get(C_PHONE));
		Integer points = record.get(C_POINTS);
		client.setPoints(points != null ? points.doubleValue() : null);
		client.setCreatedBy(record.get(C_CREATED_BY));
		String status = record.get(C_STATUS);
		if (status != null) {
			client.setStatus(Status.valueOf(status));
		}
		return client;
	}

	private Product mapProduct(Record record) {
		Long productId = record.get(PR_ID);
		if (productId == null) {
			return null;
		}
		Product product = new Product();
		product.setId(productId);
		product.setName(record.get(PR_NAME));
		Integer quantity = record.get(PR_QUANTITY);
		product.setQuantity(quantity != null ? quantity.longValue() : null);
		product.setPrice(record.get(PR_PRICE));
		product.setCreatedBy(record.get(PR_CREATED_BY));
		String status = record.get(PR_STATUS);
		if (status != null) {
			product.setStatus(Status.valueOf(status));
		}
		return product;
	}
}