package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;

/**
 *
 * @author Casper
 */
public class LlamadaAux implements Serializable{
    
    private Asesor asesor;
    private Boton boton;
    private Turno turno;
    private Llamada llamada;

    public LlamadaAux(Llamada llamada, Asesor asesor, Boton boton, Turno turno) {
        this.llamada = llamada;
        this.asesor = asesor;
        this.boton = boton;
        this.turno = turno;
    }

    public Llamada getLlamada() {
        return llamada;
    }

    public void setLlamada(Llamada llamada) {
        this.llamada = llamada;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public Boton getBoton() {
        return boton;
    }

    public void setBoton(Boton boton) {
        this.boton = boton;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }
    
}
