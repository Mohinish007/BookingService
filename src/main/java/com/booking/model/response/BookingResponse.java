package com.booking.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponse extends BaseResponse{
    private Long userId;
    private Long showId;
    private String movieName;
    private String TheatreName;
    private LocalDateTime bookingTime;
    private BigDecimal subtotal;
    private BigDecimal grandTotal;
    private BigDecimal discount;
    private List<String> seatsBooked;
}