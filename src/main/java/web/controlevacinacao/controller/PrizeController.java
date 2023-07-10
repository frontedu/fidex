package web.controlevacinacao.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.fidex_model.Prize;
import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.model.filter.PrizeFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ClientRepository;
import web.controlevacinacao.repository.PrizeRepository;
import web.controlevacinacao.repository.ProductRepository;
import web.controlevacinacao.service.PrizeService;
import web.controlevacinacao.service.ProductService;
import web.controlevacinacao.service.ClientService;

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
        if (!pagina.isEmpty()) {
            PageWrapper<Prize> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            return "premios";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Prizees com esse filtro");
            model.addAttribute("opcao", "prize");
            return "mostrarmensagem";
        }
    }

    @PostMapping("/premios/cadastrar")
    @Transactional
    public String cadastrar(@Valid Prize prize, BindingResult resultado, Model model) {

        if (prize.getClient().getPoints() < prize.getProduct().getPrice() && prize.getProduct().getQuantity() > 0) {
            model.addAttribute("mensagem", "O cliente não possui pontos suficientes para resgatar esse produto");
            return "redirect:/premios";
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();

            Product product = prize.getProduct();
            product.setQuantity(product.getQuantity() - 1);
            productService.salvar(product);

            Client client = prize.getClient();
            client.setPoints(client.getPoints() - prize.getProduct().getPrice());
            clientService.salvar(client);

            prize.setDate(currentDate);
            prizeService.salvar(prize);
            return "redirect:/premios";
        }

    }

    private void putClientAndPurchases(Model model) {
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
            model.addAttribute("mensagem", "Não foi encontrado premios com esse código");
            return "mostrarmensagem";
        }
    }

}
