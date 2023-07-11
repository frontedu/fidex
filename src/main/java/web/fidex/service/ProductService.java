package web.fidex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Product;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository ProductRepository;

    @Transactional
    public void salvar(Product Product) {
        ProductRepository.save(Product);
    }

    @Transactional
    public void remover(Long codigo) {
        ProductRepository.deleteById(codigo);
    }

    public List<Product> getProductsByPoints(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        return productRepository.findByPriceLessThanEqual(client.getPoints());
    }

}
