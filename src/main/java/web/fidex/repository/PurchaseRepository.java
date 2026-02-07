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
import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PurchaseFilter;
import web.fidex.repository.jooq.JooqSortUtil;

@Repository
public class PurchaseRepository {

	private static final Table<?> PURCHASE = DSL.table(DSL.name("purchase"));
	private static final Table<?> CLIENT = DSL.table(DSL.name("client"));

	private static final Field<Long> P_ID = DSL.field(DSL.name("purchase", "id"), Long.class);
	private static final Field<LocalDate> P_DATE = DSL.field(DSL.name("purchase", "date"), LocalDate.class);
	private static final Field<Long> P_CLIENT_ID = DSL.field(DSL.name("purchase", "client_id"), Long.class);
	private static final Field<Double> P_PRICE = DSL.field(DSL.name("purchase", "price"), Double.class);
	private static final Field<Integer> P_POINTS = DSL.field(DSL.name("purchase", "points"), Integer.class);
	private static final Field<String> P_STATUS = DSL.field(DSL.name("purchase", "status"), String.class);
	private static final Field<String> P_CREATED_BY = DSL.field(DSL.name("purchase", "createdby"), String.class);

	private static final Field<Long> C_ID = DSL.field(DSL.name("client", "id"), Long.class);
	private static final Field<String> C_NAME = DSL.field(DSL.name("client", "name"), String.class);
	private static final Field<String> C_CPF = DSL.field(DSL.name("client", "cpf"), String.class);
	private static final Field<String> C_PHONE = DSL.field(DSL.name("client", "phone"), String.class);
	private static final Field<Integer> C_POINTS = DSL.field(DSL.name("client", "points"), Integer.class);
	private static final Field<String> C_CREATED_BY = DSL.field(DSL.name("client", "createdby"), String.class);
	private static final Field<String> C_STATUS = DSL.field(DSL.name("client", "status"), String.class);

	private static final Map<String, Field<?>> SORT_MAPPING = Map.of(
			"id", P_ID,
			"price", P_PRICE,
			"client", P_CLIENT_ID,
			"date", P_DATE);

	private final DSLContext dsl;

	public PurchaseRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public Optional<Purchase> findById(Long id) {
		return dsl.select(P_ID, P_DATE, P_CLIENT_ID, P_PRICE, P_POINTS, P_STATUS, P_CREATED_BY,
				C_ID, C_NAME, C_CPF, C_PHONE, C_POINTS, C_CREATED_BY, C_STATUS)
				.from(PURCHASE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.where(P_ID.eq(id))
				.fetchOptional(this::mapPurchaseWithClient);
	}

	public Page<Purchase> findByCreatedBy(String userId, Pageable pageable) {
		PurchaseFilter filtro = new PurchaseFilter();
		filtro.setCreatedBy(userId);
		return buscarComFiltro(filtro, pageable);
	}

	@Transactional
	public Purchase save(Purchase purchase) {
		Status status = purchase.getStatus() != null ? purchase.getStatus() : Status.ATIVO;
		Long clientId = purchase.getClientId();
		if (clientId == null && purchase.getClient() != null) {
			clientId = purchase.getClient().getId();
			purchase.setClientId(clientId);
		}
		Integer points = purchase.getPointsValue();
		if (points == null) {
			points = purchase.getPoints();
		}
		Double price = purchase.getPriceValue();
		LocalDate date = purchase.getDateValue();

		if (purchase.getId() == null) {
			Long id = dsl.insertInto(PURCHASE)
					.set(P_DATE, date)
					.set(P_CLIENT_ID, clientId)
					.set(P_CREATED_BY, purchase.getCreatedBy())
					.set(P_PRICE, price)
					.set(P_STATUS, status.name())
					.set(P_POINTS, points)
					.returningResult(P_ID)
					.fetchOne(P_ID);
			purchase.setId(id);
			purchase.setStatus(status);
			return purchase;
		}

		dsl.update(PURCHASE)
				.set(P_DATE, date)
				.set(P_CLIENT_ID, clientId)
				.set(P_CREATED_BY, purchase.getCreatedBy())
				.set(P_PRICE, price)
				.set(P_STATUS, status.name())
				.set(P_POINTS, points)
				.where(P_ID.eq(purchase.getId()))
				.execute();
		purchase.setStatus(status);
		return purchase;
	}

	@Transactional
	public void deleteById(Long id) {
		dsl.deleteFrom(PURCHASE)
				.where(P_ID.eq(id))
				.execute();
	}

	public Page<Purchase> buscarComFiltro(PurchaseFilter filtro, Pageable pageable) {
		Condition condition = DSL.trueCondition();
		if (filtro.getId() != null) {
			condition = condition.and(P_ID.eq(filtro.getId()));
		}
		if (StringUtils.hasText(filtro.getClientName())) {
			condition = condition.and(DSL.upper(C_NAME).like("%" + filtro.getClientName().toUpperCase() + "%"));
		}
		if (filtro.getPrice() != null) {
			condition = condition.and(P_PRICE.eq(filtro.getPrice()));
		}
		if (filtro.getDate() != null) {
			condition = condition.and(P_DATE.eq(filtro.getDate()));
		}
		condition = condition.and(P_STATUS.eq(Status.ATIVO.name()));
		if (StringUtils.hasText(filtro.getCreatedBy())) {
			condition = condition.and(P_CREATED_BY.eq(filtro.getCreatedBy()));
		}

		List<SortField<?>> sortFields = JooqSortUtil.toSortFields(pageable.getSort(), SORT_MAPPING, P_ID.desc());

		var query = dsl.select(P_ID, P_DATE, P_CLIENT_ID, P_PRICE, P_POINTS, P_STATUS, P_CREATED_BY,
					C_ID, C_NAME, C_CPF, C_PHONE, C_POINTS, C_CREATED_BY, C_STATUS)
				.from(PURCHASE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.where(condition)
				.orderBy(sortFields);

		List<Purchase> purchases;
		if (pageable.isPaged()) {
			purchases = query.limit(pageable.getPageSize())
					.offset((int) pageable.getOffset())
					.fetch(this::mapPurchaseWithClient);
		} else {
			purchases = query.fetch(this::mapPurchaseWithClient);
		}

		Long total = dsl.selectCount()
				.from(PURCHASE)
				.leftJoin(CLIENT).on(P_CLIENT_ID.eq(C_ID))
				.where(condition)
				.fetchOne(0, Long.class);

		return new PageImpl<>(purchases, pageable, total != null ? total : 0L);
	}

	private Purchase mapPurchaseWithClient(Record record) {
		Purchase purchase = new Purchase();
		purchase.setId(record.get(P_ID));
		purchase.setDate(record.get(P_DATE));
		purchase.setClientId(record.get(P_CLIENT_ID));
		purchase.setPrice(record.get(P_PRICE));
		purchase.setPoints(record.get(P_POINTS));
		purchase.setCreatedBy(record.get(P_CREATED_BY));
		String status = record.get(P_STATUS);
		if (status != null) {
			purchase.setStatus(Status.valueOf(status));
		}

		Client client = mapClient(record);
		if (client != null) {
			purchase.setClient(client);
		}
		return purchase;
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
}