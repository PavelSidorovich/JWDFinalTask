package com.sidorovich.pavel.buber.api.model;

import java.sql.Date;
import java.util.Objects;

public class Bonus extends CommonEntity<Bonus> {

    private final Double discount;
    private final Date expires;

    public Bonus(Long id, Double discount, Date expires) {
        super(id);
        this.discount = discount;
        this.expires = expires;
    }

    public Bonus(Double discount, Date expires) {
        this(null, discount, expires);
    }

    @Override
    public Bonus withId(Long id) {
        return new Bonus(id, discount, expires);
    }

    public Double getDiscount() {
        return discount;
    }

    public Date getExpireDate() {
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
        return Objects.equals(discount, bonus.discount) && Objects.equals(expires, bonus.expires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discount, expires);
    }

    @Override
    public String toString() {
        return "Bonus{" +
               "discount=" + discount +
               ", expires=" + expires +
               ", id=" + id +
               '}';
    }

}
