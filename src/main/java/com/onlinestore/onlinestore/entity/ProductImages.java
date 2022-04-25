package com.onlinestore.onlinestore.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImages {
    @Id
    private Long id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImages(Long id, String image) {
        this.id = id;
        this.image = image;
    }
}
