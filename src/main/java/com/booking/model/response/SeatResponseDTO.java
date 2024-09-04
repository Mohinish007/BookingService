package com.booking.model.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatResponseDTO {
    private Long id;
    private String seatNumber;
    private String status;

}