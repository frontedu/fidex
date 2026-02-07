package web.fidex.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import web.fidex.service.NomeUsuarioUnicoService;
import web.fidex.validation.UniqueValueAttribute;

@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long codigo;
	@NotBlank(message = "O nome Ã© obrigatÃ³rio")
	private String nome;
	@NotBlank(message = "O e-mail Ã© obrigatÃ³rio")
	@jakarta.validation.constraints.Email(message = "O e-mail Ã© invÃ¡lido")
	private String email;
	@NotBlank(message = "A senha Ã© obrigatÃ³ria")
	@Size(min = 8, message = "A senha deve ter no mÃ­nimo 8 caracteres")
	private String senha;
	@NotBlank(message = "O nome de usuÃ¡rio Ã© obrigatÃ³rio")
	@Size(min = 4, message = "O usuÃ¡rio deve ter no mÃ­nimo 4 caracteres")
	private String nomeUsuario;
	private LocalDate dataNascimento;
	private boolean ativo;

	private Double cashback;

	public Double getCashback() {
		return cashback;
	}

	public void setCashback(Double cashback) {
		this.cashback = cashback;
	}

	@Size(min = 1, message = "O usuÃ¡rio deve ter ao menos um papel no sistema")
	@Transient
	private List<Papel> papeis = new ArrayList<>();

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public void adicionarPapel(Papel papel) {
		papeis.add(papel);
	}

	public void removerPapel(Papel papel) {
		papeis.remove(papel);
	}

	public List<Papel> getPapeis() {
		return papeis;
	}

	public void setPapeis(List<Papel> papeis) {
		this.papeis = papeis;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "codigo: " + codigo + "\nnome: " + nome + "\nemail: " + email + "\nsenha: " + senha + "\nusuario: "
				+ nomeUsuario + "\ndataNascimento: " + dataNascimento + "\nativo: " + ativo;
	}

}