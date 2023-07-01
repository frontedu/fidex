package web.controlevacinacao.model.filter;

public class ClientFilter {
    
	private Long id;
    private String cpf;
    private String name;
    private String phone;
	private Double points;
	
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

    @Override
    public String toString() {
        return "ClientFilter [id=" + id + ", cpf=" + cpf + ", name=" + name + ", phone=" + phone
                + ", points=" + points + "]";
    }

    

}
