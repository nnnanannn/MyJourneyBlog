package com.myjourneyblog.MyJourneyBlog.service;

import com.myjourneyblog.MyJourneyBlog.exception.ResourceNotFoundException;
import com.myjourneyblog.MyJourneyBlog.exception.ValidationException;
import com.myjourneyblog.MyJourneyBlog.model.User;
import com.myjourneyblog.MyJourneyBlog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
//@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public User findByID(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " +id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Username not found: "+ username));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public User createUser(User user) {
        boolean usernameExists = userRepository.existsByUsername(user.getUsername());
        boolean emailExists = userRepository.existsByEmail(user.getEmail());

        // Both username and email exist
        if(usernameExists && emailExists) {
                throw new ValidationException("Username: " + user.getUsername() + ", and Email: " + user.getEmail() +" already exist");
            }
        // Only username exists
        if (usernameExists){
                throw new ValidationException("Username already exists: " + user.getUsername());
            }
        // Only email exists
        if (emailExists) {
            throw new ValidationException("Email already exists: " + user.getEmail());
        }
        // Both are unique, save the user
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = findByID(id);

        if (userDetails.getFullname() != null) {
            user.setFullname(userDetails.getFullname());
        }
        if (userDetails.getBio() != null) {
            user.setBio(userDetails.getBio());
        }

        // updatedAt will be set automatically by @Prepersist on User Entity
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = findByID(id);
        if (user == null) {
            System.out.println("User not found");
        }
        userRepository.delete(user);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        User user = findByUsername(username);
        if (user == null) {
            System.out.println("User not found");
        }
        userRepository.delete(user);
    }

    @Transactional
    public User createUserThatFails(User user) {
        userRepository.save(user);
        throw new RuntimeException("Simulated error");
    }

}
