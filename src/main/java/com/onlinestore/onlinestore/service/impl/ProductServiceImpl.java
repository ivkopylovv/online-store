package com.onlinestore.onlinestore.service.impl;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dao.ProductDAO;
import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.exception.ResourceNotFoundException;
import com.onlinestore.onlinestore.mapper.ProductMapper;
import com.onlinestore.onlinestore.service.ProductService;
import com.onlinestore.onlinestore.utility.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public Product getProduct(Long id) {
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<ProductDTO> getProducts(int page) {
        List<Product> products = productDAO
                .findAll(PageRequest.of(page, ProductOption.PAGE_COUNT))
                .getContent();

        return ProductMapper.entityListToDTOList(products);
    }

    @Override
    public List<ProductDTO> getSortedProducts(int page, boolean asc, String attribute) {
        List<Product> products = productDAO
                .findAll(PageHelper.getPageableScriptWithSort(page, asc, attribute))
                .getContent();

        return ProductMapper.entityListToDTOList(products);
    }

    @Override
    public List<ProductDTO> findProductsByTitle(int page, String title) {
        List<Product> products = productDAO
                .findByTitleContaining(title, PageHelper.getPageableScript(page));

        return ProductMapper.entityListToDTOList(products);
    }

    @Override
    public List<ProductDTO> findSortedProductsByTitle(int page, boolean asc, String attribute, String title) {
        List<Product> products = productDAO
                .findByTitleContaining(title, PageHelper.getPageableScriptWithSort(page, asc, attribute));

        return ProductMapper.entityListToDTOList(products);
    }
}
