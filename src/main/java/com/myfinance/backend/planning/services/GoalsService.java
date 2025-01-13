package com.myfinance.backend.planning.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myfinance.backend.planning.entities.ApiResponse;
import com.myfinance.backend.planning.entities.AppGoals;
import com.myfinance.backend.planning.entities.AppUser;
import com.myfinance.backend.planning.respositories.GoalsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalsService {

    private final RestTemplate restTemplate;
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

    public ResponseEntity<?> viewUserGoals(String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            List<AppGoals> userAppGoals = goalsRepository.findByUserId(userId);

            return createApiResponse(HttpStatus.OK, "Las metas del usuario fueron consultadas con exito", userAppGoals);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrio un error al consultar las metas del usuario", null);
        }
    }

    public ResponseEntity<?> newGoal(AppGoals newGoal, String token) {
        try {

            if ((newGoal.getCurrentSpending() + newGoal.getRemainingBudget()) < newGoal.getTargetAmount()) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Las cantidades no son correctas", null);
            }

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            newGoal.setUserId(userId);

            goalsRepository.save(newGoal);
            return createApiResponse(HttpStatus.CREATED, "La meta fue creada con exito", null);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, "Ocurrio un error intentar crear la meta", null);
        }
    }

    public ResponseEntity<?> updateGoal(AppGoals updateGoal, String token) {
        try {

            if (goalsRepository.findById(updateGoal.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La meta no fue encontrada.", null);
            }

            if ((updateGoal.getCurrentSpending() + updateGoal.getRemainingBudget()) < updateGoal.getTargetAmount()) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "Las cantidades no son correctas", null);
            }

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            if (updateGoal.getUserId() != userId) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "No se pudo actualizar la meta.", null);
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

    public ResponseEntity<?> deleteGoal(Long id, String token) {
        try {

            if (goalsRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "La meta no fue encontrada.", null);
            }

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            if (!Long.valueOf(userId).equals(goalsRepository.findById(id).get().getUserId())) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "Error al eliminar la meta.", null);
            }

            goalsRepository.deleteById(id);

            return createApiResponse(HttpStatus.NO_CONTENT, "La meta fue eliminada con exito", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al eliminar la meta", null);
        }
    }

    public AppGoals findById(Long id) {
        try {

            if (goalsRepository.findById(id).isEmpty()) {
                return null;
            }

            return goalsRepository.findById(id).get();

        } catch (Exception e) {
            return null;
        }
    }

    // Metodo para generar el formato de respuesta adecuado
    private ResponseEntity<?> createApiResponse(HttpStatus status, String message, Object data) {
        ApiResponse<Object> response = new ApiResponse<>(message, data);
        return ResponseEntity.status(status).body(response);
    }

    // Metodo para solicitar el id del usuario correspondiente segun el id
    private Long getUserId(String token) {
        try {

            String userServiceUrl = "http://192.168.1.2:8081/api/users/view";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<ApiResponse<AppUser>> response = restTemplate.exchange(
                    userServiceUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<AppUser>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ApiResponse<AppUser> apiResponse = response.getBody();

                if (apiResponse != null && apiResponse.getData() != null) {
                    AppUser user = apiResponse.getData();
                    return user.getId();
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

}
