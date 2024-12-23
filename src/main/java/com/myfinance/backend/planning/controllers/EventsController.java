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

    @PostMapping("/newEvent")
    public ResponseEntity<?> newEvent(@Valid @RequestBody AppEvents newEvent) {
        ResponseEntity<?> response = eventsService.newEvent(newEvent);
        return response;
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<?> updateEvent(@Valid @RequestBody AppEvents updateEvent) {
        ResponseEntity<?> response = eventsService.updateEvent(updateEvent);
        return response;
    }

    @DeleteMapping("/deleteEvent/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        ResponseEntity<?> response = eventsService.deleteEvent(id);
        return response;
    }
}
