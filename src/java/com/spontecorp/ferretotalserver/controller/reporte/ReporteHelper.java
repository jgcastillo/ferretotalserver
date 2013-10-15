package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.entity.Tienda;

/**
 *
 * @author jgcastillo
 */
public class ReporteHelper {
    
    private Tienda tienda;
    private Object rango;
    private Number dominio;
    private Object nombreObj;
    private Object[] propiedadObj;
    
    public static final int LLAMADAS_TOTALES_TIENDA = 1;
    public static final int LLAMADAS_DISPOSITIVO_TIENDA = 2;
    public static final int LLAMADAS_ASESOR_TIENDA = 3;
    public static final int CALIDAD_TOTAL_TIENDA = 4;
    public static final int TIEMPO_TOTAL_TIENDA = 5;

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

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

}
