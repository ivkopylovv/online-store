package com.onlinestore.onlinestore.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String image;
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
    private Set<ProductImages> productImagesEntities;

    @OneToMany(mappedBy = "product")
    private Set<ProductTags> productTagsEntities;

    public Product(String name, String description, String image, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }
}
