package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dto.request.ProductCreateDto;
import com.onlinestore.onlinestore.dto.response.ProductDto;
import com.onlinestore.onlinestore.dto.response.ProductNameIdPriceDto;
import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity addProduct(ProductCreateDto product) throws ProductAlreadyExistException {
        if (productRepository.findByName(product.getName()) != null) {
            throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
        }
        ProductEntity newProduct = new ProductEntity(product.getId(), product.getName(), product.getDescription(), product.getPrice());
        return productRepository.save(newProduct);
    }

    public ProductDto getProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException("Product not Found");
        }

        return new ProductDto(product);
    }

    public void deleteProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();
        if (product == null) {
            throw new ProductNotFoundException("Product not Found");
        }
        productRepository.delete(product);
    }

    public List<ProductNameIdPriceDto> getPageProducts(int page) {
        List<ProductEntity> productsEntity = productRepository.findByOrderById(PageRequest.of(page,10));

        List<ProductNameIdPriceDto> productsDto = new ArrayList<ProductNameIdPriceDto>();

        for (ProductEntity item : productsEntity) {
            productsDto.add(new ProductNameIdPriceDto(item.getId(), item.getName(), item.getPrice()));
        }
        return productsDto;
    }

    public long getCountProducts() {
        return productRepository.count();
    }
}
