package web.fidex.model.fidex_model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import jakarta.validation.constraints.Min;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "purchase")
public class Purchase implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final ThreadLocal<DecimalFormat> PRICE_FORMATTER = ThreadLocal.withInitial(
			() -> new DecimalFormat("R$ #,##0.00"));

	@Id
	private Long id;
	@Min(value = 0)
	private Double price;
	private LocalDate date;
	private Long clientId;
	@Transient
	private Client client;
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

	private Integer points;

	public String getPrice() {
		if (price == null) {
			return "R$ 0,00";
		}
		return PRICE_FORMATTER.get().format(price);
	}

	public Double getPriceValue() {
		return price;
	}

	public int getPoints() {
		if (points != null) {
			return points;
		}
		if (price == null) {
			return 0;
		}
		return (int) (price * 5 / 100);
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
		if (client != null && client.getId() != null) {
			this.clientId = client.getId();
		}
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Integer getPointsValue() {
		return points;
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
		Purchase other = (Purchase) obj;
		return Objects.equals(id, other.id);
	}

}
