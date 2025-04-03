package com.myfinance.backend.planning.entities.Movements;

import lombok.Data;

@Data
public class AppGoal {

    private Long id;
    private Long userId;
    private String title;
    private Long targetAmount;
    private Long currentSpending;
    private Long remainingBudget;
    private String type;

}
