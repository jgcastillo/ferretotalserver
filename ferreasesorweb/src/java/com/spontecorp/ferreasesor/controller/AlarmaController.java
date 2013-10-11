package com.spontecorp.ferreasesor.controller;

import static com.spontecorp.ferreasesor.controller.SerialService.ACCION_CANCEL;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

/**
 *
 * @author jgcastillo
 */
@Stateless
@ManagedBean(name = "alarmaBean")
@ApplicationScoped
public class AlarmaController implements Serializable {

    private int count;
    private ThreadOnButton hilo;
    public static final String CHANNEL_BOTON1 = "/channelBoton1";
    private Boton boton;
    @EJB
    private BotonFacade botonFacade;
    private int botonId;
    private String ubicacion;
    private int tiempoBueno;
    private int tiempoRegular;
    private int cerrarLlamada;
    private ExecutorService executor = Executors.newCachedThreadPool();
    
    // Mapa con los hilos que se están ejecutando
    private Map<String, Runnable> llamados = new HashMap<>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boton getBoton() {
        return boton;
    }

    public int getBotonId() {
        return botonId;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getTiempoBueno() {
        return tiempoBueno;
    }

    public int getTiempoRegular() {
        return tiempoRegular;
    }

    public int getCerrarLlamada() {
        return cerrarLlamada;
    }

    public void setCerrarLlamada(int cerrarLlamada) {
        this.cerrarLlamada = cerrarLlamada;
    }

    public List<Boton> getBotones() {
        return botonFacade.findAll();
    }

    /**
     * Método para iniciar el hilo que muestra el Status del Dispositivo
     * @param pushContext
     * @param boton
     * @param tiempoBueno
     * @param tiempoRegular
     * @param tCierre 
     */
    public void startThread(PushContext pushContext, Boton boton, int tiempoBueno, int tiempoRegular, int tCierre) {
        String buttonSelected = boton.getUbicacion();

        //Se verifica si el Botón existe en el mapa, 
        //si no existe se crea el hilo y se agrega a llamados
        if (!llamados.containsKey(buttonSelected)) {
            //Se crea el hilo
            hilo = new ThreadOnButton(this, buttonSelected, pushContext, boton, tiempoBueno, tiempoRegular, tCierre);
            
            //Se llena el mapa con el hilo creado y el botón correspondiente
            llamados.put(buttonSelected, hilo);

            hilo.setArrancar();
            executor.execute(hilo);
        }
    }

    /**
     * Método para detener el hilo que muestra el Status del Dispositivo
     * @param boton 
     */
    public void stopThread(Boton boton) {
        String buttonSelected = boton.getUbicacion();
        
        //Se verifica si el Botón existe en el mapa, 
        //si existe se detiene el hilo y se elimina de llamados
        if (llamados.containsKey(buttonSelected)) {
            hilo = (ThreadOnButton) llamados.get(buttonSelected);
            hilo.setTerminar();
            llamados.remove(buttonSelected);
        }
    }

    /**
     * Método para editar la Llamada
     * Actualiza el Status a cerrada y la fecha de cierre
     * @param boton 
     */
    public void detenerLlamada(Boton boton) {
        try {
            Date momento = new Date();
            Calendar calFecha = new GregorianCalendar();
            calFecha.setTime(momento);
            calFecha.set(calFecha.get(Calendar.YEAR), calFecha.get(Calendar.MONTH), calFecha.get(Calendar.DATE));
            InitialContext context = new InitialContext();
            
            LlamadaFacadeExt llamadaFacade = (LlamadaFacadeExt) context.lookup("java:module/LlamadaFacadeExt");
            Llamada llamada = llamadaFacade.findLlamadaAbierta(boton);
            
            llamada.setFechaClose(calFecha.getTime());
            llamada.setHoraClose(momento);
            llamada.setAccion(ACCION_CANCEL);
            llamadaFacade.edit(llamada);
        } catch (NamingException ex) {
            Logger.getLogger(AlarmaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para preparar el hilo que mostrará el Status del Dispositivo
     * @param botonId
     * @param tBueno
     * @param tRegular
     * @param tCierre 
     */
    public void enviarBoton(int botonId, int tBueno, int tRegular, int tCierre) {
        try {
            InitialContext context = new InitialContext();
            BotonFacade botonFacade1 = (BotonFacade) context.lookup("java:module/BotonFacade");
            boton = botonFacade1.find(botonId);
            
            this.botonId = boton.getId();
            this.ubicacion = boton.getUbicacion();
            this.tiempoBueno = tBueno;
            this.tiempoRegular = tRegular;
            this.cerrarLlamada = tCierre;
            
            PushContext pushContext = PushContextFactory.getDefault().getPushContext();
            
            //Arrancar el Hilo
            startThread(pushContext, boton, tiempoBueno, tiempoRegular, cerrarLlamada);
            
        } catch (NamingException ex) {
            Logger.getLogger(AlarmaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este método se invoca para detener el hilo que muestra el Status del Dispositivo
     * 
     * @param botonId 
     */
    public void detenerBoton(int botonId) {
        try {
            InitialContext context = new InitialContext();
            BotonFacade btnFacade = (BotonFacade) context.lookup("java:module/BotonFacade");
            boton = btnFacade.find(botonId);
            
            //Detener el Hilo
            stopThread(boton);
        } catch (NamingException ex) {
            Logger.getLogger(AlarmaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, Runnable> getLlamados() {
        return llamados;
    }

    public void setLlamados(Map<String, Runnable> llamados) {
        this.llamados = llamados;
    }
}
