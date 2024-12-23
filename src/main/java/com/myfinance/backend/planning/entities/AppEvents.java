package com.myfinance.backend.planning.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "events")
@Data
public class AppEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "El titulo no puede estar vacío")
    private String title;

    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "start_date")
    private LocalDate startDate;

    @FutureOrPresent(message = "La fecha de fin no puede ser en el pasado")
    @NotNull(message = "La fecha de finalización es obligatoria")
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotBlank(message = "El tipo de evento no puede estar vacío")
    @Pattern(regexp = "^(income|savings|expense)$", message = "El tipo de evento debe ser 'income', 'savings' o 'expense'")
    private String type;

}
