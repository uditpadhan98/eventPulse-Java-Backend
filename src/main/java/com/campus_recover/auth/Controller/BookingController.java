package com.campus_recover.auth.Controller;

import com.campus_recover.auth.DTO.BookingRequestDTO;
import com.campus_recover.auth.Model.Booking;
import com.campus_recover.auth.Service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event-book")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<?> bookEvent(@RequestBody BookingRequestDTO requestDTO, HttpSession session) {
        return bookingService.bookEvent(requestDTO, session);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(@RequestHeader("X-User-Email") String userEmail) {
        return bookingService.getMyBookings(userEmail);
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable String bookingId, HttpSession session) {
        return bookingService.cancelBooking(bookingId, session);
    }

}
