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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductTagsRepository productTagsRepository;

    public void addProduct(ProductAllFieldsDto product) {
        productRepository.findByName(product.getName()).
                ifPresent(result -> {
                    throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
                });

        Product newProduct = new Product(
                product.getName(),
                product.getDescription(),
                product.getImage(),
                product.getPrice()
        );
        productRepository.save(newProduct);
    }

    public FullProductDto getProduct(long id) {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)));

        List <String> productsImages = productImagesRepository.
                findByProductId(id).
                stream().
                map(productsImage -> productsImage.getImage()).
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                    }
                    return result;
                }));

        List <ProductsTagDto> productsTags = productTagsRepository.
                findByProductId(id).
                stream().
                map(productTag -> new ProductsTagDto(productTag.getType(), productTag.getValue())).
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                    }
                    return result;
                }));

        return new FullProductDto(id,
                product.get().getName(),
                product.get().getDescription(),
                product.get().getPrice(),
                productsImages,
                productsTags
        );
    }

    public void deleteProduct(long id) {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)));

        productRepository.delete(product.get());
    }

    public List<ProductInfoDto> getPageProducts(int page) {

        return productRepository.
                findByOrderById(PageRequest.of(page, ProductOption.PAGE_COUNT)).
                stream().
                map(product -> new ProductInfoDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage())).
                collect(Collectors.toList());
    }

    public List<ProductInfoDto> searchProductsByNameSortingByParameter(String name, int page, Boolean asc, String parameter) {

        return productRepository.
                getByNameStartingWith(
                        name,
                        PageRequest.of(page, ProductOption.PAGE_COUNT,
                                asc ? Sort.by(parameter).ascending() : Sort.by(parameter).descending())).
                stream().map(product ->
                        new ProductInfoDto(
                                product.getId(),
                                product.getName(),
                                product.getPrice(),
                                product.getImage())).
                collect(Collectors.toList()
                );
    }

    public Long getCountPagesProductsLikeName() {
        return productRepository.count() / ProductOption.PAGE_COUNT;
    }

    public Long getCountPagesProductsLikeName(String name) {
        return productRepository.countByNameStartingWith(name) / ProductOption.PAGE_COUNT;
    }

    public void updateProductById(ProductAllFieldsDto product) {
        productRepository.findByName(product.getName()).
                ifPresent(result -> {
                    throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_WITH_NAME_ALREADY_EXIST);
                });

        productRepository.updateNameAndDescriptionAndPriceById(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getId()
        );
    }
}
