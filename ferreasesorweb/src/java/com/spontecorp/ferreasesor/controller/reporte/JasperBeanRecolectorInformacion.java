/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

/**
 *
 * @author sponte03
 */
public class JasperBeanRecolectorInformacion {
    private String respuesta;
    private int numero;

    public JasperBeanRecolectorInformacion() {
    }

    public JasperBeanRecolectorInformacion(String respuesta, int numero) {
        this.respuesta = respuesta;
        this.numero = numero;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
}
