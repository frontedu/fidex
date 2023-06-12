package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.controlevacinacao.model.fidex_model.Product;
import web.controlevacinacao.repository.ProductRepository;

@Service
public class ProductService {
    
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

}
