package com.booking.model.request;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    private Long userId;
    private Long showId;
    private int numberOfTickets;
    private List<String> seatNumbers;

}
