package com.sidorovich.pavel.buber.model.impl;

import java.sql.Date;
import java.util.Objects;

public class Bonus extends CommonEntity<Bonus> {

    private final Long clientId;
    private final Double discount;
    private final Date expires;

    public Bonus(Long id, Long clientId, Double discount, Date expires) {
        super(id);
        this.clientId = clientId;
        this.discount = discount;
        this.expires = expires;
    }

    public Bonus(Long clientId, Double discount, Date expires) {
        this(null, clientId, discount, expires);
    }

    @Override
    public Bonus withId(Long id) {
        return new Bonus(id, clientId, discount, expires);
    }

    public Double getDiscount() {
        return discount;
    }

    public Date getExpireDate() {
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
