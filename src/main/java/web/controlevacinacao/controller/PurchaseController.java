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
import web.controlevacinacao.model.fidex_model.Purchase;
import web.controlevacinacao.model.filter.PurchaseFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PurchaseRepository;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseRepository PurchaseRepository;
    
    @GetMapping("/compras")
    public String pesquisar(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Purchase> pagina = PurchaseRepository.buscarComFiltro(filtro, pageable);
        if (!pagina.isEmpty()) {
            PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
            model.addAttribute("pagina", paginaWrapper);
            return "compras";
        } else {
            model.addAttribute("mensagem", "Não foram encontradas Purchasees com esse filtro");
            model.addAttribute("opcao", "purchase");
            return "mostrarmensagem";
        }
    }



}
