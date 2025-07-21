package com.campus_recover.auth.Controller;

import com.campus_recover.auth.Model.Event;
import com.campus_recover.auth.Model.User;
import com.campus_recover.auth.Repository.EventRepository;
import com.campus_recover.auth.Repository.UserRepository;
import com.campus_recover.auth.Service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create-events")
    public ResponseEntity<?> createEvent(@RequestBody Event eventRequest, HttpSession session) {
        return eventService.createEvent(eventRequest, session);
    }

    // Get all events
    @GetMapping("/get-all-events")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    //Get user events
    @GetMapping("/my-events")
    public ResponseEntity<?> getEventsOfLoggedInUser(HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");
//        System.out.println("G"+userEmail);

        if (userEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in.");
        }

        List<Event> events = eventService.getEventsByOwnerEmail(userEmail);
        return ResponseEntity.ok(events);
    }

    // Get a single event by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            return ResponseEntity.ok(eventOpt.get());
        } else {
            return ResponseEntity.status(404).body("Event not found with id: " + id);
        }
    }



    // Update event by ID
    @PutMapping("/update-events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable String id, @RequestBody Event updatedEvent, HttpSession session) {
        return eventService.updateEvent(id, updatedEvent, session);
    }

    // Delete event by ID
    @DeleteMapping("/delete-events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id, HttpSession session) {
        return eventService.deleteEvent(id, session);
    }

}
