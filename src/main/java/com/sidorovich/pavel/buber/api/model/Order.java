package com.sidorovich.pavel.buber.api.model;

import java.math.BigDecimal;

public interface Order {

    BigDecimal getPrice();

    OrderStatus getStatus();

}
