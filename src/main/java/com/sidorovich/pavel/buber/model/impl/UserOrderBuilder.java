package com.sidorovich.pavel.buber.model.impl;

import com.sidorovich.pavel.buber.model.Builder;
import com.sidorovich.pavel.buber.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Optional;

public class UserOrderBuilder implements Builder<UserOrder> {

    private Integer id;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setClient(BuberUser client) {
        this.client = client;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setInitialCoordinates(Coordinates initialCoordinates) {
        this.initialCoordinates = initialCoordinates;
    }

    public void setEndCoordinates(Coordinates endCoordinates) {
        this.endCoordinates = endCoordinates;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public void reset() {
        this.id = null;
        this.client = null;
        this.driver = null;
        this.price = null;
        this.initialCoordinates = null;
        this.endCoordinates = null;
        this.status = null;
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
