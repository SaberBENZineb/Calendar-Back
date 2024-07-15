package com.example.db_calendar.controller;

import com.example.db_calendar.entity.Event;
import com.example.db_calendar.service.event.EventResponse;
import com.example.db_calendar.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping("/save")
    public ResponseEntity<Event> saveEvent(@RequestBody Event event){
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/test")
    public void test() throws Exception {
        System.out.println("events");
        eventService.Test();
    }

    @GetMapping("/get/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Integer eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @PutMapping("/update")
    public ResponseEntity<EventResponse> updateEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.updateEvent(event));
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer eventId)
    {
        System.out.println("delete "+eventId);
        eventService.deleteEvent(eventId);
        String jsonResponse = "{\"message\": \"Deleted\"}";
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/")
    public ResponseEntity<List<Event>> getUserEvents() {
        return ResponseEntity.ok(eventService.getUserEvents());
    }
}
