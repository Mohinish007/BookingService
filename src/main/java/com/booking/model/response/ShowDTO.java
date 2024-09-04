package com.booking.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ShowDTO {
    private Long id;
    private TheatreDTO theatreDTO;
    private MovieDTO movieDTO;
    private LocalDateTime showTime;
    private List<SeatResponseDTO> seats;
   private BigDecimal price;
}