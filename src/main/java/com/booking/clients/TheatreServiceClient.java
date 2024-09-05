package com.booking.clients;

import com.booking.model.request.SeatUpdateRequest;
import com.booking.model.response.ShowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "theatreService", url = "http://localhost:8082")
public interface TheatreServiceClient {

    @PutMapping("theatreService/api/shows/{showId}/seats/{seatNumber}/lock")
    void lockSeat(@PathVariable("showId") Long showId, @RequestBody List<String> seatNumbers);

    @PutMapping("theatreService/api/shows/{showId}/seats/{seatId}/unlock")
    void unlockSeat(@PathVariable("showId") Long showId, @RequestBody List<String> seatNumbers);

    @PutMapping("theatreService/api/show/{showId}/seat/updateSeatInventory")
    void updateSeatInventory(@PathVariable("showId") Long showId, @RequestBody SeatUpdateRequest seatUpdateRequests);

    @GetMapping("theatreService/theater/show/{id}")
    ShowDTO getShowById(@PathVariable("id") Long id);
}
