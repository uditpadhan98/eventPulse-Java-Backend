package com.campus_recover.auth.Service;

import com.campus_recover.auth.DTO.BookingRequestDTO;
import com.campus_recover.auth.Model.Booking;
import com.campus_recover.auth.Model.Event;
import com.campus_recover.auth.Model.User;
import com.campus_recover.auth.Model.UserType;
import com.campus_recover.auth.Repository.BookingRepository;
import com.campus_recover.auth.Repository.EventRepository;
import com.campus_recover.auth.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserRepository userRepo;

    public ResponseEntity<?> bookEvent(BookingRequestDTO request, HttpSession session) {
        Optional<User> userOpt = userRepo.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        Optional<Event> eventOpt = eventRepo.findById(request.getEventId());
        if (eventOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Event not found.");
        }

        User user = userOpt.get();

        if (user.getType() != UserType.ATTENDEE) {
            return ResponseEntity.status(403).body("Forbidden: Only attendees can book events.");
        }

        Booking booking = new Booking(
                eventOpt.get(),
                user,
                request.getName(),
                request.getPhone()
        );

        Booking saved = bookingRepo.save(booking);
        return ResponseEntity.status(201).body(saved);
    }

    public ResponseEntity<?> getMyBookings(String userEmail) {
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in first.");
        }

        Optional<User> userOpt = userRepo.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();

        if (user.getType() != UserType.ATTENDEE) {
            return ResponseEntity.status(403).body("Forbidden: Only attendees can view their bookings.");
        }

        List<Booking> bookings = bookingRepo.findByUser(user);
        return ResponseEntity.ok(bookings);
    }

    public ResponseEntity<?> cancelBooking(String bookingId, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in first.");
        }

        Optional<User> userOpt = userRepo.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();

        Optional<Booking> bookingOpt = bookingRepo.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Booking not found.");
        }

        Booking booking = bookingOpt.get();
        if (!booking.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Forbidden: You can only cancel your own bookings.");
        }

        bookingRepo.deleteById(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

}
