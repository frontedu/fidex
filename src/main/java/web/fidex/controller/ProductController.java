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
import web.fidex.model.fidex_model.Product;
import web.fidex.model.fidex_model.Status;
import web.fidex.model.filter.ProductFilter;
import web.fidex.pagination.PageWrapper;
import web.fidex.repository.ProductRepository;
import web.fidex.service.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @GetMapping("/produtos")
    public String pesquisar(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "ultimos");

            return "produtos";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Productes com esse filtro");
            model.addAttribute("opcao", "product");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/produtos/ordenar/estoque")
    public String pesquisarEstoque(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "quantity", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "estoque");
            return "produtos";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Productes com esse filtro");
            model.addAttribute("opcao", "product");
            return "mostrarmensagem";
        }
    }

        @GetMapping("/produtos/ordenar/pontos")
    public String pesquisarPontos(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "price", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "pontos");
            return "produtos";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Productes com esse filtro");
            model.addAttribute("opcao", "product");
            return "mostrarmensagem";
        }
    }

            @GetMapping("/produtos/ordenar/nome")
    public String pesquisarNome(ProductFilter filtro, Model model,
            @PageableDefault(size = 50) @SortDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Product> pagina = productRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Product> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            model.addAttribute("active", "nome");
            return "produtos";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Productes com esse filtro");
            model.addAttribute("opcao", "product");
            return "mostrarmensagem";
        }
    }



    @PostMapping("/produtos/cadastrar")
    public String cadastrar(@Valid Product product, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return "redirect:/produtos";
        } else {
            productService.salvar(product);
            return "redirect:/produtos";
        }
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
            model.addAttribute("mensagem", "Não foi encontrado produto com esse código");
            return "mostrarmensagem";
        }
    }

}
