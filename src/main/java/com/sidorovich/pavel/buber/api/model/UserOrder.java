package com.sidorovich.pavel.buber.api.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class UserOrder extends CommonEntity<UserOrder> implements Order {

    private final BuberUser client;
    private final Driver driver;
    private final BigDecimal price;
    private final Coordinates initialCoordinates;
    private final Coordinates endCoordinates;
    private final OrderStatus status;
    private final Date dateOfTrip;

    /* can be instantiated only using builder */
    private UserOrder(Long id, BuberUser client, Driver driver, BigDecimal price,
                      Coordinates initialCoordinates, Coordinates endCoordinates,
                      OrderStatus status, Date dateOfTrip) {
        super(id);
        this.client = client;
        this.driver = driver;
        this.price = price;
        this.initialCoordinates = initialCoordinates;
        this.endCoordinates = endCoordinates;
        this.status = status;
        this.dateOfTrip = dateOfTrip;
    }

    @Override
    public UserOrder withId(Long id) {
        return new UserOrder(id, client, driver, price, initialCoordinates, endCoordinates, status, dateOfTrip);
    }

    public UserOrder withDriver(Driver driver) {
        return new UserOrder(
                id, client, driver, price,
                initialCoordinates, endCoordinates, status,
                dateOfTrip);
    }

    public UserOrder withStatus(OrderStatus status) {
        return new UserOrder(
                id, client, driver, price,
                initialCoordinates, endCoordinates, status,
                dateOfTrip);
    }

    public UserOrder withPrice(BigDecimal price) {
        return new UserOrder(
                id, client, driver, price,
                initialCoordinates, endCoordinates, status,
                dateOfTrip);
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

    public Date getDateOfTrip() {
        return dateOfTrip;
    }

    public UserOrder withDateOfTrip(Date date) {
        return new UserOrder(
                id, client, driver, price,
                initialCoordinates, endCoordinates, status,
                date);
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
        return Objects.equals(client, userOrder.client) && Objects.equals(driver, userOrder.driver) &&
               Objects.equals(price, userOrder.price) &&
               Objects.equals(initialCoordinates, userOrder.initialCoordinates) &&
               Objects.equals(endCoordinates, userOrder.endCoordinates) && status == userOrder.status &&
               Objects.equals(dateOfTrip, userOrder.dateOfTrip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, driver, price, initialCoordinates, endCoordinates, status, dateOfTrip);
    }

    @Override
    public String toString() {
        return "UserOrder{" +
               "id=" + id +
               ", client=" + client +
               ", driver=" + driver +
               ", price=" + price +
               ", initialCoordinates=" + initialCoordinates +
               ", endCoordinates=" + endCoordinates +
               ", status=" + status +
               ", dateOfTrip=" + dateOfTrip +
               '}';
    }

    public static OrderBuilder with() {
        return new OrderBuilder();
    }

    public static class OrderBuilder implements Builder<UserOrder> {

        private Long id;
        private BuberUser client;
        private Driver driver;
        private BigDecimal price;
        private Coordinates initialCoordinates;
        private Coordinates endCoordinates;
        private OrderStatus status;
        private Date dateOfTrip;

        private OrderBuilder() {
        }

        public OrderBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder client(BuberUser client) {
            this.client = client;
            return this;
        }

        public OrderBuilder driver(Driver driver) {
            this.driver = driver;
            return this;
        }

        public OrderBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderBuilder initialCoordinates(Coordinates initialCoordinates) {
            this.initialCoordinates = initialCoordinates;
            return this;
        }

        public OrderBuilder endCoordinates(Coordinates endCoordinates) {
            this.endCoordinates = endCoordinates;
            return this;
        }

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder dateOfTrip(Date date) {
            this.dateOfTrip = date;
            return this;
        }

        @Override
        public void reset() {
            id = null;
            client = null;
            driver = null;
            price = null;
            initialCoordinates = null;
            endCoordinates = null;
            status = null;
            dateOfTrip = null;
        }

        @Override
        public UserOrder build() {
            return new UserOrder(
                    id, client, driver, price,
                    initialCoordinates, endCoordinates, status,
                    dateOfTrip);
        }
    }

}
