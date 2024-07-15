package com.example.db_calendar.service.user;

import com.example.db_calendar.config.JwtAuthenticationFilter;
import com.example.db_calendar.entity.Event;
import com.example.db_calendar.repository.EventRepository;
import com.example.db_calendar.repository.UserRepository;
import com.example.db_calendar.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    public UserResponse getUser() {
        String userEmail= JwtAuthenticationFilter.CURRENT_USER;
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            return UserResponse
                    .builder()
                    .message("get user successful")
                    .user(userRepository.save(optionalUser.get()))
                    .status("OK")
                    .build();
        }
        return UserResponse
                .builder()
                .message("get user failed")
                .status("KO")
                .build();
    }

    public List<User> getUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public UserResponse updateUser(User updatedUser) {
        // Fetch the existing user
        Optional<User> optionalUser = userRepository.findById(updatedUser.getId());
        // Update events if necessary
        if (optionalUser.isPresent()) {
            User existingUser =optionalUser.get();
            for (Event event : new ArrayList<>(existingUser.getAssignedEvents())) {
                // Perform any updates to the event if needed
                event.getUsers().remove(existingUser);
                event.getUsers().add(updatedUser);
                eventRepository.save(event);
            }
            // Save the updated user
            userRepository.delete(existingUser);
            return UserResponse
                    .builder()
                    .message("saved successful")
                    .user(userRepository.save(userRepository.save(updatedUser)))
                    .status("OK")
                    .build();
        }
        return UserResponse
                .builder()
                .message("updated failed")
                .status("KO")
                .build();

    }

    public UserResponse save(User user) {
        try {
            return UserResponse
                    .builder()
                    .message("saved successful")
                    .user(userRepository.save(user))
                    .status("OK")
                    .build();
        }catch (Exception e) {
            return UserResponse
                    .builder()
                    .message("save failed")
                    .status("KO")
                    .build();
        }
    }

    public UserResponse deleteUser(Integer userId) {
        Optional<User> optionalUser= userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user= optionalUser.get();
            for (Event event : new ArrayList<>(user.getAssignedEvents())) {
                event.getUsers().remove(user);
                eventRepository.save(event);
            }
            userRepository.delete(user);
            return UserResponse
                    .builder()
                    .message("dropped successfully")
                    .status("OK")
                    .build();
        }else {
            return UserResponse
                    .builder()
                    .message("dropped failed")
                    .status("KO")
                    .build();
        }
    }

    public UserResponse assignEventToUsers(String[] emails, Integer eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            for (String email : emails) {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    boolean eventAlreadyAssigned = user.getAssignedEvents().stream()
                            .anyMatch(assignedEvent -> assignedEvent.getId().equals(eventId));
                    if (!eventAlreadyAssigned) {
                        user.getAssignedEvents().add(event);
                        event.getUsers().add(user);
                        userRepository.save(user);
                        eventRepository.save(event);
                    }
                }
            }
            return UserResponse
                    .builder()
                    .message("Event assigned successfully")
                    .status("OK")
                    .build();
        }
        return UserResponse
                .builder()
                .message("Event assigned failed")
                .status("KO")
                .build();
    }

    public List<Event> getUserEventsByEmail(String email) {
        System.out.println("email: "+email);
        Optional<User>optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            return user.getAssignedEvents();
        }
        return new ArrayList<>();
    }
}
