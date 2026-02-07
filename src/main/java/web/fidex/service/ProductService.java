package web.fidex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.fidex.model.fidex_model.Client;
import web.fidex.model.fidex_model.Product;
import web.fidex.repository.ClientRepository;
import web.fidex.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;

    public ProductService(ProductRepository productRepository, ClientRepository clientRepository) {
        this.productRepository = productRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public void salvar(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void remover(Long codigo) {
        productRepository.deleteById(codigo);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByPoints(Long clientId) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found"));
        double points = client.getPoints() != null ? client.getPoints() : 0.0;
        return productRepository.findByPriceLessThanEqual(points);
    }

}
