package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dao.ProductDAO;
import com.onlinestore.onlinestore.dao.ProductImagesDAO;
import com.onlinestore.onlinestore.dao.ProductTagsDAO;
import com.onlinestore.onlinestore.dto.request.ProductFullInfoAddUpdateDto;
import com.onlinestore.onlinestore.dto.response.ProductFullInfoDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.dto.response.ProductsTagDto;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.entity.ProductImages;
import com.onlinestore.onlinestore.entity.ProductTags;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
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
    private final ProductDAO productDAO;
    private final ProductImagesDAO productImagesDAO;
    private final ProductTagsDAO productTagsDAO;

    public void addProduct(ProductFullInfoAddUpdateDto product) {
        productDAO.findByName(product.getName())
                .ifPresent(result -> {
                    throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
                });

        Product newProduct = new Product(
                product.getName(),
                product.getDescription(),
                product.getImage(),
                product.getPrice()
        );

        productDAO.save(newProduct);
        Long newProductId =  productDAO
                .findByName(product.getName()).get().getId();

        product.getTags()
                .forEach(productsTag ->
                        productTagsDAO.save(new ProductTags(
                                newProductId,
                                productsTag.getType(),
                                productsTag.getValue())
                        )
                );
        product.getImages()
                .forEach(productImage ->
                        productImagesDAO.save(new ProductImages(
                                newProductId,
                                productImage)
                        )
                );
    }

    public ProductFullInfoDto getProduct(Long id) {
        Optional<Product> product = Optional.ofNullable(productDAO.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)));

        List<String> productsImages = productImagesDAO
                .findByProductId(id)
                .stream()
                .map(productsImage -> productsImage.getImage())
                .collect(Collectors.
                        collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty())
                                throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                            return result;
                        })
                );

        List<ProductsTagDto> productsTags = productTagsDAO
                .findByProductId(id)
                .stream()
                .map(productTag -> new ProductsTagDto(productTag.getType(), productTag.getValue()))
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty())
                                throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
                            return result;
                        })
                );

        return new ProductFullInfoDto(
                id,
                product.get().getName(),
                product.get().getDescription(),
                product.get().getPrice(),
                productsImages,
                productsTags
        );
    }

    public void deleteProduct(String name) {
        Optional<Product> product = Optional.ofNullable(productDAO.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND)));

        Long id = product.get().getId();
        productDAO.deleteById(id);
        productImagesDAO.deleteById(id);
        productTagsDAO.deleteById(id);
    }

    public List<ProductInfoDto> getPageProducts(int page) {

        return productDAO
                .findByOrderById(PageRequest.of(page, ProductOption.PAGE_COUNT))
                .stream()
                .map(product -> new ProductInfoDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage()))
                .collect(Collectors.toList());
    }

    public List<ProductInfoDto> searchProductsByNameSortingByParameter(String name, int page, Boolean asc, String parameter) {

        return productDAO
                .getByNameStartingWith(
                        name,
                        PageRequest.of(page, ProductOption.PAGE_COUNT,
                                asc ? Sort.by(parameter).ascending() : Sort.by(parameter).descending()))
                .stream()
                .map(product -> new ProductInfoDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage()))
                .collect(Collectors.toList());
    }

    public Long getCountPagesProductsLikeName() {
        return productDAO.count() / ProductOption.PAGE_COUNT;
    }

    public Long getCountPagesProductsLikeName(String name) {
        return productDAO.countByNameStartingWith(name) / ProductOption.PAGE_COUNT;
    }

    public void updateProductById(ProductFullInfoAddUpdateDto product) {
        Product updatedProduct = productDAO
                .findByName(product.getName())
                .orElseThrow(() -> new ProductAlreadyExistException(ErrorMessage.PRODUCT_NOT_FOUND));

        productDAO
                .updateNameAndDescriptionAndPriceById(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        updatedProduct.getId()
                );
    }
}
