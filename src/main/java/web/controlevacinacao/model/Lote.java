package web.controlevacinacao.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.validation.IntegerAttributesRelation;
import web.controlevacinacao.validation.util.AttributesRelation;

@Entity
@Table(name = "lote")
@DynamicUpdate
@IntegerAttributesRelation(attribute1 = "nroDosesDoLote", attribute2 = "nroDosesAtual", relation = AttributesRelation.GREATEROREQUAL)
public class Lote implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gerador4", sequenceName = "lote_codigo_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador4", strategy = GenerationType.SEQUENCE)
	private Long codigo;
	@NotNull(message = "A validade do lote é obrigatória")
	private LocalDate validade;
	@NotNull(message = "O nro de doses do lote é obrigatório")
	@Positive(message = "O nro de doses do lote tem que ser maior que 0")
	@Column(name = "nro_doses_do_lote")
	private Integer nroDosesDoLote;
	@NotNull(message = "O nro de doses atual é obrigatório")
	@PositiveOrZero(message = "O nro de doses atual não pode ser negativo")
	@Column(name = "nro_doses_atual")
	private Integer nroDosesAtual;
	@NotNull(message = "A vacina é obrigatória")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_vacina")
	private Vacina vacina;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public Integer getNroDosesDoLote() {
		return nroDosesDoLote;
	}

	public void setNroDosesDoLote(Integer nroDosesDoLote) {
		this.nroDosesDoLote = nroDosesDoLote;
	}

	public Integer getNroDosesAtual() {
		return nroDosesAtual;
	}

	public void setNroDosesAtual(Integer nroDosesAtual) {
		this.nroDosesAtual = nroDosesAtual;
	}

	public Vacina getVacina() {
		return vacina;
	}

	public void setVacina(Vacina vacina) {
		this.vacina = vacina;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
		Lote other = (Lote) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "codigo: " + codigo + "\nvalidade: " + validade + "\nnroDosesDoLote: " + nroDosesDoLote
				+ "\nnroDosesAtual: " + nroDosesAtual;
	}

	

}
