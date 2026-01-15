package web.fidex.model.fidex_model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gerador1", sequenceName = "client_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador1", strategy = GenerationType.SEQUENCE)
	private Long id;
	@NotBlank(message = "O CPF é obrigatório")
	private String cpf;
	@NotBlank(message = "O nome da pessoa é obrigatório")
	private String name;
	@NotBlank(message = "O WhatsApp é obrigatório")
	private String phone;
	private Double points;
	private String createdBy;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		if (cpf == null || cpf.isEmpty())
			return "";
		String clean = cpf.replaceAll("\\D", "");
		if (clean.length() < 11)
			return clean;
		return clean.substring(0, 3) + "." + clean.substring(3, 6) + "." + clean.substring(6, 9) + "-"
				+ clean.substring(9, 11);
	}

	public void setCpf(String cpf) {
		this.cpf = (cpf != null) ? cpf.replaceAll("\\D", "") : null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getPhone() {
		if (phone == null || phone.isEmpty()) {
			return "";
		}
		String clean = phone.replaceAll("\\D", "");
		if (clean.length() < 11)
			return clean;
		return "(" + clean.substring(0, 2) + ") " + clean.substring(2, 3) + " " + clean.substring(3, 7) + "-"
				+ clean.substring(7);
	}

	public void setPhone(String phone) {
		this.phone = (phone != null) ? phone.replaceAll("\\D", "") : null;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(id, other.id);
	}

}
