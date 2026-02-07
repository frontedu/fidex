package web.fidex.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.Papel;
import web.fidex.model.Usuario;

@Repository
public class UsuarioRepository {

	private static final Table<?> USUARIO = DSL.table(DSL.name("usuario"));
	private static final Table<?> USUARIO_PAPEL = DSL.table(DSL.name("usuario_papel"));

	private static final Field<Long> CODIGO = DSL.field(DSL.name("codigo"), Long.class);
	private static final Field<String> NOME = DSL.field(DSL.name("nome"), String.class);
	private static final Field<String> EMAIL = DSL.field(DSL.name("email"), String.class);
	private static final Field<String> NOME_USUARIO = DSL.field(DSL.name("nome_usuario"), String.class);
	private static final Field<String> SENHA = DSL.field(DSL.name("senha"), String.class);
	private static final Field<LocalDate> DATA_NASCIMENTO = DSL.field(DSL.name("data_nascimento"), LocalDate.class);
	private static final Field<Boolean> ATIVO = DSL.field(DSL.name("ativo"), Boolean.class);
	private static final Field<Double> CASHBACK = DSL.field(DSL.name("cashback"), Double.class);

	private static final Field<Long> UP_CODIGO_USUARIO = DSL.field(DSL.name("codigo_usuario"), Long.class);
	private static final Field<Long> UP_CODIGO_PAPEL = DSL.field(DSL.name("codigo_papel"), Long.class);

	private final DSLContext dsl;

	public UsuarioRepository(DSLContext dsl) {
		this.dsl = dsl;
	}

	public Optional<Usuario> findById(Long id) {
		return dsl.select(CODIGO, NOME, EMAIL, NOME_USUARIO, SENHA, DATA_NASCIMENTO, ATIVO, CASHBACK)
				.from(USUARIO)
				.where(CODIGO.eq(id))
				.fetchOptional(this::mapUsuario);
	}

	public Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario) {
		if (nomeUsuario == null) {
			return null;
		}
		return dsl.select(CODIGO, NOME, EMAIL, NOME_USUARIO, SENHA, DATA_NASCIMENTO, ATIVO, CASHBACK)
				.from(USUARIO)
				.where(DSL.lower(NOME_USUARIO).eq(nomeUsuario.toLowerCase()))
				.limit(1)
				.fetchOne(this::mapUsuario);
	}

	@Transactional
	public Usuario save(Usuario usuario) {
		if (usuario.getCodigo() == null) {
			Long id = dsl.insertInto(USUARIO)
					.set(NOME, usuario.getNome())
					.set(EMAIL, usuario.getEmail())
					.set(NOME_USUARIO, usuario.getNomeUsuario())
					.set(SENHA, usuario.getSenha())
					.set(DATA_NASCIMENTO, usuario.getDataNascimento())
					.set(ATIVO, usuario.isAtivo())
					.set(CASHBACK, usuario.getCashback())
					.returningResult(CODIGO)
					.fetchOne(CODIGO);
			usuario.setCodigo(id);
		} else {
			dsl.update(USUARIO)
					.set(NOME, usuario.getNome())
					.set(EMAIL, usuario.getEmail())
					.set(NOME_USUARIO, usuario.getNomeUsuario())
					.set(SENHA, usuario.getSenha())
					.set(DATA_NASCIMENTO, usuario.getDataNascimento())
					.set(ATIVO, usuario.isAtivo())
					.set(CASHBACK, usuario.getCashback())
					.where(CODIGO.eq(usuario.getCodigo()))
					.execute();
		}

		if (usuario.getPapeis() != null && !usuario.getPapeis().isEmpty()) {
			dsl.deleteFrom(USUARIO_PAPEL)
					.where(UP_CODIGO_USUARIO.eq(usuario.getCodigo()))
					.execute();
			for (Papel papel : usuario.getPapeis()) {
				if (papel == null || papel.getCodigo() == null) {
					continue;
				}
				dsl.insertInto(USUARIO_PAPEL)
						.set(UP_CODIGO_USUARIO, usuario.getCodigo())
						.set(UP_CODIGO_PAPEL, papel.getCodigo())
						.execute();
			}
		}

		return usuario;
	}

	@Transactional
	public int updateCashback(String nomeUsuario, Double cashback) {
		return dsl.update(USUARIO)
				.set(CASHBACK, cashback)
				.where(DSL.lower(NOME_USUARIO).eq(nomeUsuario.toLowerCase()))
				.execute();
	}

	private Usuario mapUsuario(Record record) {
		if (record == null) {
			return null;
		}
		Usuario usuario = new Usuario();
		usuario.setCodigo(record.get(CODIGO));
		usuario.setNome(record.get(NOME));
		usuario.setEmail(record.get(EMAIL));
		usuario.setNomeUsuario(record.get(NOME_USUARIO));
		usuario.setSenha(record.get(SENHA));
		usuario.setDataNascimento(record.get(DATA_NASCIMENTO));
		Boolean ativo = record.get(ATIVO);
		usuario.setAtivo(ativo != null && ativo);
		usuario.setCashback(record.get(CASHBACK));
		return usuario;
	}
}