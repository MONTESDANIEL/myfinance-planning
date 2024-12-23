package com.myfinance.backend.planning.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinance.backend.planning.entities.AppGoals;
import com.myfinance.backend.planning.services.GoalsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalsController {
    @Autowired
    private final GoalsService goalsService;

    @GetMapping("/viewAllGoals")
    public ResponseEntity<?> viewAllGoals() {
        ResponseEntity<?> response = goalsService.viewAllGoals();
        return response;
    }

    @PostMapping("/newGoal")
    public ResponseEntity<?> newGoal(@Valid @RequestBody AppGoals newGoal) {
        ResponseEntity<?> response = goalsService.newGoal(newGoal);
        return response;
    }

    @PutMapping("/updateGoal")
    public ResponseEntity<?> updateGoal(@Valid @RequestBody AppGoals updateGoal) {
        ResponseEntity<?> response = goalsService.updateGoal(updateGoal);
        return response;
    }

    @DeleteMapping("/deleteGoal/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        ResponseEntity<?> response = goalsService.deleteGoal(id);
        return response;
    }

}
