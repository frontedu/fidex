package web.controlevacinacao.model.filter;

import java.time.LocalDate;

public class PrizeFilter {
    private Long id;
	private String clientName;
    private LocalDate date;
    private String productName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "PrizeFilter [id=" + id + ", clientName=" + clientName + ", date=" + date + ", productName="
                + productName + "]";
    }


}
