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

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ClientFilter;
import web.fidex.repository.jooq.JooqSortUtil;

@Repository
public class ClientRepository {

	private static final Table<?> CLIENT = DSL.table(DSL.name("client"));
	private static final Field<Long> ID = DSL.field(DSL.name("id"), Long.class);
	private static final Field<String> CPF = DSL.field(DSL.name("cpf"), String.class);
	private static final Field<String> NAME = DSL.field(DSL.name("name"), String.class);
	private static final Field<String> PHONE = DSL.field(DSL.name("phone"), String.class);
	private static final Field<Integer> POINTS = DSL.field(DSL.name("points"), Integer.class);
	private static final Field<String> CREATED_BY = DSL.field(DSL.name("createdby"), String.class);
	private static final Field<String> STATUS = DSL.field(DSL.name("status"), String.class);

	private static final Map<String, Field<?>> SORT_MAPPING = Map.of(
			"id", ID,
			"name", NAME,
			"points", POINTS);

	private final DSLContext dsl;

	public ClientRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public Optional<Client> findById(Long id) {
		return dsl.select(ID, CPF, NAME, PHONE, POINTS, CREATED_BY, STATUS)
				.from(CLIENT)
				.where(ID.eq(id))
				.fetchOptional(this::mapClient);
	}

	public List<Client> findByStatus(Status status) {
		return dsl.select(ID, CPF, NAME, PHONE, POINTS, CREATED_BY, STATUS)
				.from(CLIENT)
				.where(STATUS.eq(status.name()))
				.fetch(this::mapClient);
	}

	public List<Client> findByCreatedByAndStatus(String createdBy, Status status) {
		return dsl.select(ID, CPF, NAME, PHONE, POINTS, CREATED_BY, STATUS)
				.from(CLIENT)
				.where(CREATED_BY.eq(createdBy).and(STATUS.eq(status.name())))
				.fetch(this::mapClient);
	}

	public Page<Client> findByCreatedBy(String userId, Pageable pageable) {
		ClientFilter filtro = new ClientFilter();
		filtro.setCreatedBy(userId);
		return buscarComFiltro(filtro, pageable);
	}

	@Transactional
	public Client save(Client client) {
		Status status = client.getStatus() != null ? client.getStatus() : Status.ATIVO;
		Integer points = null;
		if (client.getPoints() != null) {
			points = client.getPoints().intValue();
		}

		if (client.getId() == null) {
			Long id = dsl.insertInto(CLIENT)
					.set(CPF, client.getCpfRaw())
					.set(NAME, client.getName())
					.set(PHONE, client.getPhoneRaw())
					.set(POINTS, points)
					.set(CREATED_BY, client.getCreatedBy())
					.set(STATUS, status.name())
					.returningResult(ID)
					.fetchOne(ID);
			client.setId(id);
			client.setStatus(status);
			return client;
		}

		dsl.update(CLIENT)
				.set(CPF, client.getCpfRaw())
				.set(NAME, client.getName())
				.set(PHONE, client.getPhoneRaw())
				.set(POINTS, points)
				.set(CREATED_BY, client.getCreatedBy())
				.set(STATUS, status.name())
				.where(ID.eq(client.getId()))
				.execute();
		client.setStatus(status);
		return client;
	}

	@Transactional
	public void deleteById(Long id) {
		dsl.deleteFrom(CLIENT)
				.where(ID.eq(id))
				.execute();
	}

	public Page<Client> buscarComFiltro(ClientFilter filtro, Pageable pageable) {
		Condition condition = DSL.trueCondition();
		if (filtro.getId() != null) {
			condition = condition.and(ID.eq(filtro.getId()));
		}
		if (StringUtils.hasText(filtro.getName())) {
			condition = condition.and(DSL.lower(NAME).like("%" + filtro.getName().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getCpf())) {
			condition = condition.and(DSL.lower(CPF).like("%" + filtro.getCpf().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(filtro.getPhone())) {
			condition = condition.and(DSL.lower(PHONE).like("%" + filtro.getPhone().toLowerCase() + "%"));
		}
		if (filtro.getPoints() != null) {
			condition = condition.and(POINTS.eq(filtro.getPoints().intValue()));
		}
		condition = condition.and(STATUS.eq(Status.ATIVO.name()));
		if (StringUtils.hasText(filtro.getCreatedBy())) {
			condition = condition.and(CREATED_BY.eq(filtro.getCreatedBy()));
		}

		List<SortField<?>> sortFields = JooqSortUtil.toSortFields(pageable.getSort(), SORT_MAPPING, ID.desc());

		var query = dsl.select(ID, CPF, NAME, PHONE, POINTS, CREATED_BY, STATUS)
				.from(CLIENT)
				.where(condition)
				.orderBy(sortFields);

		List<Client> clients;
		if (pageable.isPaged()) {
			clients = query.limit(pageable.getPageSize())
					.offset((int) pageable.getOffset())
					.fetch(this::mapClient);
		} else {
			clients = query.fetch(this::mapClient);
		}

		Long total = dsl.selectCount()
				.from(CLIENT)
				.where(condition)
				.fetchOne(0, Long.class);

		return new PageImpl<>(clients, pageable, total != null ? total : 0L);
	}

	private Client mapClient(Record record) {
		Client client = new Client();
		client.setId(record.get(ID));
		client.setCpf(record.get(CPF));
		client.setName(record.get(NAME));
		client.setPhone(record.get(PHONE));
		Integer points = record.get(POINTS);
		client.setPoints(points != null ? points.doubleValue() : null);
		client.setCreatedBy(record.get(CREATED_BY));
		String status = record.get(STATUS);
		if (status != null) {
			client.setStatus(Status.valueOf(status));
		}
		return client;
	}
}