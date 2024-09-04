package com.booking.service;

import com.booking.clients.TheatreServiceClient;
import com.booking.entites.Booking;
import com.booking.entites.Ticket;
import com.booking.model.request.BookingRequest;
import com.booking.model.request.SeatRequest;
import com.booking.model.request.SeatUpdateRequest;
import com.booking.model.response.BaseResponse;
import com.booking.model.response.BookingResponse;
import com.booking.model.response.ShowDTO;
import com.booking.model.response.ShowDetails;
import com.booking.repository.BookingRepository;
import com.booking.repository.TicketRepository;
import com.booking.utility.DiscountContext;
import com.booking.utility.DiscountStrategy;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private List<DiscountStrategy> discountStrategies;

    private ObjectMapper objectMapper = null;

    @Autowired
    private TheatreServiceClient theatreServiceClient;

    public BookingService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        // Call TheatreService to validate show ID and get show details
        ShowDTO showDetails = theatreServiceClient.getShowById(bookingRequest.getShowId());

        if (showDetails == null) {
            throw new IllegalArgumentException("Invalid show ID.");
        }

        //Lock should have been done already by UI by calling theater seat lock service
        //theatreServiceClient.lockSeat(showDetails.getId(), bookingRequest.getSeatNumbers());
        // Considering the seats lock were already taken, We will book the reserved seats

        BigDecimal subtotal  = calculateTotalPrice(showDetails.getPrice(), bookingRequest);
        BigDecimal grandTotal  = subtotal ;
        DiscountContext context = new DiscountContext(bookingRequest.getUserId(), showDetails.getShowTime());
        for (DiscountStrategy strategy : discountStrategies) {
            if (strategy.appliesTo(context)) {
                grandTotal  = strategy.applyDiscount(subtotal );
                break;
            }
        }
        BigDecimal discount = subtotal.subtract(grandTotal);

        // Create booking
        Booking booking = new Booking();
        booking.setUserId(bookingRequest.getUserId());
        booking.setShowId(showDetails.getId());
        booking.setBookingTime(LocalDateTime.now());
        booking.setSubtotal(subtotal );
        booking.setMovieName(showDetails.getMovieDTO().getTitle());
        booking.setTheatreName(showDetails.getTheatreDTO().getName());
        booking.setGrandTotal(grandTotal );
        booking.setDiscount(discount);

        booking = bookingRepository.save(booking);

        List<SeatRequest> updatedSeats = showDetails.getSeats().stream()
                .filter( s-> bookingRequest.getSeatNumbers().contains(s.getSeatNumber()))
                .map(i -> {
                    return new SeatRequest(i.getId(),i.getSeatNumber(), "BOOKED");
                }).collect(Collectors.toList());

        // Create tickets
        Booking finalBooking = booking;
        List<Ticket> tickets = updatedSeats.stream()
                .map(seatId -> {
                    String seatNumber = seatId.getSeatNumber();
                    BigDecimal seatPrice = showDetails.getPrice();
                    return createTicket(finalBooking, seatNumber, seatPrice);
                })
                .collect(Collectors.toList());

        ticketRepository.saveAll(tickets);





        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest(bookingRequest.getShowId(), updatedSeats);

        theatreServiceClient.updateSeatInventory(showDetails.getId(), seatUpdateRequest);
        BookingResponse bookingResponse = objectMapper.convertValue(booking, BookingResponse.class);
        bookingResponse.setMovieName(showDetails.getMovieDTO().getTitle());
        bookingResponse.setTheatreName(showDetails.getTheatreDTO().getName());
        bookingResponse.setSeatsBooked(tickets.stream().map(Ticket::getSeatNumber).collect(Collectors.toList()));

        return bookingResponse;
    }

    private Ticket createTicket(Booking booking, String seatNumber, BigDecimal seatPrice) {
        Ticket ticket = new Ticket();
        ticket.setBooking(booking);
        ticket.setSeatNumber(seatNumber);
        ticket.setPrice(seatPrice);
        return ticket;
    }

    public BookingResponse getBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found."));
        BookingResponse bookingResponse = objectMapper.convertValue(booking, BookingResponse.class);
        return bookingResponse;
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, BookingRequest request) {
        // Implement price calculation logic based on number of tickets
        return basePrice.multiply(BigDecimal.valueOf(request.getNumberOfTickets()));
    }

    private BigDecimal calculateDiscount(BookingRequest request, ShowDetails showDetails) {
        // Implement discount calculation logic based on business rules
        BigDecimal discount = BigDecimal.ZERO;
        if (request.getNumberOfTickets() >= 3) {
            discount = showDetails.getPrice().multiply(BigDecimal.valueOf(0.5));
        }
        if (showDetails.getShowTime().getHour() >= 12 && showDetails.getShowTime().getHour() < 17) {
            discount = discount.add(showDetails.getPrice().multiply(BigDecimal.valueOf(0.2)));
        }
        return discount;
    }
}
