package web.controlevacinacao.controller;

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
import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.model.filter.ProductFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ProductRepository;
import web.controlevacinacao.service.ProductService;

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
