package web.fidex.model.fidex_model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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

@Entity
@Table(name = "prize")
public class Prize implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "gerador2", sequenceName = "prize_id_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador2", strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	private Client client;
	private LocalDate date;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVO;
	private String createdBy;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return date.format(formatter);
	}

	public void setDate(LocalDate date) {
		this.date = date;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prize other = (Prize) obj;
		return Objects.equals(id, other.id);
	}

}
