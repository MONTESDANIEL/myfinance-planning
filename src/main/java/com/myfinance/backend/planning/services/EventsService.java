package com.myfinance.backend.planning.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myfinance.backend.planning.entities.ApiResponse;
import com.myfinance.backend.planning.entities.AppEvents;
import com.myfinance.backend.planning.respositories.EventsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventsService {

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

    public ResponseEntity<?> newEvent(AppEvents newEvent) {
        try {

            eventsRepository.save(newEvent);
            return createApiResponse(HttpStatus.CREATED, "El evento fue creado con exito", null);

        } catch (Exception e) {
            return createApiResponse(HttpStatus.BAD_REQUEST, "Ocurrio un error intentar crear el evento", null);
        }
    }

    public ResponseEntity<?> updateEvent(AppEvents updateEvent) {
        try {

            if (eventsRepository.findById(updateEvent.getId()).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El evento no fue encontrado.", null);
            }

            AppEvents event = eventsRepository.findById(updateEvent.getId()).get();

            event.setTitle(updateEvent.getTitle());
            event.setStartDate(updateEvent.getStartDate());
            event.setEndDate(updateEvent.getEndDate());
            event.setType(updateEvent.getType());

            eventsRepository.save(event);

            return createApiResponse(HttpStatus.OK, "El evento fue actualizado con exito.", null);
        } catch (Exception e) {
            return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ocurrio un error al intentar acualizar el evento",
                    null);
        }
    }

    public ResponseEntity<?> deleteEvent(Long id) {
        try {

            if (eventsRepository.findById(id).isEmpty()) {
                return createApiResponse(HttpStatus.CONFLICT, "El evento no fue encontrado.", null);
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

}
