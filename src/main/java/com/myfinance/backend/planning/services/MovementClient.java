package com.myfinance.backend.planning.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myfinance.backend.planning.entities.ApiResponse;
import com.myfinance.backend.planning.entities.Movements.ResponseAppMovements;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementClient {

    private final RestTemplate restTemplate;

    @Value("${movements.service.url}")
    private String movementsServiceUrl;

    // Método para obtener los movimientos usando un token de autorización
    public List<ResponseAppMovements> getMovements(String token) {

        // Configurar encabezados para la solicitud con el token Bearer
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Crear la entidad HTTP con los encabezados, sin cuerpo (void)
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ApiResponse<List<ResponseAppMovements>>> response = restTemplate.exchange(
                    movementsServiceUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<ApiResponse<List<ResponseAppMovements>>>() {
                    });

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ApiResponse<List<ResponseAppMovements>> apiResponse = response.getBody();

                if (apiResponse != null && apiResponse.getData() != null) {
                    List<ResponseAppMovements> movements = apiResponse.getData();
                    return movements;
                }
            }
            return null;
        } catch (Exception e) {
            // En caso de excepción, imprimir el error
            System.err.println("Error al realizar la solicitud: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}