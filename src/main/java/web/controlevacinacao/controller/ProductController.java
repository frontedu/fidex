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
import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.model.filter.ProductFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.ProductRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository ProductRepository;

    @GetMapping("/produtos")
    public String pesquisar(ProductFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Product> pagina = ProductRepository.buscarComFiltro(filtro, pageable);
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



}
