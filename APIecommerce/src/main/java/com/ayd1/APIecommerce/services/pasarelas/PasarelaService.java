/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ayd1.APIecommerce.services.pasarelas;

//import 

/**
 *
 * @author lamr4
 */
public interface PasarelaService {
  
    //Obtener token para la tienda
    public String geToken(String code, String secretKey) throws Exception;
    //verificar si hay cuenta con el email que va en la URL
    public boolean emailExiste(String token, String email) throws Exception;
    //Realizar pago
    public String realizarPago(String token, String userEmail, String amount, String payment_method) throws Exception;
    
}


