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
import web.controlevacinacao.model.fidex_model.Prize;
import web.controlevacinacao.model.filter.PrizeFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PrizeRepository;

@Controller
public class PrizeController {

    @Autowired
    private PrizeRepository PrizeRepository;

    @GetMapping("/premios")
    public String pesquisar(PrizeFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Prize> pagina = PrizeRepository.buscarComFiltro(filtro, pageable);
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



}
