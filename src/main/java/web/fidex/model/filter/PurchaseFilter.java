package web.fidex.model.filter;
import java.time.LocalDate;

public class PurchaseFilter {
    private Long id;
    private String clientName;
    private Double price;
    private LocalDate date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
    
    public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "PurchaseFilter [id=" + id + ", clientName=" + clientName + ", price=" + price + ", date=" + date + "]";
    }



}

