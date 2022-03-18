package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.Product;
import com.onlinestore.onlinestore.dto.request.ProductAllFieldsDto;
import com.onlinestore.onlinestore.dto.response.ProductDto;
import com.onlinestore.onlinestore.dto.response.ProductNameIdPriceDto;
import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity addProduct(ProductAllFieldsDto product) throws ProductAlreadyExistException {
        if (productRepository.findByName(product.getName()) != null) {
            throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
        }
        ProductEntity newProduct = new ProductEntity(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );
        return productRepository.save(newProduct);
    }

    public ProductDto getProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_IN_BASKET);
        }

        return new ProductDto(product);
    }

    public void deleteProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();
        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }
        productRepository.delete(product);
    }

    public List<ProductNameIdPriceDto> getPageProducts(int page) {
        List<ProductEntity> productsEntity = productRepository.findByOrderById(PageRequest.of(page, Product.countPage));

        List<ProductNameIdPriceDto> productsDto = new ArrayList<ProductNameIdPriceDto>();

        for (ProductEntity item : productsEntity) {
            productsDto.add(new ProductNameIdPriceDto(item.getId(), item.getName(), item.getPrice()));
        }
        return productsDto;
    }


    public List<ProductNameIdPriceDto> searchProductsByNameSortingByParameter(
            String name,
            int page,
            Boolean asc,
            String parameter
    ) {
        List<ProductEntity> productsEntity = new ArrayList<ProductEntity>();
        if (asc) {
            productsEntity = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, Product.countPage, Sort.by(parameter).ascending())
            );
        } else {
            productsEntity = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, Product.countPage, Sort.by(parameter).descending())
            );
        }
        List<ProductNameIdPriceDto> productsDto = new ArrayList<ProductNameIdPriceDto>();

        for (ProductEntity item : productsEntity) {
            productsDto.add(new ProductNameIdPriceDto(item.getId(), item.getName(), item.getPrice()));
        }
        return productsDto;
    }


    public long getCountPagesProductsLikeName() {
        return productRepository.count() / Product.countPage;
    }

    public long getCountPagesProductsLikeName(String name) {
        return productRepository.countByNameStartingWith(name) / Product.countPage;
    }

    public void updateProductById(ProductAllFieldsDto product) throws ProductAlreadyExistException{
        if (productRepository.findByName(product.getName()) != null){
            throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_WITH_NAME_ALREADY_EXIST);
        }
        productRepository.updateNameAndDescriptionAndPriceById(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getId()
        );
    }
}
