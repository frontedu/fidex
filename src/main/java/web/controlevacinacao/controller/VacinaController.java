package web.controlevacinacao.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.RespostaJSON;
import web.controlevacinacao.ajax.ThymeleafUtil;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.ajax.TipoResposta;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.VacinaFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.VacinaService;

@Controller
@RequestMapping("/vacinas")
public class VacinaController {

    private static final Logger logger = LoggerFactory.getLogger(VacinaController.class);

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private ThymeleafUtil util;

    // @GetMapping("/todas")
    // public String mostrarTodas(Model model) {
    // List<Vacina> vacinas = vacinaRepository.findAll();
    // logger.info("Vacinas buscadas no BD: {}", vacinas);
    // model.addAttribute("vacinas", vacinas);
    // return "vacinas/vacinas";
    // }

    // @GetMapping("/buscar")
    // public String abrirEntradaCodigo() {
    // return "vacinas/codigo";
    // }

    // @PostMapping("/buscar")
    // public String buscarPeloCodigo(Long codigo, Model model) {
    // if (codigo != null) {
    // Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
    // if (optVacina.isPresent()) {
    // model.addAttribute("vacinas", List.of(optVacina.get()));
    // return "vacinas/vacinas";
    // } else {
    // model.addAttribute("mensagem", "Vacina com esse código não encontrada");
    // return "mostrarmensagem";
    // }
    // } else {
    // model.addAttribute("mensagem", "Entre com um código válido");
    // return "mostrarmensagem";
    // }
    // }

    // @GetMapping("/buscarnome")
    // public String abrirEntradaNome() {
    // return "vacinas/entradanome";
    // }

    // @PostMapping("/buscarnome")
    // public String buscarPeloNome(String nome, Model model) {
    // if (!nome.isEmpty()) {
    // List<Vacina> vacinas = vacinaRepository.findByNomeContainingIgnoreCase(nome);
    // if (!vacinas.isEmpty()) {
    // model.addAttribute("vacinas", vacinas);
    // return "vacinas/vacinas";
    // } else {
    // model.addAttribute("mensagem", "Não foram encontradas vacinas com esse nome:
    // " + nome);
    // return "mostrarmensagem";
    // }
    // } else {
    // model.addAttribute("mensagem", "Digite um nome para fazer a busca");
    // return "mostrarmensagem";
    // }
    // }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar() {
        return "vacinas/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(VacinaFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Vacina> pagina = vacinaRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Vacina> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            return "vacinas/vacinas";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas vacinas com esse filtro");
            model.addAttribute("opcao", "vacinas");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/cadastrar")
    public String abrirCadastrar(Vacina vacina, Model model) {
        model.addAttribute("titulo", "Cadastrar Vacina");
        model.addAttribute("url", "/vacinas/cadastrar");
        model.addAttribute("textoBotao", "Cadastrar");
        return "vacinas/cadastrar";
    }

    @PostMapping(value = { "/cadastrar" }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public RespostaJSON cadastrar(@RequestBody @Valid Vacina vacina, BindingResult resultado,
            Model model, HttpServletRequest request, HttpServletResponse response) {

        RespostaJSON resposta;

        if (resultado.hasErrors()) {
            model.addAttribute("titulo", "Cadastrar Vacina");
            model.addAttribute("url", "/vacinas/cadastrar");
            model.addAttribute("textoBotao", "Cadastrar");
            resposta = new RespostaJSON(TipoResposta.FRAGMENTO);
            String html = util.processThymeleafTemplate(request, response, model.asMap(), "vacinas/cadastrar",
                    "cadastrar");
            resposta.setHtmlFragmento(html);
        } else {
            // vacina.setStatus(Status.ATIVO);
            vacinaService.salvar(vacina);

            NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Vacina cadastrada com sucesso",
                    TipoNotificaoAlertify.SUCESSO, 5);
            model.addAttribute("titulo", "Cadastrar Vacina");
            model.addAttribute("url", "/vacinas/cadastrar");
            model.addAttribute("textoBotao", "Cadastrar");
            resposta = new RespostaJSON(TipoResposta.FRAGMENTO_E_NOTIFICACAO);
            resposta.setNotificacao(notificacaoAlertify);
            model.addAttribute("vacina", new Vacina());
            String html = util.processThymeleafTemplate(request, response, model.asMap(), "vacinas/cadastrar",
                    "cadastrar");
            resposta.setHtmlFragmento(html);
        }
        return resposta;
    }

    @GetMapping("/cadastrook")
    public String mostrarMensagemCadastroOK(Model model) {
        model.addAttribute("mensagem", "Vacina cadastrada com sucesso");
        model.addAttribute("opcao", "vacinas");
        return "mostrarmensagem";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Vacina vacina, Model model) {
        model.addAttribute("titulo", "Alterar Vacina");
        model.addAttribute("url", "/vacinas/alterar");
        model.addAttribute("textoBotao", "Alterar");
        return "vacinas/alterar";
    }

    @PostMapping("/alterar")
    public String alterar(@Valid Vacina vacina, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return abrirAlterar(vacina, model);
        } else {
            vacinaService.salvar(vacina);
            return "redirect:/vacinas/alteracaook";
        }
    }

    @GetMapping("/alteracaook")
    public String mostrarMensagemAlteracaoOK(Model model) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Vacina alterada com sucesso",
            TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "vacinas/pesquisar";
    }

    @PostMapping("/abrirremover")
    public String abrirRemover(Vacina vacina) {
        return "vacinas/confirmarremocao";
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
        if (optVacina.isPresent()) {
            Vacina vacina = optVacina.get();
            vacina.setStatus(Status.INATIVO);
            vacinaService.salvar(vacina);
            return "redirect:/vacinas/remocaook";
        } else {
            model.addAttribute("mensagem", "Não foi encontrada Vacina com esse código");
            model.addAttribute("opcao", "vacinas");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/remocaook")
    public String mostrarMensagemRemocaoOK(Model model) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Vacina removida com sucesso",
                TipoNotificaoAlertify.SUCESSO, 5);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "vacinas/pesquisar";
    }

}
