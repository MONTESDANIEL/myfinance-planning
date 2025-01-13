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

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 0, message = "El monto debe ser un valor positivo")
    private Long amount;

    @NotBlank(message = "El titulo no puede estar vacío")
    private String title;

    @FutureOrPresent(message = "La fecha no puede ser en el pasado")
    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "date")
    private LocalDate date;

    @NotBlank(message = "El tipo de evento no puede estar vacío")
    @Pattern(regexp = "^(income|savings|expense)$", message = "El tipo de evento debe ser 'income', 'savings' o 'expense'")
    private String type;

}
