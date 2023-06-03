package web.controlevacinacao.ajax;

public class RespostaJSON {

	private TipoResposta tipoResposta;
	private String htmlFragmento;
	private NotificacaoAlertify notificacao;

	public RespostaJSON(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}
	
	public RespostaJSON(String tipoResposta) {
		this.tipoResposta = TipoResposta.valueOf(tipoResposta);
	}
	
	public TipoResposta getTipoResposta() {
		return tipoResposta;
	}

	public void setTipoResposta(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	public String getHtmlFragmento() {
		return htmlFragmento;
	}

	public void setHtmlFragmento(String htmlFragmento) {
		this.htmlFragmento = htmlFragmento;
	}

	public NotificacaoAlertify getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(NotificacaoAlertify notificacao) {
		this.notificacao = notificacao;
	}

	@Override
	public String toString() {
		return "tipoResposta: " + tipoResposta.getDescricao() + "\nhtmlFragmento: " + htmlFragmento + "\nnotificacao: " + notificacao;
	}

}