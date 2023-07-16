package web.fidex.model.filter;

import java.time.LocalDate;

public class PrizeFilter {
    private Long id;
	private String clientName;
    private String clientCpf;
    private String clientPhone;
    private LocalDate date;
    private String productName;
    private String createdBy;

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

    public String getClientCpf() {
        return clientCpf;
    }

    public void setClientCpf(String clientCpf) {
        this.clientCpf = clientCpf;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


}
