/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

/**
 *
 * @author sponte03
 */
public class JasperBeanLlamadas {
    
    private String nombre;
    private double cantidad;

    public JasperBeanLlamadas() {
    }

    public JasperBeanLlamadas(String nombre, double cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }
    
    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
