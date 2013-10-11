package com.spontecorp.ferreasesor.controller;

/**
 *
 * @author jgcastillo
 */
public class BotonIntermedia {
    
    private String ubicacion;
    private int botonId;
    private int tiempoBueno;
    private int tiempoRegular;
    private int status;
    private String counter;

    public BotonIntermedia(String ubicacion, int botonId, int tBueno, int tRegular, int status, String counter) {
        this.ubicacion = ubicacion;
        this.botonId = botonId;
        this.tiempoBueno = tBueno;
        this.tiempoRegular = tRegular;
        this.status = status;
        this.counter = counter;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getBotonId() {
        return botonId;
    }

    public void setBotonId(int botonId) {
        this.botonId = botonId;
    }

    public int getTiempoBueno() {
        return tiempoBueno;
    }

    public void setTiempoBueno(int tiempoBueno) {
        this.tiempoBueno = tiempoBueno;
    }

    public int getTiempoRegular() {
        return tiempoRegular;
    }

    public void setTiempoRegular(int tiempoRegular) {
        this.tiempoRegular = tiempoRegular;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

}
