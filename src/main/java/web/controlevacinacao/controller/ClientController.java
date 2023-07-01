package web.controlevacinacao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.filter.ClientFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClientRepository;

@Controller
public class ClientController {

    @Autowired
    private ClientRepository ClientRepository;
    
    @GetMapping("/clientes")
    public String pesquisar(ClientFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Client> pagina = ClientRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Client> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            return "clientes";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas clientes com esse filtro");
            model.addAttribute("opcao", "client");
            return "mostrarmensagem";
        }
    }



}
