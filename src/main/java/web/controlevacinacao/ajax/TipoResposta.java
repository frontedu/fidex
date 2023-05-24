package web.controlevacinacao.ajax;

public enum TipoResposta {

	FRAGMENTO("Fragmento de página"),
	NOTIFICACAO("Notificação"),
	FRAGMENTO_E_NOTIFICACAO("Fragmento de página e Notificação");
	
	private String descricao;
	
	private TipoResposta(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}