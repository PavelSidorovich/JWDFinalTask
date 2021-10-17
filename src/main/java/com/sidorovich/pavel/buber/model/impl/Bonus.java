package com.sidorovich.pavel.buber.model.impl;

import java.sql.Date;
import java.util.Objects;
import java.util.Optional;

public class Bonus {

    private Integer id; // can be null
    private final Double discount;
    private final Date expires;

    public Bonus(Integer id, Double discount, Date expires) {
        this.id = id;
        this.discount = discount;
        this.expires = expires;
    }

    public Bonus(Double discount, Date expires) {
        this(null, discount, expires);
    }

    public Optional<Integer> getId() {
        return Optional.ofNullable(this.id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDiscount() {
        return discount;
    }

    public Date getExpires() {
        return expires;
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
        return Objects.equals(id, bonus.id) && Objects.equals(discount, bonus.discount) &&
               Objects.equals(expires, bonus.expires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, discount, expires);
    }
}
