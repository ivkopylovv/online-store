package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity addProduct(ProductEntity product) throws ProductAlreadyExistException {
        if (productRepository.findByName(product.getName()) != null) {
            throw new ProductAlreadyExistException("Product already exists");
        }
        return productRepository.save(product);
    }

    public ProductEntity getProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();
        if (product == null) {
            throw new ProductNotFoundException("Product not Found");
        }
        return product;
    }

    public void deleteProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();
        if (product == null) {
            throw new ProductNotFoundException("Product not Found");
        }
        productRepository.delete(product);
    }

    public List<ProductEntity> getProductsBetweenId1Id2 (long id1,long id2) {
        return productRepository.getByIdGreaterThanEqualAndIdLessThanEqual(id1,id2);
    }

    public  long getCountProducts(){
        return productRepository.count();
    }
}
