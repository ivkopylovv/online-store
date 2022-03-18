package com.onlinestore.onlinestore.utility.embeddable;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BasketId implements Serializable {
    private Long userId;
    private Long productId;

    public BasketId() {
    }

    public BasketId(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketId basketId = (BasketId) o;
        return Objects.equals(userId, basketId.userId) && Objects.equals(productId, basketId.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }

}
