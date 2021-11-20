package com.sidorovich.pavel.buber.api.model;

import java.math.BigDecimal;
import java.util.Objects;

public class UserOrder extends CommonEntity<UserOrder> implements Order {

    private final BuberUser client;
    private final Driver driver;
    private final BigDecimal price;
    private final Coordinates initialCoordinates;
    private final Coordinates endCoordinates;
    private final OrderStatus status;

    /* can be instantiated only using builder */
    UserOrder(Long id, BuberUser client, Driver driver, BigDecimal price,
              Coordinates initialCoordinates, Coordinates endCoordinates,
              OrderStatus status) {
        super(id);
        this.client = client;
        this.driver = driver;
        this.price = price;
        this.initialCoordinates = initialCoordinates;
        this.endCoordinates = endCoordinates;
        this.status = status;
    }

    @Override
    public UserOrder withId(Long id) {
        return new UserOrder(id, client, driver, price, initialCoordinates, endCoordinates, status);
    }

    public BuberUser getClient() {
        return client;
    }

    public Driver getDriver() {
        return driver;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    public Coordinates getInitialCoordinates() {
        return initialCoordinates;
    }

    public Coordinates getEndCoordinates() {
        return endCoordinates;
    }

    @Override
    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserOrder userOrder = (UserOrder) o;
        return Objects.equals(id, userOrder.id) && Objects.equals(client, userOrder.client) &&
               Objects.equals(driver, userOrder.driver) && Objects.equals(price, userOrder.price) &&
               Objects.equals(initialCoordinates, userOrder.initialCoordinates) &&
               Objects.equals(endCoordinates, userOrder.endCoordinates) && status == userOrder.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, driver, price, initialCoordinates, endCoordinates, status);
    }
}
