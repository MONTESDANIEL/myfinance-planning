package com.myfinance.backend.planning.entities.Movements;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAppMovements {
    private Long id;
    private Long userId;
    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private String movementType;
    private AppGoal goal;
    private AppTag tag;
}
