package web.controlevacinacao.pagination;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

public class PageWrapper<T> {

	private static final Logger logger = LoggerFactory.getLogger(PageWrapper.class);

	private Page<T> pagina;
	private UriComponentsBuilder uriBuilder;
	private int maximoPaginasMostrar = 5;
	private int inicio;
	private int fim;

	public PageWrapper(Page<T> pagina, HttpServletRequest request) {
		this.pagina = pagina;
		StringBuffer requestURL = request.getRequestURL();
		String queryString = removeEmptyQueryParams(request.getQueryString());
		String httpURL = requestURL.append(queryString != null ? "?" + queryString : "").toString().replaceAll("\\+",
				"%20");
		logger.debug("PageWrapper criado para a requestURL: {}, queryString: {} e URL final: {}", requestURL,
				queryString, httpURL);
		uriBuilder = UriComponentsBuilder.fromHttpUrl(httpURL);
		definirInicioFimPaginacao();
	}

	private String removeEmptyQueryParams(String queryString) {
		logger.debug("Removendo os parâmetros vazios da queryString: {}", queryString);
		String result = "";
		if (queryString != null) {
			if (!queryString.isBlank()) {
				for (String parameter : queryString.split("&")) {
					if (parameter.indexOf('=') != parameter.length() - 1) { // achou o = antes da ultima posicao, ou
																			// seja, tem valor
						result += (result.isEmpty() ? "" : "&") + parameter;
					}
				}
			}
			logger.debug("queryString sem parâmetros vazios: {}", result);
		}
		return result;
	}

	public List<T> getConteudo() {
		return pagina.getContent();
	}

	public boolean isVazia() {
		return pagina.getContent().isEmpty();
	}

	public int getAtual() {
		return pagina.getNumber();
	}

	public boolean isPrimeira() {
		return pagina.isFirst();
	}

	public boolean isUltima() {
		return pagina.isLast();
	}

	public int getNumeroPaginas() {
		return pagina.getTotalPages();
	}

	public int getInicio() {
		return inicio;
	}

	public int getFim() {
		return fim;
	}

	public String urlAtual() {
		String url = uriBuilder.build(true).encode().toUriString();
		logger.debug("Retornando a URL usada na criação do PageWrapper: {}", url);
		return url;
	}

	public String urlParaPagina(int pagina) {
		logger.debug("Gerando uma nova URL para a pagina {}", pagina);
		String url = uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
		logger.debug("URL gerada: {}", url);
		return url;
	}

	// Se a propriedade nao tiver uma ordenacao na URL atual, insere uma com a ordem
	// ASC na URL.
	// Se a propriedade tiver uma ordenacao na URL atual, inverte essa ordem na URL.
	// Nos dois casos se existir uma ordenacao anterior ela sera substituida por
	// essa.
	// Do jeito que esta nao permite ordenacao por mais de uma coluna.
	public String urlInvertendoDirecaoOrdem(String propriedade) {
		logger.debug("Gerando uma nova URL com a ordem da propriedade {} invertida", propriedade);
		// Nao podemos usar o mesmo uriBuilder ou entao ao carregar uma pagina o sort
		// inserido em
		// uma coluna alteraria o uriBuilder que depois sera usado nos numeros da
		// paginacao o que
		// colocaria a ordenacao nesses numeros mesmo que nao tenhamos clicado para
		// ordenar ainda
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
				.fromUriString(uriBuilder.build(true).encode().toUriString());
		String valorSort = String.format("%s,%s", propriedade, inverterDirecaoOrdem(propriedade));
		String url = uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
		logger.debug("URL gerada: {}", url);
		return url;
	}

	// Caso a propriedade tenha uma ordem o metodo retorna o seu inverso. ASC ->
	// DESC, DESC -> ASC.
	// Caso a propriedade nao tenha uma ordem o metodo retorna ASC.
	private String inverterDirecaoOrdem(String propriedade) {
		logger.debug("Invertendo a direcao da propriedade {}", propriedade);
		String direcao = "asc";
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc";
		}
		logger.debug("Direção final da propriedade {}: {}", propriedade, direcao);
		return direcao;
	}

	public boolean isDescendente(String propriedade) {
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			return Sort.Direction.DESC.equals(order.getDirection());
		}
		return false;
	}

	public boolean isAscendente(String propriedade) {
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		if (order != null) {
			return Sort.Direction.ASC.equals(order.getDirection());
		}
		return false;
	}

	public boolean ordenada(String propriedade) {
		Sort.Order order = (pagina.getSort() != null) ? pagina.getSort().getOrderFor(propriedade) : null;
		return order != null;
	}

	public int getMaximoPaginasMostrar() {
		return maximoPaginasMostrar;
	}

	public void setMaximoPaginasMostrar(int maximoPaginasMostrar) {
		this.maximoPaginasMostrar = maximoPaginasMostrar;
	}

	private void definirInicioFimPaginacao() {
		logger.debug("Gerando a numeração da paginação para a Página {} e o máximo de páginas na numeração {}", pagina,
				maximoPaginasMostrar);
		int metadeMaximoPaginasMostrar = maximoPaginasMostrar / 2;
		int totalDePaginas = pagina.getTotalPages();
		int paginaAtual = pagina.getNumber() + 1;

		logger.debug("totalDePaginas: {}, paginaAtual: {}", totalDePaginas, paginaAtual);

		inicio = 1;
		if (totalDePaginas == 0) {
			fim = 1;
		} else if (totalDePaginas <= maximoPaginasMostrar) {
			fim = totalDePaginas;
		} else {
			if (paginaAtual <= metadeMaximoPaginasMostrar + 1) {
				fim = maximoPaginasMostrar;
			} else {
				inicio = paginaAtual - metadeMaximoPaginasMostrar;
				if (paginaAtual + metadeMaximoPaginasMostrar <= totalDePaginas) {
					fim = paginaAtual + metadeMaximoPaginasMostrar;
				} else {
					fim = totalDePaginas;
					inicio = totalDePaginas - maximoPaginasMostrar + 1;
				}
			}
		}
		logger.debug("Números das páginas serão gerados entre {} e {}", inicio, fim);
	}

}