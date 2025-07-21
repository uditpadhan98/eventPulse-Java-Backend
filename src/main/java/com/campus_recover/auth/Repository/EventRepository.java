package com.campus_recover.auth.Repository;

import com.campus_recover.auth.Model.Event;
import com.campus_recover.auth.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event,String> {
    List<Event> findByOwnerId(String ownerId);
    List<Event> findByOwner(User owner);
    List<Event> findByOwnerEmail(String email);
}
