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
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.repository.ProductImagesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.ProductTagsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        List <ProductImages> productImagesEntities = productImagesRepository.
                findByProductId(id).
                stream().
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                    }
                    return result;
                }));

        List <ProductTags> productTagsEntities = productTagsRepository.
                findByProductId(id).
                stream().
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                    }
                    return result;
                }));

        List <ProductsTagDto> productsTags = new ArrayList <ProductsTagDto>();
        List <String> productsImages = new ArrayList<>();

        for (ProductImages productImage: productImagesEntities) {
            productsImages.add(productImage.getImage());
        }

        for (ProductTags productTag: productTagsEntities) {
            productsTags.add(new ProductsTagDto(productTag.getType(), productTag.getValue()));
        }

        return new FullProductDto(id,
                product.get().getName(),
                product.get().getDescription(),
                product.get().getPrice(),
                productsImages, productsTags);
    }

    public void deleteProduct(long id) {
        Optional<Product> product = Optional.ofNullable(productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)));

        productRepository.delete(product.get());
    }

    public List<ProductInfoDto> getPageProducts(int page) {
        List<Product> products = productRepository.
                findByOrderById(PageRequest.of(page, ProductOption.PAGE_COUNT));
        List<ProductInfoDto> productsDto = new ArrayList<>();

        for (Product item : products) {
            productsDto.add(new ProductInfoDto(item.getId(), item.getName(), item.getPrice(), item.getImage()));
        }

        return productsDto;
    }

    public List<ProductInfoDto> searchProductsByNameSortingByParameter(String name, int page, Boolean asc, String parameter) {
        List<Product> products;

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
        List<ProductInfoDto> productsDto = new ArrayList<>();

        for (Product item : products) {
            productsDto.add(new ProductInfoDto(item.getId(), item.getName(), item.getPrice(), item.getImage()));
        }

        return productsDto;
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
