package web.fidex.controller;

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
import jakarta.validation.Valid;
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ProductFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ProductRepository;
import web.fidex.service.ProductService;

@Controller
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("/produtos")
    public String pesquisar(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "ultimos");

        return "produtos";
    }

    @GetMapping("/produtos/ordenar/estoque")
    public String pesquisarEstoque(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "quantity", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "estoque");
        return "produtos";

    }

    @GetMapping("/produtos/ordenar/pontos")
    public String pesquisarPontos(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "pontos");
        return "produtos";

    }

    @GetMapping("/produtos/ordenar/nome")
    public String pesquisarNome(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request,
            @AuthenticationPrincipal(expression = "username") String username) {
        filtro.setCreatedBy(username);
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("active", "nome");
        return "produtos";
    }

    @PostMapping("/produtos/cadastrar")
    public String cadastrar(@Valid Product product, BindingResult resultado, Model model,
            @AuthenticationPrincipal(expression = "username") String username) {
        if (resultado.hasErrors()) {
            model.addAttribute("mensagem", "Ha campos invalidos ou em branco. Verifique e tente novamente.");
            return "mostrarmensagem";
        }

        product.setCreatedBy(username);

        productService.salvar(product);
        return "redirect:/produtos";
    }

    @PostMapping("/produtos/remover")
    public String remover(Long id, Model model) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            product.setStatus(Status.INATIVO);
            productService.salvar(product);
            return "redirect:/produtos";
        } else {
            model.addAttribute("mensagem", "Nao foi encontrado produto com esse codigo.");
            return "mostrarmensagem";
        }
    }

}
