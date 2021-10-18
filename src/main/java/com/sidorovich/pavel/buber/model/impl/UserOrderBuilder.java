package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Optional;

public class UserOrderBuilder implements Builder<UserOrderBuilder, UserOrder> {

    private Long id;
    private BuberUser client;
    private Driver driver;
    private BigDecimal price;
    private Coordinates initialCoordinates;
    private Coordinates endCoordinates;
    private OrderStatus status;

    private UserOrderBuilder() {
    }

    private static class InstanceCreator {
        static UserOrderBuilder INSTANCE = new UserOrderBuilder();
    }

    public static UserOrderBuilder getInstance() {
        return UserOrderBuilder.InstanceCreator.INSTANCE;
    }

    public UserOrderBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public UserOrderBuilder setClient(BuberUser client) {
        this.client = client;
        return this;
    }

    public UserOrderBuilder setDriver(Driver driver) {
        this.driver = driver;
        return this;
    }

    public UserOrderBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public UserOrderBuilder setInitialCoordinates(Coordinates initialCoordinates) {
        this.initialCoordinates = initialCoordinates;
        return this;
    }

    public UserOrderBuilder setEndCoordinates(Coordinates endCoordinates) {
        this.endCoordinates = endCoordinates;
        return this;
    }

    public UserOrderBuilder setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public UserOrderBuilder reset() {
        this.id = null;
        this.client = null;
        this.driver = null;
        this.price = null;
        this.initialCoordinates = null;
        this.endCoordinates = null;
        this.status = null;
        return this;
    }

    /**
     * Automatically calls reset() method
     *
     * @return optional value of UserOrder
     */
    @Override
    public Optional<UserOrder> getResult() {
        if (client == null || driver == null ||
            price == null || initialCoordinates == null ||
            endCoordinates == null || status == null) {
            reset();
            return Optional.empty();
        }
        Optional<UserOrder> order = Optional.of(
                new UserOrder(
                        id, client, driver,
                        price, initialCoordinates,
                        endCoordinates, status
                )
        );
        reset();
        return order;
    }
}
