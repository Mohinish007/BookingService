package com.booking.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowDetails extends BaseResponse {

    private Long theatreId;
    private String movieName;
    private LocalDateTime showTime;
    private BigDecimal price;

}
