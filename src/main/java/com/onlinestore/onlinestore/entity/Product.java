package com.onlinestore.onlinestore.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String mainImage;
    private Double price;

    @ManyToMany(fetch = LAZY)
    @EqualsAndHashCode.Exclude
    private Set<ProductImages> productImagesEntities;

    @ManyToMany(fetch = LAZY)
    @EqualsAndHashCode.Exclude
    private Set<ProductTags> productTagsEntities;
}
