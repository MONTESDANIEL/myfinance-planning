package com.myfinance.backend.planning.services;

import java.util.List;
import java.util.Objects;
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
import com.myfinance.backend.planning.entities.AppEvents;
import com.myfinance.backend.planning.entities.AppUser;
import com.myfinance.backend.planning.respositories.EventsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventsService {

    private final RestTemplate restTemplate;
    private final EventsRepository eventsRepository;

    public ResponseEntity<?> viewAllEvents() {
        try {
            List<AppEvents> appEvents = StreamSupport.stream(eventsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());

            return createApiResponse(HttpStatus.OK, "Los eventos fueron consultados con exito", appEvents);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al consultar los eventos",
                    null);
        }
    }

    public ResponseEntity<?> viewUserEvents(String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            List<AppEvents> userAppEvents = eventsRepository.findByUserId(userId);

            return createApiResponse(HttpStatus.OK, "Los eventos del usuario fueron consultados con exito",
                    userAppEvents);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrio un error al consultar los eventos del usuario",
                    null);
        }
    }

    public ResponseEntity<?> newEvent(AppEvents newEvent, String token) {
        try {

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            newEvent.setUserId(userId);

            eventsRepository.save(newEvent);
            return createApiResponse(HttpStatus.CREATED, "El evento fue creado con exito", null);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, "Ocurrio un error intentar crear el evento", null);
        }
    }

    public ResponseEntity<?> updateEvent(AppEvents updateEvent, String token) {
        try {

            if (eventsRepository.findById(updateEvent.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El evento no fue encontrado.", null);
            }

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            if (!Objects.equals(updateEvent.getUserId(), userId)) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "No se pudo actualizar el evento.", null);
            }

            AppEvents event = eventsRepository.findById(updateEvent.getId()).get();

            event.setTitle(updateEvent.getTitle());
            event.setDate(updateEvent.getDate());
            event.setType(updateEvent.getType());

            eventsRepository.save(event);

            return createApiResponse(HttpStatus.OK, "El evento fue actualizado con exito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrio un error al intentar acualizar el evento",
                    null);
        }
    }

    public ResponseEntity<?> deleteEvent(Long id, String token) {
        try {

            if (eventsRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El evento no fue encontrado.", null);
            }

            Long userId = getUserId(token);

            // Verificación temprana de la existencia del usuario
            if (userId == null) {
                return createApiResponse(HttpStatus.BAD_REQUEST, "No se pudo cargar el usuario.", null);
            }

            if (!Long.valueOf(userId).equals(eventsRepository.findById(id).get().getUserId())) {
                return createApiResponse(HttpStatus.UNAUTHORIZED, "Error al eliminar el evento.", null);
            }

            eventsRepository.deleteById(id);

            return createApiResponse(HttpStatus.NO_CONTENT, "El evento fue eliminado con exito", null);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error al eliminar el evento", null);
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
