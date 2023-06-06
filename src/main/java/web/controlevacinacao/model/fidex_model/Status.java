package web.controlevacinacao.model.fidex_model;

public enum Status {

	ATIVO("ativo"),
	INATIVO("inativo");
	
	private String descricao;
	
	private Status(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
