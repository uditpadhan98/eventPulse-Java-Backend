package com.campus_recover.auth.Service;

import com.campus_recover.auth.DTO.UserRequestDTO;
import com.campus_recover.auth.Model.User;
import com.campus_recover.auth.Model.UserType;
import com.campus_recover.auth.Repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user
    public String register(User user) {

        if (user.getType() == UserType.ORGANISER && (user.getClub() == null || user.getClub().isEmpty())) {
            throw new IllegalArgumentException("Club is required for organisers.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered!";
        }

        // Ideally: hash the password here before saving (e.g., using BCrypt)
        // ✅ Hash the password
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return "User registered successfully!";
    }

    // Login user
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

//        if (!user.getPassword().equals(password)) {
//            return Optional.empty();
//        }
        // ✅ Check raw password against hashed one
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }

    // Logout logic
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("User logged out successfully!");
    }

    // Get all users
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Edit profile logic
    public ResponseEntity<String> editProfile(String id, UserRequestDTO updatedUser, HttpSession session) {
        String loggedInEmail = (String) session.getAttribute("userEmail");
        if (loggedInEmail == null) {
            return ResponseEntity.status(401).body("Unauthorized: Please log in first.");
        }

        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOpt.get();

        if (!existingUser.getEmail().equals(loggedInEmail)) {
            return ResponseEntity.status(403).body("Forbidden: You can only edit your own profile.");
        }

        existingUser.setName(updatedUser.getName());
        existingUser.setClub(updatedUser.getClub());
        existingUser.setPassword(updatedUser.getPassword()); // TODO: hash password

        userRepository.save(existingUser);
        return ResponseEntity.ok("Profile updated successfully!");
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }
}
