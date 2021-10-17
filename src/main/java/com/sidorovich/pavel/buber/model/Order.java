package com.sidorovich.pavel.buber.model;

import java.math.BigDecimal;

public interface Order {

    BigDecimal getPrice();

    OrderStatus getStatus();

}
