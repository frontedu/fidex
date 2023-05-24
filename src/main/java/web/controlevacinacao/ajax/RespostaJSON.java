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

	public String getMensagem() {
		return notificacao.getMensagem();
	}

	public void setMensagem(String mensagem) {
		notificacao.setMensagem(mensagem);
	}

	public String getTipoMensagem() {
		return notificacao.getTipo();
	}

	public void setTipoMensagem(String tipoMensagem) {
		notificacao.setTipo(tipoMensagem);
	}
	
	public void setTipoMensagem(TipoNotificaoAlertify tipo) {
		notificacao.setTipo(tipo);
	}

	public int getIntervalo() {
		return notificacao.getIntervalo();
	}

	public void setIntervalo(int intervalo) {
		notificacao.setIntervalo(intervalo);
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