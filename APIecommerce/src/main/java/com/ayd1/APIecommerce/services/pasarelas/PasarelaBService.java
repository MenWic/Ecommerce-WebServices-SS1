/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ayd1.APIecommerce.services.pasarelas;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author lamr4
 */
@Service
public class PasarelaBService implements PasarelaService {

    private final String urlPasarelaB = "https://8f22-2800-98-1a0e-634d-a6d3-ffe0-9249-b67f.ngrok-free.app/api";

    @Override
    public String geToken(String code, String secretKey) throws Exception {
        //throw new UnsupportedOperationException("La Pasarela AlexisPagos no requiere autenticación mediante token.");
    return "";
    }

    @Override
    public boolean emailExiste(String token, String email) throws Exception {
        String formattedEmail = email.split("@")[0] + "@Tienda Nuevo.com";
        String url = urlPasarelaB + "/cuenta/ObtenerCuentasEspecificasCorreo/" + formattedEmail;
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<?> request = new HttpEntity<>(new HttpHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "cuenta encontrada".equals(responseBody.get("message"))) {
                return true;
            } else {
                throw new Exception("El correo no está asociado a ninguna cuenta en Pasarela AlexisPagos");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Error al verificar el correo en Pasarela AlexisPagos: " + e.getMessage());
        }
    }

    @Override
    public String realizarPago(String token, String userEmail, String amount, String payment_method) throws Exception {
        String formattedEmail = userEmail.split("@")[0] + "@Tienda Nuevo.com";
        String url = urlPasarelaB + "/transaccion/generarTransaccion";

        Map<String, Object> body = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        body.put("correo", formattedEmail);
        body.put("monto", amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "transaccion generada".equals(responseBody.get("error"))) {
                return "Pago realizado con éxito en Pasarela AlexisPagos";
            } else if (responseBody != null && "Error al crear transaccion, no se genero el pago".equals(responseBody.get("error"))) {
                throw new Exception("No se pudo generar el pago en Pasarela AlexisPagos: " + responseBody.get("error"));
            } else {
                throw new Exception("Error desconocido al realizar el pago en Pasarela AlexisPagos");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Error al realizar el pago en Pasarela AlexisPagos: " + e.getMessage());
        }
    }
}
