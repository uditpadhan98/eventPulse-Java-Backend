package com.campus_recover.auth.Controller;

import com.campus_recover.auth.DTO.LoginRequestDTO;
import com.campus_recover.auth.DTO.UserRequestDTO;
import com.campus_recover.auth.Model.User;
import com.campus_recover.auth.Service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String result = authService.register(user);
        if (result.equals("User registered successfully!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO loginRequest, HttpSession session) {
        Optional<User> userOpt = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }
        User user = userOpt.get();
        // Store email in session
        session.setAttribute("userEmail", user.getEmail());
        // Optional: exclude password from the returned object
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }


    // Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpSession session) {
        return authService.logout(session);
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return authService.getAllUsers();
    }

    // Edit profile
    @PutMapping("/edit-profile/{id}")
    public ResponseEntity<String> editProfile(
            @PathVariable String id,
            @RequestBody UserRequestDTO updatedUser,
            HttpSession session) {
        return authService.editProfile(id, updatedUser, session);
    }

}
