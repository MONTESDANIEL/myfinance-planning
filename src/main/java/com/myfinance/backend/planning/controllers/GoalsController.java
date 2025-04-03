package com.myfinance.backend.planning.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinance.backend.planning.entities.AppGoals;
import com.myfinance.backend.planning.services.GoalsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalsController {

    private final GoalsService goalsService;

    @GetMapping("/viewAllGoals")
    public ResponseEntity<?> viewAllGoals() {
        ResponseEntity<?> response = goalsService.viewAllGoals();
        return response;
    }

    @GetMapping("/viewUserGoals")
    public ResponseEntity<?> viewUserGoals(@RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = goalsService.viewUserGoals(authorizationToken);
        return response;
    }

    @PostMapping("/newGoal")
    public ResponseEntity<?> newGoal(@Valid @RequestBody AppGoals newGoal,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = goalsService.newGoal(newGoal, authorizationToken);
        return response;
    }

    @PutMapping("/updateGoal")
    public ResponseEntity<?> updateGoal(@Valid @RequestBody AppGoals updateGoal,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = goalsService.updateGoal(updateGoal, authorizationToken);
        return response;
    }

    @DeleteMapping("/deleteGoal/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id,
            @RequestHeader("Authorization") String authorizationToken) {
        ResponseEntity<?> response = goalsService.deleteGoal(id, authorizationToken);
        return response;
    }

    @GetMapping("/findById/{id}")
    public AppGoals findById(@PathVariable Long id) {
        AppGoals response = goalsService.findById(id);
        return response;
    }

}
