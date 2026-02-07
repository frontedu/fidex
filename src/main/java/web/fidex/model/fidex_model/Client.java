package web.fidex.model.fidex_model;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Pattern NON_DIGITS = Pattern.compile("\\D");

	@Id
	private Long id;
	@NotBlank(message = "O CPF e obrigatorio")
	private String cpf;
	@NotBlank(message = "O nome da pessoa e obrigatorio")
	private String name;
	@NotBlank(message = "O WhatsApp e obrigatorio")
	private String phone;
	private Double points;
	private String createdBy;
	private Status status = Status.ATIVO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCpf() {
		String clean = onlyDigits(cpf);
		if (clean == null || clean.isEmpty()) {
			return "";
		}
		if (clean.length() < 11) {
			return clean;
		}
		return clean.substring(0, 3) + "." + clean.substring(3, 6) + "." + clean.substring(6, 9) + "-"
				+ clean.substring(9, 11);
	}

	public String getCpfRaw() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = onlyDigits(cpf);
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
		String clean = onlyDigits(phone);
		if (clean == null || clean.isEmpty()) {
			return "";
		}
		if (clean.length() < 11) {
			return clean;
		}
		return "(" + clean.substring(0, 2) + ") " + clean.substring(2, 3) + " " + clean.substring(3, 7) + "-"
				+ clean.substring(7);
	}

	public String getPhoneRaw() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = onlyDigits(phone);
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

	private static String onlyDigits(String value) {
		if (value == null) {
			return null;
		}
		return NON_DIGITS.matcher(value).replaceAll("");
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
