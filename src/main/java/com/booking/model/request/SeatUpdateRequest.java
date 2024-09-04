package com.booking.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatUpdateRequest {
   // private Long theatreId;
    private Long showId;
    List<SeatRequest> seatRequestList;

}