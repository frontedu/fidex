package web.fidex.model.filter;

public class ClientFilter {
    
	private Long id;
    private String cpf;
    private String name;
    private String phone;
	private Double points;
	private String createdBy;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
