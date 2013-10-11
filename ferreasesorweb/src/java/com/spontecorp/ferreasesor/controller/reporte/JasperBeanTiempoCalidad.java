/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

/**
 *
 * @author sponte07
 */
public class JasperBeanTiempoCalidad {

    private String propiedad;
    private double serie1;
    private double serie2;
    private double serie3;
    private double serie4;

    public JasperBeanTiempoCalidad() {
    }

    public JasperBeanTiempoCalidad(String propiedad, double serie1, double serie2, double serie3, double serie4) {
        this.propiedad = propiedad;
        this.serie1 = serie1;
        this.serie2 = serie2;
        this.serie3 = serie3;
        this.serie4 = serie4;
    }

    public void setSerie4(double serie4) {
        this.serie4 = serie4;
    }

    public double getSerie4() {
        return serie4;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public double getSerie1() {
        return serie1;
    }

    public void setSerie1(double serie1) {
        this.serie1 = serie1;
    }

    public double getSerie2() {
        return serie2;
    }

    public void setSerie2(double serie2) {
        this.serie2 = serie2;
    }

    public double getSerie3() {
        return serie3;
    }

    public void setSerie3(double serie3) {
        this.serie3 = serie3;
    }
}
