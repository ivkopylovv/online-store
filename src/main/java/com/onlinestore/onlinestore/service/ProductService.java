package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.ProductAllFieldsDto;
import com.onlinestore.onlinestore.dto.response.FullProductDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.dto.response.ProductsTagDto;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.entity.ProductImages;
import com.onlinestore.onlinestore.entity.ProductTags;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.repository.ProductImagesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.ProductTagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductTagsRepository productTagsRepository;

    public Product addProduct(ProductAllFieldsDto product) {
        if (productRepository.findByName(product.getName()) != null) {
            throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
        }

        Product newProduct = new Product(
                product.getName(),
                product.getDescription(),
                product.getImage(),
                product.getPrice()
        );

        return productRepository.save(newProduct);
    }

    public FullProductDto getProduct(long id) {
        Product product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        List <ProductImages> productImagesEntities = productImagesRepository.findByProductId(id);

        if (productImagesEntities.isEmpty()) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
        }

        List <ProductTags> productTagsEntities = productTagsRepository.findByProductId(id);

        if (productTagsEntities.isEmpty()) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_TAGS_NOT_FOUND);
        }

        ArrayList <ProductsTagDto> productsTags = new ArrayList <ProductsTagDto>();
        ArrayList <String> productsImages = new ArrayList<>();

        for (ProductImages productImage: productImagesEntities) {
            productsImages.add(productImage.getImage());
        }

        for (ProductTags productTag: productTagsEntities) {
            productsTags.add(new ProductsTagDto(productTag.getType(), productTag.getValue()));
        }

        return new FullProductDto(id,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                productsImages, productsTags);
    }

    public void deleteProduct(long id) throws ProductNotFoundException {
        Product product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        productRepository.delete(product);
    }

    public List<ProductInfoDto> getPageProducts(int page) {
        List<Product> products = productRepository.findByOrderById(PageRequest.of(page, ProductOption.PAGE_COUNT));
        List<ProductInfoDto> productsDto = new ArrayList<ProductInfoDto>();

        for (Product item : products) {
            productsDto.add(new ProductInfoDto(item.getId(), item.getName(), item.getPrice(), item.getImage()));
        }

        return productsDto;
    }

    public List<ProductInfoDto> searchProductsByNameSortingByParameter(
            String name,
            int page,
            Boolean asc,
            String parameter
    ) {
        List<Product> products = new ArrayList<Product>();
        if (asc) {
            products = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, ProductOption.PAGE_COUNT, Sort.by(parameter).ascending())
            );
        } else {
            products = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, ProductOption.PAGE_COUNT, Sort.by(parameter).descending())
            );
        }
        List<ProductInfoDto> productsDto = new ArrayList<ProductInfoDto>();

        for (Product item : products) {
            productsDto.add(new ProductInfoDto(item.getId(), item.getName(), item.getPrice(), item.getImage()));
        }

        return productsDto;
    }

    public long getCountPagesProductsLikeName() {
        return productRepository.count() / ProductOption.PAGE_COUNT;
    }

    public long getCountPagesProductsLikeName(String name) {

        return productRepository.countByNameStartingWith(name) / ProductOption.PAGE_COUNT;
    }

    public void updateProductById(ProductAllFieldsDto product) {
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
