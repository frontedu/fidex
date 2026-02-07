package web.fidex.model.fidex_model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "prize")
public class Prize implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Id
	private Long id;

	private Long clientId;
	@Transient
	private Client client;
	private LocalDate date;
	private Long productId;
	@Transient
	private Product product;
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
		if (date == null) {
			return "";
		}
		return date.format(DATE_FORMATTER);
	}

	public LocalDate getDateValue() {
		return date;
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
		if (client != null && client.getId() != null) {
			this.clientId = client.getId();
		}
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
		if (product != null && product.getId() != null) {
			this.productId = product.getId();
		}
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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
