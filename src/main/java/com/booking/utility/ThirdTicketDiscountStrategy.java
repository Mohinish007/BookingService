package com.booking.utility;

import com.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ThirdTicketDiscountStrategy implements DiscountStrategy {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public boolean appliesTo(DiscountContext context) {
        // Check if this is the third ticket booked by the user
        return context.getUserId() != null && bookingRepository.countByUserId(context.getUserId()) == 2;
    }

    @Override
    public BigDecimal applyDiscount(BigDecimal totalPrice) {
        return totalPrice.multiply(BigDecimal.valueOf(0.5));
    }
}