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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Prize;
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.PrizeFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.PrizeRepository;
import web.fidex.repository.ProductRepository;
import web.fidex.service.ClientService;
import web.fidex.service.PrizeService;
import web.fidex.service.ProductService;

@Controller
public class PrizeController {

    private final PrizeRepository prizeRepository;
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final PrizeService prizeService;
    private final ProductService productService;
    private final ClientService clientService;

    public PrizeController(PrizeRepository prizeRepository,
            ProductRepository productRepository,
            ClientRepository clientRepository,
            PrizeService prizeService,
            ProductService productService,
            ClientService clientService) {
        this.prizeRepository = prizeRepository;
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
        this.prizeService = prizeService;
        this.productService = productService;
        this.clientService = clientService;
    }

    @GetMapping("/premios")
    public String pesquisar(PrizeFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClientAndPurchases(model, username);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");
        return "premios";
    }

    @GetMapping("/premios/ordenar/cliente")
    public String pesquisarAntigos(PrizeFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "client", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClientAndPurchases(model, username);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "cliente");
        return "premios";
    }

    @GetMapping("/premios/ordenar/produto")
    public String pesquisarProduto(PrizeFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "product", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        putClientAndPurchases(model, username);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "produto");
        return "premios";
    }

    @PostMapping("/premios/cadastrar")
    @Transactional
    public String cadastrar(@Valid Prize prize, BindingResult resultado, Model model,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal(expression = "username") String username) {

        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("erro",
                    "Ha campos invalidos ou em branco. Verifique e tente novamente.");
            return "redirect:/premios";
        }

        try {
            Long productId = prize.getProductId();
            if (productId == null && prize.getProduct() != null) {
                productId = prize.getProduct().getId();
            }

            Long clientId = prize.getClientId();
            if (clientId == null && prize.getClient() != null) {
                clientId = prize.getClient().getId();
            }

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produto nao encontrado."));
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Cliente nao encontrado."));

            if (product.getQuantity() == null || product.getQuantity() <= 0) {
                redirectAttributes.addFlashAttribute("erro", "Produto sem estoque disponivel.");
                return "redirect:/premios";
            }

            double clientPoints = client.getPoints() != null ? client.getPoints() : 0.0;
            double productPrice = product.getPrice() != null ? product.getPrice() : 0.0;

            if (clientPoints < productPrice) {
                redirectAttributes.addFlashAttribute("erro",
                        "O cliente nao possui pontuacao suficiente para resgatar esse produto.");
                return "redirect:/premios";
            }

            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();

            product.setQuantity(product.getQuantity() - 1);
            productService.salvar(product);

            client.setPoints(clientPoints - productPrice);
            clientService.salvar(client);

            prize.setProduct(product);
            prize.setClient(client);
            prize.setCreatedBy(username);
            prize.setDate(currentDate);
            prizeService.salvar(prize);

            redirectAttributes.addFlashAttribute("sucesso", "Premiacao cadastrada com sucesso!");
            return "redirect:/premios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar premiacao: " + e.getMessage());
            return "redirect:/premios";
        }
    }

    private void putClientAndPurchases(Model model, String username) {
        List<Client> clients = clientRepository.findByCreatedByAndStatus(username, Status.ATIVO);
        List<Product> products = productRepository.findByCreatedByAndStatus(username, Status.ATIVO);
        model.addAttribute("clients", clients);
        model.addAttribute("products", products);
    }

    @PostMapping("/premios/remover")
    public String remover(Long id, Model model) {
        Optional<Prize> optPrize = prizeRepository.findById(id);
        if (optPrize.isPresent()) {
            Prize prize = optPrize.get();
            prize.setStatus(Status.INATIVO);
            prizeService.salvar(prize);
            return "redirect:/premios";
        } else {
            model.addAttribute("mensagem", "Nao foi encontrado premios com esse codigo.");
            return "mostrarmensagem";
        }
    }

}
