package com.onlinestore.onlinestore.entity;


import javax.persistence.*;

@Entity
public class ProductImagesEntity {
    @Id
    private Long id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    public ProductImagesEntity(Long id, String image) {
        this.id = id;
        this.image = image;
    }

    public ProductImagesEntity() {
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
