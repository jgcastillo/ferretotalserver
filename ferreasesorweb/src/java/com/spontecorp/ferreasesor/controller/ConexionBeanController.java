package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Turno;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "conexBeanController")
@ApplicationScoped
public class ConexionBeanController implements Serializable {

    private SerialService serialService;
    private boolean connected;
    private String portSelected;
    private List<String> items = null;
    private Turno turnoActual;

    public ConexionBeanController() {
        serialService = new SerialService();
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getPortSelected() {
        return portSelected;
    }

    public void setPortSelected(String portSelected) {
        this.portSelected = portSelected;
    }

    /**
     * Se listan los Puertos disponibles
     *
     * @return
     * @throws IOException
     */
    public List<String> getItems() {
        try {
            items = serialService.getPortAvalaibleSelectOne();

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ConexionBeanController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    /**
     * Método llamado desde el Botón Conectar en el JSF
     *
     * @param evt
     */
    public void connectButtonActionPerformed(ActionEvent evt) {
        serialService.setPortSelected(portSelected);
        serialService.connectAction();
        if (serialService.isConnected()) {
            connected = serialService.isConnected();
            serialService.initListener();
        }
    }

    /**
     * Desconecta el puerto serial pre: un puerto serial abierto post: cierra el
     * puerto serial
     */
    public void disconnect(ActionEvent evt) {
        serialService.disconnect();
        connected = serialService.isConnected();
    }
    
    /**
     * Se verifica el Turno Actual
     */
    public void updateTurnoActual(){
        serialService.chequeaTurno();
        setTurnoActual(serialService.getTurnoActual());
    } 

    public Turno getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Turno turnoActual) {
        this.turnoActual = turnoActual;
    }
    
    
}
