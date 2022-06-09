package com.onlinestore.onlinestore.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductTags {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String type;
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductTags(Long id, String type, String value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

}
