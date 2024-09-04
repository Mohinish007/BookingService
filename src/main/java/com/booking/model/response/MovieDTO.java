package com.booking.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private String genre;
    private String language;
}