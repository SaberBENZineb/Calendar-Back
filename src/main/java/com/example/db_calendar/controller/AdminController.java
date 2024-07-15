package com.example.db_calendar.controller;

import com.example.db_calendar.entity.Event;
import com.example.db_calendar.entity.User;
import com.example.db_calendar.service.event.EventService;
import com.example.db_calendar.service.user.UserResponse;
import com.example.db_calendar.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;
    private final EventService eventService;

    public AdminController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping("/event/{eventId}")
    public ResponseEntity<UserResponse> assignEventToUsers(
            @RequestBody String[] emails,
            @PathVariable Integer eventId
    ){
        System.out.println("requestBody"+ Arrays.toString(emails));
        return ResponseEntity.ok(userService.assignEventToUsers(emails, eventId));
    }

    @GetMapping("/{email}/events")
    public ResponseEntity<List<Event>> getUserEvents(
            @PathVariable String email
    ){
        return ResponseEntity.ok(userService.getUserEventsByEmail(email));
    }

    @GetMapping("/{eventId}/users")
    public ResponseEntity<List<User>> getEventUsers(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.getEventUsers(eventId));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventService.getEvents());
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer eventId)
    {
        System.out.println("delete "+eventId);
        eventService.deleteEventForAll(eventId);
        String jsonResponse = "{\"message\": \"Deleted\"}";
        return ResponseEntity.ok(jsonResponse);
    }
}
