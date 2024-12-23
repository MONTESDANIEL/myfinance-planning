package com.myfinance.backend.planning.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myfinance.backend.planning.entities.ApiResponse;
import com.myfinance.backend.planning.entities.AppGoals;
import com.myfinance.backend.planning.respositories.GoalsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalsService {

    private final GoalsRepository goalsRepository;

    public ResponseEntity<?> viewAllGoals() {
        try {
            List<AppGoals> appGoals = StreamSupport.stream(goalsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());

            return createApiResponse(HttpStatus.OK, "Las metas fueron consultadas con exito", appGoals);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al consultar las metas", null);
        }
    }

    public ResponseEntity<?> newGoal(AppGoals newGoal) {
        try {

            if ((newGoal.getCurrentSpending() + newGoal.getRemainingBudget()) < newGoal.getTargetAmount()) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Las cantidades no son correctas", null);
            }

            goalsRepository.save(newGoal);
            return createApiResponse(HttpStatus.CREATED, "La meta fue creada con exito", null);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, "Ocurrio un error intentar crear la meta", null);
        }
    }

    public ResponseEntity<?> updateGoal(AppGoals updateGoal) {
        try {

            if (goalsRepository.findById(updateGoal.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La meta no fue encontrada.", null);
            }

            if ((updateGoal.getCurrentSpending() + updateGoal.getRemainingBudget()) < updateGoal.getTargetAmount()) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Las cantidades no son correctas", null);
            }

            AppGoals goal = goalsRepository.findById(updateGoal.getId()).get();

            goal.setTitle(updateGoal.getTitle());
            goal.setTargetAmount(updateGoal.getTargetAmount());
            goal.setCurrentSpending(updateGoal.getCurrentSpending());
            goal.setRemainingBudget(updateGoal.getRemainingBudget());
            goal.setType(updateGoal.getType());

            goalsRepository.save(goal);

            return createApiResponse(HttpStatus.OK, "La meta fue actualizada con exito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al intentar acualizar la meta",
                    null);
        }
    }

    public ResponseEntity<?> deleteGoal(Long id) {
        try {

            if (goalsRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La meta no fue encontrada.", null);
            }

            goalsRepository.deleteById(id);

            return createApiResponse(HttpStatus.NO_CONTENT, "La meta fue eliminada con exito", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al eliminar la meta", null);
        }
    }

    // Metodo para generar el formato de respuesta adecuado
    private ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
        ApiResponse<Object> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

}
