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
public class ProductImages {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String image;
}
