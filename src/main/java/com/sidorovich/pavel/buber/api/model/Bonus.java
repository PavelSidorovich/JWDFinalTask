package com.sidorovich.pavel.buber.api.model;

import java.sql.Date;
import java.util.Objects;

public class Bonus extends CommonEntity<Bonus> {

    private final Double discount;
    private final Date expires;
    private final BuberUser client;

    public Bonus(Long id, Double discount, Date expires, BuberUser client) {
        super(id);
        this.discount = discount;
        this.expires = expires;
        this.client = client;
    }

    public Bonus(Double discount, Date expires, BuberUser client) {
        this(null, discount, expires, client);
    }

    @Override
    public Bonus withId(Long id) {
        return new Bonus(id, discount, expires, client);
    }

    public Double getDiscount() {
        return discount;
    }

    public Date getExpireDate() {
        return expires;
    }

    public BuberUser getClient() {
        return client;
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
        return Objects.equals(discount, bonus.discount) && Objects.equals(expires, bonus.expires) &&
               Objects.equals(client, bonus.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discount, expires, client);
    }

    @Override
    public String toString() {
        return "Bonus{" +
               "discount=" + discount +
               ", expires=" + expires +
               ", client=" + client +
               ", expireDate=" + getExpireDate() +
               ", id=" + id +
               '}';
    }

}
