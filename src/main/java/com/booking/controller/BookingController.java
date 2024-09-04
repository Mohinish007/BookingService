package com.booking.controller;


import com.booking.entites.Booking;
import com.booking.model.request.BookingRequest;
import com.booking.model.response.BookingResponse;
import com.booking.service.BookingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "BookingService")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @ApiOperation(value = "Create a new booking", notes = "Creates a new booking for a given show and user.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created booking"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest bookingRequest) {
        BookingResponse booking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.ok(booking);
    }

    @ApiOperation(value = "Get booking details", notes = "Retrieves booking details by booking ID.")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        BookingResponse booking = bookingService.getBooking(id);
        return ResponseEntity.ok(booking);
    }
}
