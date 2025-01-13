package com.myfinance.backend.planning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinance.backend.planning.entities.AppEvents;
import com.myfinance.backend.planning.services.EventsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventsController {

    @Autowired
    private final EventsService eventsService;

    @GetMapping("/viewAllEvents")
    public ResponseEntity<?> viewAllEvents() {
        ResponseEntity<?> response = eventsService.viewAllEvents();
        return response;
    }

    @GetMapping("/viewUserEvents")
    public ResponseEntity<?> viewUserEvents(@RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = eventsService.viewUserEvents(authorizationToken);
        return response;
    }

    @PostMapping("/newEvent")
    public ResponseEntity<?> newEvent(@Valid @RequestBody AppEvents newEvent,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = eventsService.newEvent(newEvent, authorizationToken);
        return response;
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody AppEvents updateEvent,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = eventsService.updateEvent(updateEvent, authorizationToken);
        return response;
    }

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = eventsService.deleteEvent(id, authorizationToken);
        return response;
    }
}
