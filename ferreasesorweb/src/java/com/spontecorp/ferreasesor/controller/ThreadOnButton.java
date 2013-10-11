package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.primefaces.push.PushContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sponte03
 */

public class ThreadOnButton implements Runnable {

    private Date sec = new Date();
    private SimpleDateFormat secFormat = new SimpleDateFormat("mm:ss");
    private long counter = 0;
    private int tiempoBueno;
    private int tiempoRegular;
    private int tiempoCierre;
    private Boton boton;   
    private volatile PushContext pushContext;
    private String showCounter;
    private volatile boolean terminar;
    private final String nombreHilo;
    private final static Logger logger = LoggerFactory.getLogger(ThreadOnButton.class);
    private String CHANNEL;
    private AlarmaController alarmaController;
    
    ThreadOnButton(AlarmaController alarmaController, String nombreHilo, PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular, int tCierre) {
        this.alarmaController = alarmaController;
        this.nombreHilo = nombreHilo;
        this.pushContext = pushContext;
        this.boton = boton;
        this.tiempoBueno = tiempoBueno;
        this.tiempoRegular = tiempoRegular;
        this.tiempoCierre = tCierre;
    }
    
    public void setTerminar(){
        this.terminar = true;
    }
    
    public void setArrancar(){
        this.terminar = false;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    @Override
    public void run() {
        setCHANNEL("/channel" + boton.getId());
        while (!terminar) {
            try {
                counter++;
                sec.setTime(counter * 1000);
                this.showCounter = secFormat.format(sec);
                if (counter > 0 && counter <= tiempoBueno) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 1, this.showCounter));
                    //System.out.println(counter);
                } else if (counter > tiempoBueno && counter <= tiempoRegular) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 2, this.showCounter));
                    //System.out.println(counter);
                } else if (counter > tiempoRegular && counter < tiempoCierre) {
                    pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 3, this.showCounter));
                    //System.out.println(counter);
                } else if (counter >= tiempoCierre){
                    terminar = true;
                    //System.out.println(counter);
                    //pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 4, this.showCounter));
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                logger.error("Error de interrupción: " + e.getMessage());
            }
        }
        if (terminar) {
            //Se invoca al método que detiene el hilo 
            alarmaController.stopThread(boton);
            
            //Se invoca al método que actualiza el status y fecha de cierre de la Llamada
            alarmaController.detenerLlamada(boton);
            
            //Se maneja la vista que muestra el Status de la Llamada
            pushContext.push(getCHANNEL(), new BotonIntermedia(boton.getUbicacion(), boton.getId(), this.tiempoBueno, this.tiempoRegular, 4, this.showCounter));
        }
        
    }
        
}
