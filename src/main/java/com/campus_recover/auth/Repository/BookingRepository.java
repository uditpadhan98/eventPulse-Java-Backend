package com.campus_recover.auth.Repository;

import com.campus_recover.auth.Model.Booking;
import com.campus_recover.auth.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking,String> {
    List<Booking> findByUser(User user);
}
