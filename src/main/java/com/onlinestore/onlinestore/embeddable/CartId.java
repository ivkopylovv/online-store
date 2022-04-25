package com.onlinestore.onlinestore.embeddable;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CartId implements Serializable {
    private Long userId;
    private Long productId;
}
