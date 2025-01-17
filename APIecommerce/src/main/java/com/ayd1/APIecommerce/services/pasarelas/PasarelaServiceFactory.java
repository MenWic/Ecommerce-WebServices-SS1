/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ayd1.APIecommerce.services.pasarelas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author lamr4
 */

@Component
public class PasarelaServiceFactory {
    @Autowired
    private PasarelaAService pasarelaAService;

    @Autowired
    private PasarelaBService pasarelaBService;

    public PasarelaService getPasarelaService(String pasarela) throws Exception {
        if ("A".equalsIgnoreCase(pasarela)) {
            return pasarelaAService;
        } else if ("B".equalsIgnoreCase(pasarela)) {
            return pasarelaBService;
        }
        throw new Exception("Pasarela indicada no existente: " + pasarela);
    }
}
