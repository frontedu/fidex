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
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.filter.ClientFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClientRepository;
import web.controlevacinacao.service.ClientService;

@Controller
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientRepository ClientRepository;

    @Autowired
    private ClientService ClientService;

    @Autowired
    private ThymeleafUtil util;
    
  /*
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
   */




}
