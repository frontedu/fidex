package web.controlevacinacao.ajax;

public class NotificacaoAlertify {

	private String mensagem;
	private TipoNotificaoAlertify tipo;
	private int intervalo = 4;

	public NotificacaoAlertify(String mensagem) {
		this.mensagem = mensagem;
		tipo = TipoNotificaoAlertify.MEMSAGEM;
	}

	public NotificacaoAlertify(String mensagem, TipoNotificaoAlertify tipo) {
		this.mensagem = mensagem;
		this.tipo = tipo;
	}
	
	public NotificacaoAlertify(String mensagem, TipoNotificaoAlertify tipo, int intervalo) {
		this.mensagem = mensagem;
		this.tipo = tipo;
		this.intervalo = intervalo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTipo() {
		return tipo.getTipo();
	}

	public void setTipo(TipoNotificaoAlertify tipo) {
		this.tipo = tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = TipoNotificaoAlertify.valueOf(tipo);
	}

	public int getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(int intervalo) {
		this.intervalo = intervalo;
	}

	@Override
	public String toString() {
		return "mensagem: " + mensagem + "\ntipo: " + tipo.getTipo() + "\nintervalo: " + intervalo;
	}

}