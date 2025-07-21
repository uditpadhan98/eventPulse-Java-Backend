package com.campus_recover.auth.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @DBRef
    private Event event;

    @DBRef
    private User user;

    private String name;
    private String phone;

    @Transient // <-- used only for request
    private String eventId;

    public Booking() {}

    public Booking(Event event, User user, String name, String phone) {
        this.event = event;
        this.user = user;
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public String getId() { return id; }

    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}

