package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.ProductAllFieldsDto;
import com.onlinestore.onlinestore.dto.response.FullProductDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.dto.response.ProductsTagDto;
import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.entity.ProductImagesEntity;
import com.onlinestore.onlinestore.entity.ProductTagsEntity;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.repository.ProductImagesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.ProductTagsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final ProductTagsRepository productTagsRepository;

    public ProductService(ProductRepository productRepository,
                          ProductImagesRepository productImagesRepository,
                          ProductTagsRepository productTagsRepository) {
        this.productRepository = productRepository;
        this.productImagesRepository = productImagesRepository;
        this.productTagsRepository = productTagsRepository;
    }

    public ProductEntity addProduct(ProductAllFieldsDto product) {
        if (productRepository.findByName(product.getName()) != null) {
            throw new ProductAlreadyExistException(ErrorMessage.PRODUCT_ALREADY_EXIST);
        }

        ProductEntity newProduct = new ProductEntity(
                product.getName(),
                product.getDescription(),
                product.getImage(),
                product.getPrice()
        );

        return productRepository.save(newProduct);
    }

    public FullProductDto getProduct(long id) {
        ProductEntity product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        List <ProductImagesEntity> productImagesEntities = productImagesRepository.findByProductId(id);

        if (productImagesEntities.isEmpty()) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND);
        }

        List <ProductTagsEntity> productTagsEntities = productTagsRepository.findByProductId(id);

        if (productTagsEntities.isEmpty()) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_TAGS_NOT_FOUND);
        }

        ArrayList <ProductsTagDto> productsTags = new ArrayList <ProductsTagDto>();
        ArrayList <String> productsImages = new ArrayList<>();

        for (ProductImagesEntity productImage: productImagesEntities) {
            productsImages.add(productImage.getImage());
        }

        for (ProductTagsEntity productTag: productTagsEntities) {
            productsTags.add(new ProductsTagDto(productTag.getType(), productTag.getValue()));
        }

        return new FullProductDto(id,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                productsImages, productsTags);
    }

    public void deleteProduct(long id) throws ProductNotFoundException {
        ProductEntity product = productRepository.findById(id).get();

        if (product == null) {
            throw new ProductNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        productRepository.delete(product);
    }

    public List<ProductInfoDto> getPageProducts(int page) {
        List<ProductEntity> productsEntity = productRepository.findByOrderById(PageRequest.of(page, ProductOption.countPage));
        List<ProductInfoDto> productsDto = new ArrayList<ProductInfoDto>();

        for (ProductEntity item : productsEntity) {
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
        List<ProductEntity> productsEntity = new ArrayList<ProductEntity>();
        if (asc) {
            productsEntity = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, ProductOption.countPage, Sort.by(parameter).ascending())
            );
        } else {
            productsEntity = productRepository.getByNameStartingWith(
                    name,
                    PageRequest.of(page, ProductOption.countPage, Sort.by(parameter).descending())
            );
        }
        List<ProductInfoDto> productsDto = new ArrayList<ProductInfoDto>();

        for (ProductEntity item : productsEntity) {
            productsDto.add(new ProductInfoDto(item.getId(), item.getName(), item.getPrice(), item.getImage()));
        }

        return productsDto;
    }

    public long getCountPagesProductsLikeName() {
        return productRepository.count() / ProductOption.countPage;
    }

    public long getCountPagesProductsLikeName(String name) {

        return productRepository.countByNameStartingWith(name) / ProductOption.countPage;
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
