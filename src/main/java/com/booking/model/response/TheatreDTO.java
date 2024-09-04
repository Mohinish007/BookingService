package com.booking.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TheatreDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
}