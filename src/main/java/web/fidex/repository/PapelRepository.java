package web.fidex.repository;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import web.fidex.model.Papel;

@Repository
public class PapelRepository {

	private static final Table<?> PAPEL = DSL.table(DSL.name("papel"));
	private static final Field<Long> CODIGO = DSL.field(DSL.name("codigo"), Long.class);
	private static final Field<String> NOME = DSL.field(DSL.name("nome"), String.class);

	private final DSLContext dsl;

	public PapelRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public List<Papel> findAll() {
		return dsl.select(CODIGO, NOME)
				.from(PAPEL)
				.fetch(this::mapPapel);
	}

	public Optional<Papel> findById(Long id) {
		return dsl.select(CODIGO, NOME)
				.from(PAPEL)
				.where(CODIGO.eq(id))
				.fetchOptional(this::mapPapel);
	}

	private Papel mapPapel(Record record) {
		Papel papel = new Papel();
		papel.setCodigo(record.get(CODIGO));
		papel.setNome(record.get(NOME));
		return papel;
	}
}