package web.fidex.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
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

    @Autowired
    private PrizeRepository prizeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/premios")
    public String pesquisar(PrizeFilter filtro, Model model,
            @PageableDefault(size = 500) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClientAndPurchases(model);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");
        return "premios";
    }

    @GetMapping("/premios/ordenar/cliente")
    public String pesquisarAntigos(PrizeFilter filtro, Model model,
            @PageableDefault(size = 500) @SortDefault(sort = "client", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClientAndPurchases(model);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "cliente");
        return "premios";
    }

    @GetMapping("/premios/ordenar/produto")
    public String pesquisarProduto(PrizeFilter filtro, Model model,
            @PageableDefault(size = 500) @SortDefault(sort = "product", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClientAndPurchases(model);
        Page<Prize> pagina = prizeRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "produto");
        return "premios";
    }

    @PostMapping("/premios/cadastrar")
    @Transactional
    public String cadastrar(@Valid Prize prize, BindingResult resultado, Model model) {

        if(resultado.hasErrors()) {
            model.addAttribute("mensagem", "Há campos em inválidos ou em branco. Verifique e tente novamente.");
            return "mostrarmensagem";
        } else if(prize.getClient().getPoints() < prize.getProduct().getPrice() && prize.getProduct().getQuantity() > 0) {
            model.addAttribute("mensagem", "O cliente não possui pontuação suficiente para resgatar esse produto");
            return "mostrarmensagem";
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();

            Product product = prize.getProduct();
            product.setQuantity(product.getQuantity() - 1);
            productService.salvar(product);

            Client client = prize.getClient();
            client.setPoints(client.getPoints() - prize.getProduct().getPrice());
            clientService.salvar(client);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername();

            prize.setCreatedBy(userId);

            prize.setDate(currentDate);
            prizeService.salvar(prize);
            return "redirect:/premios";
        }

    }

    private void putClientAndPurchases(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            model.addAttribute("username", username);
        }

        List<Client> clients = clientRepository.findByStatus(Status.ATIVO);
        List<Product> products = productRepository.findByStatus(Status.ATIVO);
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
            model.addAttribute("mensagem", "Não foi encontrado prêmios com esse código.");
            return "mostrarmensagem";
        }
    }

}
