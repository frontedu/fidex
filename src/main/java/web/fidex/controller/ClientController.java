package web.fidex.controller;

import java.util.Optional;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ClientFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ClientRepository;
import web.fidex.service.ClientService;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientService clientService;

    @GetMapping("/clientes")
    public String pesquisar(ClientFilter filtro, Model model,
            @PageableDefault(size = 5000) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "ultimos");
            return "clientes";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas clientes com esse filtro");
            model.addAttribute("opcao", "client");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/clientes/ordenar/pontuacao")
    public String pesquisarPontuacao(ClientFilter filtro, Model model,
            @PageableDefault(size = 5000) @SortDefault(sort = "points", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "pontuacao");
            return "clientes";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas clientes com esse filtro");
            model.addAttribute("opcao", "client");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/clientes/ordenar/nome")
    public String pesquisarNome(ClientFilter filtro, Model model,
            @PageableDefault(size = 5000) @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "nome");
            return "clientes";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas clientes com esse filtro");
            model.addAttribute("opcao", "client");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/clientes/ordenar/antigos")
    public String pesquisarAntigos(ClientFilter filtro, Model model,
            @PageableDefault(size = 5000) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "antigos");
            return "clientes";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas clientes com esse filtro");
            model.addAttribute("opcao", "client");
            return "mostrarmensagem";
        }
    }

    @PostMapping("/clientes/cadastrar")
    public String cadastrar(@Valid Client client, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return "redirect:/clientes";
        } else {
            clientService.salvar(client);
            return "redirect:/clientes";
        }
    }

    @PostMapping("/clientes/remover")
    public String remover(Long id, Model model) {
        Optional<Client> optClient = clientRepository.findById(id);
        if (optClient.isPresent()) {
            Client client = optClient.get();
            client.setStatus(Status.INATIVO);
            clientService.salvar(client);
            return "redirect:/clientes";
        } else {
            model.addAttribute("mensagem", "Não foi encontrada cliente com esse código");
            return "mostrarmensagem";
        }
    }

}
