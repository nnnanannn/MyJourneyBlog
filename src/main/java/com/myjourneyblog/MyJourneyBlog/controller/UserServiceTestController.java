package com.myjourneyblog.MyJourneyBlog.controller;

import com.myjourneyblog.MyJourneyBlog.dto.UserRegistrationDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserResponseDTO;
import com.myjourneyblog.MyJourneyBlog.dto.UserUpdateDTO;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import com.myjourneyblog.MyJourneyBlog.service.UserService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Test controller for UserService operations
 */

@RestController
@RequestMapping("/api/test/users")
@RequiredArgsConstructor
public class UserServiceTestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO user = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserByID(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        UserResponseDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO updateDTO) {
        UserResponseDTO user = userService.updateUser(id, updateDTO);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> usernameExists(@PathVariable String username) {
        boolean exists = userService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = userService.emailExists(email);
        return ResponseEntity.ok(exists);
    }

    @Autowired
    UserRepository userRepository;
        // ======== DELETE all users ========
    @GetMapping("/delete-all-users")
    public String deleteAllUsers() {
        long count = userRepository.count();
        userRepository.deleteAll();
        return "Deleted " + count + " users";
    }

        //============== RESET DATABASE For Testing:  ==============
    //==== ID Auto-Increment reset FOR TESTING ====
    //==== DELETE ALL DATA ===
    @Autowired
    private EntityManager entityManager;
    @GetMapping("/reset-ids")
    @Transactional
    public String resetAutoIncrement() {
        userRepository.deleteAll();
        userRepository.flush();
        // Specific for PostgreSQL
        entityManager.createNativeQuery("ALTER SEQUENCE users_id_seq RESTART WITH 1").executeUpdate();

        return "Auto-increment reset for PostgreSQL";
    }

}
