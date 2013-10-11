package com.spontecorp.ferreasesor.controller.reporte;

/**
 *
 * @author jgcastillo
 */
public class ReporteHelper {
    
    private Object rango;
    private Number dominio;
    private Object nombreObj;
    private Object[] propiedadObj;
    public static final int LLAMADAS_TOTALES = 1;
    public static final int LLAMADAS_DISPOSITIVO = 2;
    public static final int LLAMADAS_ASESOR = 3;
    public static final int LLAMADAS_TURNO = 4;
    public static final int PROMEDIO_PULSACIONES = 5;
    public static final int TIEMPOS_X_DISPOSITIVO = 6;
    public static final int TIEMPOS_X_FERREASESOR = 7;
    public static final int TIEMPOS_X_TURNO = 8;
    public static final int CALIDAD_X_DISPOSITIVO = 9;
    public static final int CALIDAD_X_FERREASESOR = 10;
    public static final int CALIDAD_X_TURNO = 11;
    public static final int CALIDAD_TOTAL = 12;

    public Object getRango() {
        return rango;
    }

    public void setRango(Object rango) {
        this.rango = rango;
    }

    public Number getDominio() {
        return dominio;
    }

    public void setDominio(Number dominio) {
        this.dominio = dominio;
    }

    public Object getNombreObj() {
        return nombreObj;
    }

    public void setNombreObj(Object nombreObj) {
        this.nombreObj = nombreObj;
    }

    public Object[] getPropiedadObj() {
        return propiedadObj;
    }

    public void setPropiedadObj(Object[] propiedadObj) {
        this.propiedadObj = propiedadObj;
    }

}
