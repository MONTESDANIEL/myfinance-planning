package com.myfinance.backend.planning.entities.Movements;

import lombok.Data;

@Data
public class AppPdf {
    private int year;
    private int startMonth;
    private int endMonth;
    private String type;
    private String token;
}
