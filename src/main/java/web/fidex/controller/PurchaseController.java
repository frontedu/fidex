package web.fidex.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PurchaseFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.PurchaseRepository;
import web.fidex.service.PurchaseService;

@Controller
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseRepository purchaseRepository,
            ClientRepository clientRepository,
            PurchaseService purchaseService) {
        this.purchaseRepository = purchaseRepository;
        this.clientRepository = clientRepository;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/compras")
    public String pesquisar(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClient(model);

        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");
        return "compras";
    }

    @GetMapping("/compras/ordenar/valor")
    public String pesquisarValor(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClient(model);
        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "valor");
        return "compras";

    }

    @GetMapping("/compras/ordenar/cliente")
    public String pesquisarNome(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "client", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClient(model);
        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "cliente");
        return "compras";

    }

    @PostMapping("/compras/cadastrar")
    public String cadastrar(@Valid Purchase purchase, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return "redirect:/compras";
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();
            purchase.setDate(currentDate);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername();

            purchase.setCreatedBy(userId);

            purchaseService.salvar(purchase);

            Client client = purchase.getClient();
            client.setPoints(client.getPoints() + purchase.getPoints());
            clientRepository.save(client);

            return "redirect:/compras";
        }
    }

    private void putClient(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("username", username);
        }

        List<Client> clients = clientRepository.findByStatus(Status.ATIVO);
        model.addAttribute("clients", clients);
    }

    @PostMapping("/compras/remover")
    @Transactional
    public String remover(Long id, Model model) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {

            Purchase purchase = optPurchase.get();
            Client client = purchase.getClient();

            if (client.getPoints() - purchase.getPoints() < 0) {
                client.setPoints(0.00);
                clientRepository.save(client);
            } else {
                client.setPoints(client.getPoints() - purchase.getPoints());
                clientRepository.save(client);
            }

            purchase.setStatus(Status.INATIVO);
            purchaseService.salvar(purchase);

            return "redirect:/compras";
        } else {
            model.addAttribute("mensagem", "Não foi encontrado compra com esse código.");
            return "mostrarmensagem";
        }
    }

}
