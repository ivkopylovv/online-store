package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.embeddable.CartId;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
    @EmbeddedId
    private CartId cartId;
}
