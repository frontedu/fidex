package web.controlevacinacao.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jfree.util.Log;
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
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.model.fidex_model.Client;
import web.controlevacinacao.model.fidex_model.Purchase;
import web.controlevacinacao.model.fidex_model.Status;
import web.controlevacinacao.model.filter.PurchaseFilter;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.PurchaseRepository;
import web.controlevacinacao.repository.ClientRepository;
import web.controlevacinacao.service.PurchaseService;

@Controller
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/compras")
    public String pesquisar(PurchaseFilter filtro, Model model,
            @PageableDefault(size = 10000) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {
        putClient(model);
        Page<Purchase> pagina = purchaseRepository.buscarComFiltro(filtro, pageable);

        PageWrapper<Purchase> paginaWrapper = new PageWrapper<>(pagina, request);
        model.addAttribute("pagina", paginaWrapper);
        return "compras";

    }

    @PostMapping("/compras/cadastrar")
    public String cadastrar(@Valid Purchase purchase, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            return "redirect:/compras";
        } else {
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();
            purchase.setDate(currentDate);
            purchaseService.salvar(purchase);

            Client client = purchase.getClient();
            client.setPoints(client.getPoints() + purchase.getPoints());
            clientRepository.save(client);

            return "redirect:/compras";
        }
    }

    private void putClient(Model model) {
        List<Client> clients = clientRepository.findByStatus(Status.ATIVO);
        model.addAttribute("clients", clients);
    }

    @PostMapping("/compras/remover")
    @Transactional
    public String remover(Long id, Model model) {
        Optional<Purchase> optPurchase = purchaseRepository.findById(id);
        if (optPurchase.isPresent()) {

            Purchase purchase = optPurchase.get();

            Client client = purchase.getClient();
            client.setPoints(client.getPoints() - purchase.getPoints());
            clientRepository.save(client);

            purchase.setStatus(Status.INATIVO);
            purchaseService.salvar(purchase);

            return "redirect:/compras";
        } else {
            model.addAttribute("mensagem", "Não foi encontrado compra com esse código");
            return "mostrarmensagem";
        }
    }

}
