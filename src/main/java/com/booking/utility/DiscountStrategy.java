package com.booking.utility;

import com.booking.model.request.BookingRequest;
import com.booking.model.response.ShowDetails;

import java.math.BigDecimal;

public interface DiscountStrategy {
    boolean appliesTo(DiscountContext context);
    BigDecimal applyDiscount(BigDecimal totalPrice);
}