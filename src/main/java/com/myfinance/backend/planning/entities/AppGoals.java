package com.myfinance.backend.planning.entities;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "goals")
@Data
public class AppGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "El título no puede estar vacío")
    private String title;

    @NotNull(message = "El monto objetivo es obligatorio")
    @Min(value = 0, message = "El monto objetivo debe ser mayor o igual a 0")
    @Column(name = "target_amount")
    private Long targetAmount;

    @NotBlank(message = "El tipo de meta es obligatorio")
    @Pattern(regexp = "^(incremental|reductive)$", message = "El tipo de meta debe ser 'incremental' o 'reductive'")
    private String type;
}
