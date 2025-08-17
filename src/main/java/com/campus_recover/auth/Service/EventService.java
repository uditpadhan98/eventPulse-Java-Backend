package com.campus_recover.auth.Service;

import com.campus_recover.auth.Model.Event;
import com.campus_recover.auth.Model.User;
import com.campus_recover.auth.Repository.BookingRepository;
import com.campus_recover.auth.Repository.EventRepository;
import com.campus_recover.auth.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BookingRepository bookingRepo;

    public ResponseEntity<?> createEvent(Event eventRequest, HttpSession session) {
        try {
            String userEmail = (String) session.getAttribute("userEmail");

            if (userEmail == null) {
                return ResponseEntity.status(401).body("Unauthorized: Please log in.");
            }

            Optional<User> userOpt = userRepo.findByEmail(userEmail);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found.");
            }

            User user = userOpt.get();

            if (user.getType() == null || !user.getType().name().equalsIgnoreCase("ORGANISER")) {
                return ResponseEntity.status(403).body("Forbidden: Only organisers can create events.");
            }

            Event event = new Event();
            event.setOwner(user);
            event.setOrganiser(user.getClub());
            event.setTitle(eventRequest.getTitle());
            event.setAddress(eventRequest.getAddress());
            event.setPhotos(eventRequest.getPhotos());
            event.setDescription(eventRequest.getDescription());
            event.setDate(eventRequest.getDate());
            event.setTime(eventRequest.getTime());

            Event savedEvent = eventRepo.save(event);
            return ResponseEntity.status(201).body(savedEvent);

        } catch (Exception e) {
            e.printStackTrace(); // Print full stack trace for detailed error
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    public ResponseEntity<?> updateEvent(String id, Event updatedEvent, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in.");
        }

        Optional<User> userOpt = userRepo.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        Optional<Event> eventOpt = eventRepo.findById(id);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Event not found.");
        }

        Event event = eventOpt.get();

        if (!event.getOwner().getEmail().equals(userEmail)) {
            return ResponseEntity.status(403).body("Forbidden: You can only update your own event.");
        }

        event.setTitle(updatedEvent.getTitle());
        event.setAddress(updatedEvent.getAddress());
        event.setPhotos(updatedEvent.getPhotos());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setTime(updatedEvent.getTime());

        Event saved = eventRepo.save(event);
        return ResponseEntity.ok(saved);
    }

    @Transactional
    public ResponseEntity<?> deleteEvent(String id, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in.");
        }

        Optional<Event> eventOpt = eventRepo.findById(id);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Event not found.");
        }

        Event event = eventOpt.get();

        if (!event.getOwner().getEmail().equals(userEmail)) {
            return ResponseEntity.status(403).body("Forbidden: You can only delete your own event.");
        }

        // ✅ First delete related bookings
        // ✅ Delete all bookings linked to this event
        long deletedBookings = bookingRepo.deleteAllByEvent(event);

        // ✅ Then delete the event
        eventRepo.delete(event);
        return ResponseEntity.ok("Event deleted successfully." + deletedBookings + " related bookings removed.");
    }

    public List<Event> getEventsByOwnerEmail(String email) {
        Optional<User> userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            return List.of(); // return empty list if user not found
        }
        return eventRepo.findByOwner(userOpt.get());
    }

    public Optional<Event> getEventById(String id) {
        return eventRepo.findById(id);
    }

}
