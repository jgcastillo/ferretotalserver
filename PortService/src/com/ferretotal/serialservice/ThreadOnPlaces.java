package com.ferretotal.serialservice;

import com.ferretotal.serialservice.entity.Boton;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class ThreadOnPlaces extends SwingWorker<Void, Void>{
    
    private long counter = 0;
    private SimpleDateFormat secFormat = new SimpleDateFormat("mm:ss");
    private Date sec = new Date();
    private int tiempoBueno;
    private int tiempoRegular;
    private int cerrarLlamada;
    private Boton boton;
    private SerialService serialService;
    private Map<String, Runnable> llamados;
    private Logger logger = LoggerFactory.getLogger(ThreadOnPlaces.class);
    
    public ThreadOnPlaces(SerialService serialService, int tiempoBueno, int tiempoRegular, 
                int cerrarLlamada, Boton boton){
        this.serialService = serialService;
        this.tiempoBueno = tiempoBueno;
        this.tiempoRegular = tiempoRegular;
        this.cerrarLlamada = cerrarLlamada;
        this.boton = boton;
    }
 
    @Override
    protected Void doInBackground() throws Exception {
        while(!isCancelled()){
            try{
                counter++;
                if(counter > 0 && counter <= tiempoBueno){
                    //do nothing
                } else if (counter > tiempoBueno && counter <= tiempoRegular){
                    //do nothing
                } else if (counter > tiempoRegular && counter < cerrarLlamada) {
                    //do nothing
                } else if (counter >= cerrarLlamada) { //Si el contador es mayor al tiempo configurado en cancelarLlamada
                    pararBoton(boton);
                    this.cancel(true);
                }
                sec.setTime(counter * 1000);
                Thread.sleep(1000);
            } catch( InterruptedException e){
            }
        }
        return null;
    }
    
    public void pararBoton(Boton boton) {
        String ubicacion = boton.getUbicacion();
        llamados = serialService.getLlamados();
        llamados.remove(ubicacion);
        serialService.saveLlamada(boton, SerialService.ACCION_CANCEL);

    }
}
