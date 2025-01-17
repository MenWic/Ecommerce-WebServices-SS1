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
public class PasarelaAService implements PasarelaService {

    private String urlPasarelaA = "http://3.15.162.171:3000/api/v1"; //Indicar URL de la Pasarela de Manuel

    @Override
    public String geToken(String code, String secretKey) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String url = urlPasarelaA + "/auth/company";

        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("secretKey", secretKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("token")) {
                return responseBody.get("token").toString();
            } else {
                throw new Exception("Company not found en Pasarela Chiltepago");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("No se pudo obtener el token de autenticación de la pasarela Chiltepago:" + e.getMessage());
            //throw new Exception("Error al autenticar en Pasarela A: " + e.getMessage());
        }
    }

    @Override
    public boolean emailExiste(String token, String email) throws Exception {
        String url = urlPasarelaA + "/user-email/" + email;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Asi estaba anteriorrmente
        //HttpEntity<Map<String, String>> request = new HttpEntity<>(null, headers);
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("exists")) {
                return Boolean.TRUE.equals(responseBody.get("exists"));
            } else {
                throw new Exception("Error en la respuesta de verificación de email en Pasarela Chiltepago");
            }
        } catch (HttpClientErrorException e) {
            throw new Exception("Usuario no encontrado en Pasarela Chiltepago: " + e.getMessage());
        }
    }

    @Override
    public String realizarPago(String token, String userEmail, String amount, String payment_method) throws Exception {
        String url = urlPasarelaA + "/pay";

        Map<String, Object> body = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();

        body.put("userEmail", userEmail);
        body.put("amount", amount);
        body.put("payment_method", payment_method);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && "Payment processed successfully".equals(responseBody.get("message"))) {
                return "Pago realizado con éxito en Pasarela Chiltepago";
            } else if (responseBody != null && "User not found".equals(responseBody.get("message"))) {
                throw new Exception("Usuario no encontrado en Pasarela CHiltepagos para el pago.");
            } else {
                throw new Exception("Error al realizar el pago en Pasarela Chiltepago: " + responseBody.get("message"));
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401) {
                throw new Exception("Token de autenticación no válido en Pasarela Chiltepago");
            } else {
                throw new Exception("Error al realizar el pago en Pasarela Chiltepago: " + e.getMessage());
            }
        }
    }
}
