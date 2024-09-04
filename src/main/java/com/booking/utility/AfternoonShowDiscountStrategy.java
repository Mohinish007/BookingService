package com.booking.utility;


import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class AfternoonShowDiscountStrategy implements DiscountStrategy {
    @Override
    public boolean appliesTo(DiscountContext context) {
        // Check if the showtime is in the afternoon (e.g., between 12:00 PM and 04:00 PM)
        LocalDateTime showDateTime = context.getShowTime();
        LocalTime showTime = showDateTime.toLocalTime();
        return showTime.isAfter(LocalTime.of(12, 0)) && showTime.isBefore(LocalTime.of(16, 0));
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal totalPrice) {
        return totalPrice.multiply(BigDecimal.valueOf(0.8));
    }
}