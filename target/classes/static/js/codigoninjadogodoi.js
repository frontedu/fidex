window.addEventListener("load", function() {

	atualizarCodigoNaPagina();

	function atualizarCodigoNaPagina() {
		configurarAlertifyJS();
		prepararComponentesData();
		prepararConfirmacoes();
		mostrarNotificacaoFromHTML();
		prepararEnvioFormulariosComAJAX();
	}


	//Configuracao do AlertifyJS
	function configurarAlertifyJS() {
		alertify.defaults.transition = "slide";
		alertify.defaults.theme.ok = "btn btn-primary";
		alertify.defaults.theme.cancel = "btn btn-danger";
		alertify.defaults.theme.input = "form-control";
		alertify.set('notifier', 'position', 'top-right');
	}
	
	
	//Busca campos input com a classe componentedata na pagina atual e liga-os com
	// o componente flatpickr.
	function prepararComponentesData() {
    	let inputsData = document.querySelectorAll(".componentedata");
    	inputsData.forEach(function(input) {
    		flatpickr(input, {
    	    	"locale": "pt",
    	    	"dateFormat": "d/m/Y" 
    		});
    	});
    }
    
    //Busca por botoes com a classe confirmacao e usa o AlertifyJS para pedir uma 
    // confirmacao do usuario antes de efetuar o submit do formulario pai do botao.
    //O valor do input hidden com a classe mensagem dentro do formulario eh exibido na pergunta.
    //Caso o usuario aceite, o formulario eh submetido, caso cancele ou feche nada acontece.
    function prepararConfirmacoes() {
    	let botoesConfirmacao = document.querySelectorAll(".confirmacao");
    	botoesConfirmacao.forEach(function(botao) {
    		let formPai = botao.parentNode;
    		formPai.addEventListener("submit", function(evento) {
    			let mensagem = "Você esqueceu de definir a mensagem a ser exibida aqui...";   			
    			let inputMensagem = formPai.getElementsByClassName("mensagem")[0];
    			if (inputMensagem !== undefined) {
    				mensagem = inputMensagem.value;
    			}    				
    			evento.preventDefault();
	    		alertify.set('notifier', 'position', 'top-right');
    			alertify.confirm('Confirmação', mensagem, aceitou, cancelou()).set('labels', { ok: 'Sim', cancel: 'Não' });

	    		function aceitou() {
	    			formPai.submit();
	    		}

	    		function cancelou() {
	    		}
	    	});
    	});
    }
    
    
    //Busca por um input hidden com o id mensagem no HTML da página atual.
    //Caso encontre, busca por um input hidden com o id tipo e um input hidden com o id intervalo.
    //Mostra uma notificao do AlertifyJS desse tipo com essa mensagem por esse intervalor.
    function mostrarNotificacaoFromHTML() {
        let inputMensagem = document.getElementById("mensagem");
    	if (inputMensagem !== null && inputMensagem.value !== "") {
	    	mensagem = inputMensagem.value;
	    	tipo = document.getElementById("tipo").value;
	    	intervalo = document.getElementById("intervalo").value;
            alertify.set('notifier', 'position', 'top-right');
    		alertify.notify(mensagem, tipo, intervalo);
	    }
	}
	
	//Tratamento de formularios via AJAX
	//Para funcionar a pagina deve ter ao menos um formulario com a classe "enviarcomajax".
	//Caso o envio tenha como resposta um fragmento HTML o formulario deverá ter um atributo
	// data-iddestinoresposta com o id do elemento que recebera o fragmento HTML de resposta
	//Caso nao exista um atributo data-iddestinoresposta a resposta será colocada no body da página.
	function prepararEnvioFormulariosComAJAX() {
		let formulariosAJAX = document.querySelectorAll(".enviarcomajax");
		formulariosAJAX.forEach(function(formulario) {
			formulario.addEventListener("submit", enviarFormulario);
		});
	}

	//Funcao que envia o formulario alvo do evento (submit) via AJAX.
	//A URL da requisicao é obtida do action do formulario.
	//Os dados do formulario são enviados no formato JSON.
	//Dependendo do tipo da resposta recebida (FRAGMENTO, NOTIFICACAO ou  FRAGMENTO_E_NOTIFICACAO) a pagina atual é atualizada com o fragmento recebido e/ou a notificacao é exibida.
	function enviarFormulario(evento) {
		evento.preventDefault();
		let formulario = evento.target;
		let urlDestino = formulario.getAttribute("action");
		let dadosForm = new FormData(formulario);
		let dados = {};
		for (let chave of dadosForm.keys()) {
			dados[chave] = dadosForm.get(chave);
		}
		enviarViaAjax(dados, urlDestino, function(resposta) {
			//Não deveria acontecer, mas se chegou apenas texto/HTML colocamos como conteúdo do body
			if (typeof resposta === "String" || typeof resposta === "string") {
				atualizarFragmentoHTML(formulario, resposta);
			} else if (typeof resposta === 'object') {  //É o esperado que se receba
				switch (resposta.tipoResposta) {
					case 'FRAGMENTO':
						atualizarFragmentoHTML(formulario, resposta.htmlFragmento);
						break;
					case 'NOTIFICACAO':
						mostrarNotificacao(resposta.notificacao);
						break;
					case 'FRAGMENTO_E_NOTIFICACAO':
						mostrarNotificacao(resposta.notificacao);
						atualizarFragmentoHTML(formulario, resposta.htmlFragmento);
						break;
				}
			}
		}, (dados != {}) ? 'POST' : 'GET');
		return false;
	}

	//Funcao que mostra uma notificacao do AlertifyJS.
	//notificacao deve ser um objeto com os atributos mensagem, tipo e intervalo.
	//o tipo deve ser um dos tipos permitidos pelo AlertifyJS: success, error, warning ou message
	function mostrarNotificacao(notificacao) {
		alertify.notify(notificacao.mensagem, notificacao.tipo, notificacao.intervalo);
	}

	//Funcao que atualiza um pedaco da pagina atual
	//O formulario deve ter um atributo data-iddestinoresposta com o id do elemento que recebera o conteudo. Caso não exista o conteudo será colocado no body da página.
	function atualizarFragmentoHTML(formulario, conteudo) {
		let destinoresposta = document.getElementById(formulario.dataset.iddestinoresposta);
		if (destinoresposta !== null ) {
			destinoresposta.innerHTML = conteudo;	
		} else {
			document.body.innerHTML = conteudo;
		}
		atualizarCodigoNaPagina();
	}

	//Funcao responsavel pelo AJAX na página.
	//Se conteudoJSON for um objeto JSON com conteúdo ele será enviado.
	//Se conteudoJSON for vazio {} nada é enviado e nesse caso o metodo deveria ser um GET.
	//A resposta recebida via AJAX pode ser um objeto JSON ou não (HTML/texto puro).
	// De qualquer forma a funcaoSucesso é chamada passando essa resposta como argumento. 
	function enviarViaAjax(conteudoJSON, url, funcaoSucesso, metodo = 'POST', funcaoErro = function(e) { console.log(e); }) {
		console.log("Na funcao enviarViAjax");
		let xhr = new XMLHttpRequest();
		xhr.onload = function() {
			console.log(this);
			if (this.status == 200) {
				let resposta = {};
				if (this.response.startsWith('{')) {
					console.log("O tipo da resposta recebida é JSON");
					resposta = JSON.parse(this.response);
				} else {
					console.log("O tipo da resposta recebida NÃO é JSON");
					resposta = this.response;
				}
				funcaoSucesso(resposta);
			} else if ((this.status == 404) || (this.status == 403) || (this.status == 500)) {
				funcaoErro(this);
			}
		}
		xhr.onerror = function() {
			funcaoErro(this);
		}
		xhr.open(metodo, url, true);
		if (conteudoJSON !== {}) {
			xhr.setRequestHeader("Content-type", "application/json");
			xhr.send(JSON.stringify(conteudoJSON));
		} else {
			xhr.send();
		}
	}

});