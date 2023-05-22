package web.controlevacinacao.controller;

import java.util.List;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.model.filter.LoteFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.LoteService;

@Controller
@RequestMapping("/lotes")
public class LoteController {

    private static final Logger logger = LoggerFactory.getLogger(LoteController.class);

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private LoteService loteService;

    @Autowired
    private LoteRepository loteRepository;

    @GetMapping("/cadastrar")
    public String abrirCadastrar(Lote lote, Model model) {
        model.addAttribute("titulo", "Cadastrar Lote");
        model.addAttribute("url", "/lotes/cadastrar");
        model.addAttribute("textoBotao", "Cadastrar");
        colocarVacinasNoModel(model);
        return "lotes/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid Lote lote, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return abrirCadastrar(lote, model);
        } else {
            loteService.salvar(lote);
            return "redirect:/lotes/cadastrook";
        }
    }

    @GetMapping("/cadastrook")
    public String mostrarMensagemCadastroOK(Model model) {
        model.addAttribute("mensagem", "Lote cadastrado com sucesso");
        model.addAttribute("opcao", "lotes");
        return "mostrarmensagem";
    }

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar(Model model) {
        colocarVacinasNoModel(model);
        model.addAttribute("uso", "lotes");
        model.addAttribute("url", "/lotes/pesquisar");
        return "lotes/pesquisar";
    }

    private void colocarVacinasNoModel(Model model) {
        List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
        model.addAttribute("vacinas", vacinas);
    }

    @GetMapping("/pesquisar")
    public String pesquisar(LoteFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Lote> pagina = loteRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("uso", "lotes");
            return "lotes/lotes";
        } else {
            model.addAttribute("mensagem", "Não foram encontrados lotes com esse filtro");
            model.addAttribute("opcao", "lotes");
            return "mostrarmensagem";
        }
    }

    @PostMapping("/abriralterar")
    public String abrirAlterar(Lote lote, Model model) {
        colocarVacinasNoModel(model);
        model.addAttribute("titulo", "Alterar Lote");
        model.addAttribute("url", "/lotes/alterar");
        model.addAttribute("textoBotao", "Alterar");
        return "lotes/cadastrar";
    }

    @PostMapping("/alterar")
    public String alterar(@Valid Lote lote, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return abrirAlterar(lote, model);
        } else {
            loteService.salvar(lote);
            return "redirect:/lotes/alteracaook";
        }
    }

    @GetMapping("/alteracaook")
    public String mostrarMensagemAlteracaoOK(Model model) {
        model.addAttribute("mensagem", "Lote alterado com sucesso");
        model.addAttribute("opcao", "lotes");
        return "mostrarmensagem";
    }

    @PostMapping("/abrirremover")
    public String abrirRemover(Lote lote) {
        return "lotes/confirmarremocao";
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Optional<Lote> optLote = loteRepository.findById(codigo);
        if (optLote.isPresent()) {
            Lote lote = optLote.get();
            lote.setStatus(Status.INATIVO);
            loteService.salvar(lote);
            return "redirect:/lotes/remocaook";
        } else {
            model.addAttribute("mensagem", "Não foi encontrado Lote com esse código");
            model.addAttribute("opcao", "lotes");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/remocaook")
    public String mostrarMensagemRemocaoOK(Model model) {
        model.addAttribute("mensagem", "Lote removido com sucesso");
        model.addAttribute("opcao", "lotes");
        return "mostrarmensagem";
    }

}
