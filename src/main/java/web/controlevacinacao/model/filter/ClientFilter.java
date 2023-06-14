package web.controlevacinacao.model.filter;

public class ClientFilter {
    
	private Long id;
    private String cpf;
    private String name;
    private String phone_number;
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

    public String getPhoneNumber() {
		return phone_number;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phone_number = phoneNumber;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

    @Override
    public String toString() {
        return "ClientFilter [id=" + id + ", cpf=" + cpf + ", name=" + name + ", phone_number=" + phone_number
                + ", points=" + points + "]";
    }

    

}
