package web.fidex.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
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

    private final ClientRepository clientRepository;
    private final ClientService clientService;

    public ClientController(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @GetMapping("/clientes")
    public String pesquisar(ClientFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");
        return "clientes";
    }

    @GetMapping("/clientes/ordenar/pontuacao")
    public String pesquisarPontuacao(ClientFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "points", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "pontuacao");
        return "clientes";
    }

    @GetMapping("/clientes/ordenar/nome")
    public String pesquisarNome(ClientFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "nome");
        return "clientes";
    }

    @GetMapping("/clientes/ordenar/antigos")
    public String pesquisarAntigos(ClientFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = clientRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "antigos");
        return "clientes";
    }

    @PostMapping("/clientes/cadastrar")
    public String cadastrar(@Valid Client client, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("mensagem", "Há campos em branco. Verifique e tente novamente.");
            return "mostrarmensagem";
        } else if (client.getPhone().replaceAll("\\D", "").length() != 11) {
            model.addAttribute("mensagem", "WhatsApp inválido. Digite o número completo com DDD (11 dígitos).");
            return "mostrarmensagem";
        } else if (client.getCpf().replaceAll("\\D", "").length() != 11) {
            model.addAttribute("mensagem", "CPF inválido. Digite os 11 dígitos do CPF.");
            return "mostrarmensagem";
        } else {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            client.setCreatedBy(userId);

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
            model.addAttribute("mensagem", "Não foi encontrada cliente com esse código.");
            return "mostrarmensagem";
        }
    }

}
