package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.embeddable.BasketId;

import javax.persistence.*;

@Entity
public class BasketEntity {
    @EmbeddedId
    private BasketId basketId;

    public BasketEntity() {
    }

    public BasketEntity(BasketId basketId) {
        this.basketId = basketId;
    }

    public BasketId getBasketId() {
        return basketId;
    }

    public void setBasketId(BasketId basketId) {
        this.basketId = basketId;
    }
}
