package web.controlevacinacao.model.filter;

public class ProductFilter {
    
	private Long id;
    private String name;
    private Double price;
    private Long quantity;
    private Double points;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

    public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

    @Override
    public String toString() {
        return "ProductFilter [id=" + id + ", name=" + name + ", price=" + price + ", quantity=" + quantity
                + ", points=" + points + "]";
    }

    

}