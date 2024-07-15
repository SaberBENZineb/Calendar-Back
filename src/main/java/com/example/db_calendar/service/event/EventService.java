package com.example.db_calendar.service.event;

import com.example.db_calendar.config.JwtAuthenticationFilter;
import com.example.db_calendar.entity.User;
import com.example.db_calendar.repository.EventRepository;
import com.example.db_calendar.entity.Event;
import com.example.db_calendar.repository.UserRepository;
import com.example.db_calendar.service.user.UserService;
import com.example.db_calendar.service.graph.MicrosoftGraph;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public Event createEvent(Event event) {
        Event event1=eventRepository.save(event);
        String userEmail= JwtAuthenticationFilter.CURRENT_USER;
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User not found"));
        userService.assignEventToUsers(new String[]{user.getEmail()},event1.getId());
        return event1;
    }

    public EventResponse getEvent(Integer eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return EventResponse
                    .builder()
                    .message("get event successful")
                    .status("OK")
                    .event(optionalEvent.get())
                    .build();
        }
        return EventResponse
                .builder()
                .message("get event failed")
                .status("KO")
                .build();
    }

    public List<Event> getEvents() {
        return new ArrayList<>(eventRepository.findAll());
    }

    public void Test() throws Exception {
        MicrosoftGraph.outlookEvents();
    }

    public EventResponse updateEvent(Event updatedEvent) {
        try {
            // Fetch the existing user
            Optional<Event> OptionalExistingEvent = eventRepository.findById(updatedEvent.getId());
            // Update events if necessary
            if (OptionalExistingEvent.isPresent()) {
                Event existingEvent=OptionalExistingEvent.get();

                for (User user : new ArrayList<>(existingEvent.getUsers())) {
                    // Perform any updates to the event if needed
                    user.getAssignedEvents().remove(existingEvent);
                    user.getAssignedEvents().add(updatedEvent);
                    userRepository.save(user);
                }
                // Save the updated user
                eventRepository.delete(existingEvent);
                return EventResponse
                        .builder()
                        .message("get user successful")
                        .status("OK")
                        .event(eventRepository.save(updatedEvent))
                        .build();
            }
            return EventResponse
                    .builder()
                    .message("update event failed")
                    .status("KO")
                    .build();
        }catch (Exception e) {
        return EventResponse
                .builder()
                .message("update event failed")
                .status("KO")
                .build();
        }
    }

    public void deleteEventForAll(Integer eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        // Update events if necessary
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            for (User user : new ArrayList<>(event.getUsers())) {
                // Perform any updates to the event if needed
                user.getAssignedEvents().remove(event);
                userRepository.save(user);
            }
            // Save the updated user
            eventRepository.delete(event);
        }
    }

    public void deleteEvent(Integer eventId) {
        String userEmail= JwtAuthenticationFilter.CURRENT_USER;
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        // Update events if necessary
        if (optionalEvent.isPresent() && optionalUser.isPresent()) {
            Event event = optionalEvent.get();
            User user = optionalUser.get();
            user.getAssignedEvents().remove(event);
            event.getUsers().remove(user);
            userRepository.save(user);
            if (event.getUsers().isEmpty()) {
                eventRepository.delete(event);
            }else{
                eventRepository.save(event);
            }
            userRepository.save(user);
        }
    }

    public List<User> getEventUsers(Integer eventId) {
        Optional<Event> event=eventRepository.findById(eventId);
        if (event.isPresent()) {
            return event.get().getUsers();
        }
        return new ArrayList<>();
    }

    public List<Event> getUserEvents() {
        String userEmail= JwtAuthenticationFilter.CURRENT_USER;
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            return user.get().getAssignedEvents();
        }
        return new ArrayList<>();
    }
}
