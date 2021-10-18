package com.sidorovich.pavel.buber.model.impl;

import java.sql.Date;
import java.util.Objects;
import java.util.Optional;

public class Bonus {

    private Long id; // can be null
    private final Long clientId;
    private final Double discount;
    private final Date expires;

    public Bonus(Long id, Long clientId, Double discount, Date expires) {
        this.id = id;
        this.clientId = clientId;
        this.discount = discount;
        this.expires = expires;
    }

    public Bonus(Long clientId, Double discount, Date expires) {
        this(null, clientId, discount, expires);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(this.id);
    }

    public Double getDiscount() {
        return discount;
    }

    public Date getExpires() {
        return expires;
    }

    public Long getClientId() {
        return clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bonus bonus = (Bonus) o;
        return Objects.equals(id, bonus.id) && Objects.equals(clientId, bonus.clientId) &&
               Objects.equals(discount, bonus.discount) && Objects.equals(expires, bonus.expires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, discount, expires);
    }

    @Override
    public String toString() {
        return "Bonus{" +
               "id=" + id +
               ", clientId=" + clientId +
               ", discount=" + discount +
               ", expires=" + expires +
               '}';
    }
}
