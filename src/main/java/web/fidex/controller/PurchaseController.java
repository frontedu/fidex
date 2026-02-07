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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.fidex.model.Usuario;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Purchase;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PurchaseFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.PurchaseRepository;
import web.fidex.service.PurchaseService;
import web.fidex.service.UsuarioService;

@Controller
public class PurchaseController {

    private final PurchaseRepository purchaseRepository;
    private final ClientRepository clientRepository;
    private final PurchaseService purchaseService;
    private final UsuarioService usuarioService;

    public PurchaseController(PurchaseRepository purchaseRepository,
            ClientRepository clientRepository,
            PurchaseService purchaseService,
            UsuarioService usuarioService) {
        this.purchaseRepository = purchaseRepository;
        this.clientRepository = clientRepository;
        this.purchaseService = purchaseService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/compras")
    public String pesquisar(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClient(model, username);

        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");
        return "compras";
    }

    @GetMapping("/compras/ordenar/valor")
    public String pesquisarValor(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClient(model, username);
        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "valor");
        return "compras";

    }

    @GetMapping("/compras/ordenar/cliente")
    public String pesquisarNome(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "client", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClient(model, username);
        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "cliente");
        return "compras";

    }

    @PostMapping("/compras/cadastrar")
    @Transactional
    public String cadastrar(@Valid Purchase purchase, BindingResult resultado, Model model,
            @AuthenticationPrincipal(expression = "username") String username) {
        if (resultado.hasErrors()) {
            return "redirect:/compras";
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();
        purchase.setDate(currentDate);

        purchase.setCreatedBy(username);

        Usuario usuario = usuarioService.findByNomeUsuarioIgnoreCase(username);
        double cashbackPercent = (usuario != null && usuario.getCashback() != null) ? usuario.getCashback() : 5.0;

        double purchasePrice = (purchase.getPriceValue() != null) ? purchase.getPriceValue() : 0.0;
        int pointsEarned = (int) (purchasePrice * cashbackPercent / 100);
        purchase.setPoints(pointsEarned);

        purchaseService.salvar(purchase);

        purchaseService.salvar(purchase);

        Client client = purchase.getClient();
        if (client == null && purchase.getClientId() != null) {
            client = clientRepository.findById(purchase.getClientId()).orElse(null);
            purchase.setClient(client);
        }

        if (client != null) {
            client.setPoints((client.getPoints() != null ? client.getPoints() : 0.0) + pointsEarned);
            clientRepository.save(client);
        }

        return "redirect:/compras";
    }

    private void putClient(Model model, String username) {
        List<Client> clients = clientRepository.findByCreatedByAndStatus(username, Status.ATIVO);
        model.addAttribute("clients", clients);
    }

    @PostMapping("/compras/remover")
    @Transactional
    public String remover(Long id, Model model) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {

            Purchase purchase = optPurchase.get();
            Client client = purchase.getClient();

            double currentPoints = client.getPoints() != null ? client.getPoints() : 0.0;
            int purchasePoints = purchase.getPoints();

            if (currentPoints - purchasePoints < 0) {
                client.setPoints(0.0);
                clientRepository.save(client);
            } else {
                client.setPoints(currentPoints - purchasePoints);
                clientRepository.save(client);
            }

            purchase.setStatus(Status.INATIVO);
            purchaseService.salvar(purchase);

            return "redirect:/compras";
        } else {
            model.addAttribute("mensagem", "Nao foi encontrado compra com esse codigo.");
            return "mostrarmensagem";
        }
    }

}
