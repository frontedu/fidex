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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.filter.PessoaFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.service.PessoaService;

@Controller
@RequestMapping("/pessoas")
public class PessoaController {
    
    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/cadastrar")
    public String abrirCadastro(Pessoa pessoa) {
        return "pessoas/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(Pessoa pessoa) {
        pessoaService.salvar(pessoa);
        return "redirect:/pessoas/mostrarmensagemcadastrook";
    }

    @GetMapping("/mostrarmensagemcadastrook")
    public String mostrarMensagemCadastroOK(Model model, Pessoa pessoa) {
        NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Pessoa inserida com sucesso!");
        notificacaoAlertify.setTipo(TipoNotificaoAlertify.SUCESSO);
        notificacaoAlertify.setIntervalo(5);
        model.addAttribute("notificacao", notificacaoAlertify);
        return "pessoas/cadastrar";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar(Model model) {
        model.addAttribute("url", "/pessoas/pesquisar");
        model.addAttribute("uso", "pessoas");
        return "pessoas/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(PessoaFilter filtro, Model model,
            @PageableDefault(size = 5)
            @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) 
            Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.filtrar(filtro, pageable);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        logger.info("Pessoas buscadas no BD: {}", paginaWrapper.getConteudo());
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("uso", "pessoas");
        return "pessoas/mostrartodas";
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Long codigo, Model model) {
        Optional<Pessoa> optPessoa = pessoaRepository.findById(codigo);
        if (optPessoa.isPresent()) {
            model.addAttribute("pessoa", optPessoa.get());
            return "pessoas/alterar";
        } else {
            model.addAttribute("opcao", "pessoas");
            model.addAttribute("mensagem", "Não existe pessoa com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/alterar")
    public String alterar(Pessoa pessoa) {
        pessoaService.salvar(pessoa);
        return "redirect:/pessoas/mostrarmensagemalterarok";
    }

    @GetMapping("/mostrarmensagemalterarok")
    public String mostrarMensagemAlterarOK(Model model) {
        model.addAttribute("opcao", "pessoas");
        model.addAttribute("mensagem", "Pessoa alterada com sucesso!");
        return "mostrarmensagem";
    }

    @PostMapping("/abrirremover")
    public String abrirConfirmar(Long codigo, Model model) {
        Optional<Pessoa> optPessoa = pessoaRepository.findById(codigo);
        if (optPessoa.isPresent()) {
            model.addAttribute("pessoa", optPessoa.get());
            return "pessoas/confirmarremocao";
        } else {
            model.addAttribute("opcao", "pessoas");
            model.addAttribute("mensagem", "Não existe pessoa com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Optional<Pessoa> optPessoa = pessoaRepository.findById(codigo);
        if (optPessoa.isPresent()) {
            Pessoa pessoa = optPessoa.get();
            pessoa.setStatus(Status.INATIVO);
            pessoaService.alterar(pessoa);
            return "redirect:/pessoas/mostrarmensagemremocaook";
        } else {
            model.addAttribute("opcao", "pessoas");
            model.addAttribute("mensagem", "Impossível remover pessoa com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @GetMapping("/mostrarmensagemremocaook")
    public String mostrarMensagemRemoverOK(Model model) {
        model.addAttribute("opcao", "pessoas");
        model.addAttribute("mensagem", "Pessoa removida com sucesso!");
        return "mostrarmensagem";
    }
}
