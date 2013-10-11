package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Feriado;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Tiempo;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.jpa.ext.BotonJpaControllerExt;
import com.spontecorp.ferreasesor.jpa.ext.ConexionJpaController;
import com.spontecorp.ferreasesor.jpa.ext.DistribucionJpaControllerExt;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "conexionBean")
@ApplicationScoped
public class ConexionController implements SerialPortEventListener, Serializable{

    private String portSelected;
    private boolean connected;
    private Enumeration ports;
    private Map<String, CommPortIdentifier> portMap;
    private InputStream input;
    private CommPortIdentifier selectedPortIdentifier;
    private SerialPort serialPort;
    @EJB
    private BotonJpaControllerExt botonController;
    @EJB
    private DistribucionJpaControllerExt distController;
    @EJB
    private LlamadaFacadeExt llamadaController;
    @EJB
    private TiendaFacade tiendaFacade;
    //private TurnoJpaController turnoController;
    private ConexionJpaController controller;
    private final static int TIMEOUT = 1;
    protected Map<String, Runnable> llamados = new HashMap<>();
    private final static Logger logger = LoggerFactory.getLogger(ConexionController.class);
    private byte[] buffer;
    private int tail;
    private boolean isFeriado;
    private List<Feriado> feriados;
    private List<Turno> turnos;
    private Turno turnoActual;
    private Tienda tienda;
    private int tiempoBueno;
    private int tiempoRegular;
    private int cerrarLlamada;
    //private ThreadOnPlaces hilo;
    // Constantes que representan el tipo de selección
    private static final String ATENCION = "Atención";
    private static final String CUENTA = "Cuenta";
    // Constantes a guardar en la BD
    protected static final int ACCION_CANCEL = 0;
    protected static final int ACCION_CUENTA = 1;
    protected static final int ACCION_ATENCION = 2;
    private static AlarmaController alarmaController;

    
    public ConexionController() {
        chequeos();
    }

    public String getPortSelected() {
        return portSelected;
    }

    public void setPortSelected(String portSelected) {
        this.portSelected = portSelected;
    }
    
    public List<String> getPortAvalaibleSelectOne(){
        System.out.println("Entrada al getPOrtAvalaible");
        Map<String, CommPortIdentifier> mapa = searchPorts();
        List<String> items = new ArrayList<>();
        for (Map.Entry<String, CommPortIdentifier> entry : mapa.entrySet()) {
            items.add(entry.getKey());
        }
        return items;
    }

    
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public Map<String, Runnable> getLlamados() {
        return llamados;
    }

    private void chequeos() {
        checkTienda();
        checkFeriado();
        checkTurnos();
    }

    /**
     * Busca los puertos disponibles y los agrega a un Map, para ser usado en un
     * comboBox
     */
    public Map searchPorts() {
        ports = CommPortIdentifier.getPortIdentifiers();
        portMap = new HashMap();
        while (ports.hasMoreElements()) {
            CommPortIdentifier curPort = (CommPortIdentifier) ports.nextElement();
            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                //window.portsComboBox.addItem(curPort.getName());
                portMap.put(curPort.getName(), curPort);
            }
        }
        return portMap;
    }

    /**
     * Se conecta al puerto seleccionado en el portsComboBox, pre: los puertos
     * encontrados usando el método searchForPorts post: el puerto conectado es
     * almacenado en commPort, si falla, una excepción es generada
     */
    public void connect(String selectedPort) {
        selectedPortIdentifier = (CommPortIdentifier) portMap.get(selectedPort);
        try {
            serialPort = (SerialPort) selectedPortIdentifier.open("Spontecorp Data Analizer", TIMEOUT);
            serialPort.setSerialPortParams(9600,
                    javax.comm.SerialPort.DATABITS_8,
                    javax.comm.SerialPort.STOPBITS_1,
                    javax.comm.SerialPort.PARITY_NONE);
            input = serialPort.getInputStream();
            setConnected(true);

        } catch (PortInUseException e) {
            logger.error(selectedPort + " está en uso. (" + e.toString() + ")");
        } catch (Exception e) {
            logger.error("No pudo abrir el " + selectedPort + "(" + e.toString() + ")");
        }
    }

    /**
     * Desconecta el puerto serial pre: un puerto serial abierto post: cierra el
     * puerto serial
     */
    public void disconnect() {
        try {
            serialPort.removeEventListener();
            serialPort.close();
            input.close();
            //output.close();
            setConnected(false);
            logger.info("Equipo desconectado");
        } catch (Exception e) {
            logger.error("Falló al cerrar " + serialPort.getName() + "(" + e.toString() + ")");
        }
    }

    /**
     * Arranca el event listener que conoce si hay datos disponibles para ser
     * leidos pre: un puerto serial abierto post: un event listener para el
     * puerto serial para conocer cuando hay datos recibidos
     */
    public void initListener() {
        try {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            logger.error("Demasiados listeners. (" + e.toString() + ")");
        }
    }

    /**
     * Detecta que hay data presente en el puerto serial al que está conectado
     *
     * @param spe
     */
    @Override
    public void serialEvent(SerialPortEvent spe) {
        buffer = new byte[8];
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                tail = 0;
                int b = 0;
                // si el stream no se ha completado el método retorna -1
                while (tail < 8) {
                    // simple protocolo: cada mensaje termina con una nueva linea
                    b = input.read();
                    if (b != '\n') {
                        buffer[tail] = (byte) b;
                        tail++;
                    }
                }
                checkBoton(new String(buffer));
            } catch (Exception e) {
                logger.error("Ha ocurrido un error capturando la llamada: " + e);
            }
        }
    }

    /*
     * Este método está hecho para funcionar con botones Vellux
     */
    private void checkBoton(String botonStr) {
        String address = botonStr.substring(0, 4).trim();
        String display = botonStr.substring(4, 7).trim();

        int action = Integer.parseInt(botonStr.substring(7).trim());
        Boton boton = botonController.findBotonByAddress(address);
        switch (action) {
            case 0:
                pararBoton(boton);
                break;
//            case 1:
//                arrancaBoton(boton, CUENTA);
//                break;
            case 2:
                arrancaBoton(boton, ATENCION);
                break;
        }
    }

    public void pararBoton(Boton boton) {
        String ubicacion = boton.getUbicacion();
        if (llamados.containsKey(ubicacion)) {
//            hilo = (ThreadOnPlaces) llamados.get(ubicacion);
//            hilo.cancel(true);
            llamados.remove(ubicacion);
            stopAlarma(boton.getId());
            saveLlamada(boton, ACCION_CANCEL);

        }
    }

    private void arrancaBoton(Boton boton, String tipo) {
        String ubicacion = boton.getUbicacion();
        if (!llamados.containsKey(ubicacion)) {
            if (tipo.equals(ATENCION)) {

                chequeaTurno();

//                hilo = new ThreadOnPlaces(this, tiempoBueno, tiempoRegular, cerrarLlamada, boton);
//                hilo.execute();
//                llamados.put(ubicacion, hilo);
                arrancaAlarma(boton.getId(), tiempoBueno, tiempoRegular, cerrarLlamada);
                saveLlamada(boton, ACCION_ATENCION);
            }
        }
    }
    
    private boolean isTodayFeriado(Calendar today) {
        Calendar calTestFeriado = new GregorianCalendar();
        boolean result = false;
        for (Feriado feriado : feriados) {
            calTestFeriado.set(today.get(Calendar.YEAR), feriado.getMes(), feriado.getDia());
            if (calTestFeriado.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                    && calTestFeriado.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                    && calTestFeriado.get(Calendar.DATE) == today.get(Calendar.DATE)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void checkFeriado() {
        if (controller.findFeriadoEntities().isEmpty()) {
            // Emitir mensaje que feriados no están configurados
        } else {
            feriados = controller.findFeriadoEntities();
        }
    }

    private void checkTurnos() {
        turnos = null;
        if (turnos == null){
            turnos = controller.getTurnos();
        }
        
        if (turnos.isEmpty()) {
            // Emitir mensaje que turnos no están configurados
        }
    }

    private void checkTienda() {
        System.out.println("el tiendaFacade vale:" + tiendaFacade.toString());
//        if (controller.getTienda() == null) {
//            // Emitir mensaje que tienda no está configurada
//        } else {
//            tienda = controller.getTienda();
//        }
        tienda = tiendaFacade.find(JpaUtilities.ID_TIENDA);
        System.out.println("Tienda : " + tienda);
    }

    protected void saveLlamada(Boton boton, int accion) {
        Llamada llamada = null;
        Calendar calFecha = new GregorianCalendar();
        Calendar calHora = new GregorianCalendar();
        try {
            Date momento = new Date();
            calFecha.setTime(momento);
            calFecha.set(calFecha.get(Calendar.YEAR), calFecha.get(Calendar.MONTH), calFecha.get(Calendar.DATE));
            Distribucion dist = distController.findDistribucion(boton, turnoActual);

            switch (accion) {
                case ACCION_ATENCION:
                case ACCION_CUENTA:
                    llamada = new Llamada();
                    llamada.setDistribucionId(dist);
                    llamada.setAccion(accion);
                    llamada.setFechaOpen(calFecha.getTime());
                    llamada.setHoraOpen(momento);
                    llamada.setTiendaId(getTienda());
                    llamadaController.create(llamada);
                    break;
                case ACCION_CANCEL:
                    llamada = llamadaController.findLlamadaAbierta(boton);
                    llamada.setFechaClose(calFecha.getTime());
                    llamada.setHoraClose(momento);
                    llamada.setAccion(ACCION_CANCEL);
                    llamadaController.edit(llamada);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error guardando/actualizando la llamada: " + e);
        }
    }

    private void chequeaTurno() {
        turnos = null;
        turnos = controller.getTurnos();
        tiempoBueno = 0;
        tiempoRegular = 0;
        cerrarLlamada = 0;
        
        Calendar calActual = new GregorianCalendar();
        Calendar calTurnoStart = new GregorianCalendar();
        Calendar calTurnoEnd = new GregorianCalendar();
        double horaActual;
        double horaTurnoStart;
        double horaTurnoEnd;

        isFeriado = false;
        Date fechaActual = new Date(System.currentTimeMillis());
        calActual.setTime(fechaActual);
        horaActual = (double) (calActual.get(Calendar.HOUR_OF_DAY) + calActual.get(Calendar.MINUTE) / 60.0);

        if (!controller.findFeriadoEntities().isEmpty()) {
            if (calActual.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isTodayFeriado(calActual)) {
                isFeriado = true;
            }
        }

        for (Turno turno : turnos) {
            if (!isFeriado && (turno.getFeriado() == JpaUtilities.NORMAL)) {
                calTurnoStart.setTime(turno.getHoraStart());
                calTurnoEnd.setTime(turno.getHoraEnd());

                horaTurnoStart = calTurnoStart.get(Calendar.HOUR_OF_DAY) + calTurnoStart.get(Calendar.MINUTE) / 60.0;
                horaTurnoEnd = calTurnoEnd.get(Calendar.HOUR_OF_DAY) + calTurnoEnd.get(Calendar.MINUTE) / 60.0;

                List<Tiempo> tiempos = null;
                
                if (horaActual >= horaTurnoStart && horaActual <= horaTurnoEnd) {
                    turnoActual = turno;
                    tiempos = turnoActual.getTiempoList();

                    for (Tiempo tiempo : tiempos) {
                        tiempoBueno = tiempo.getAtencionBuena();
                        tiempoRegular = tiempo.getAtencionRegular();
                        cerrarLlamada = tiempo.getCerrarLlamada();
                    }
                    break;
                }
            } else {
                if (isFeriado && (turno.getFeriado() == JpaUtilities.FERIADO)) {
                    calTurnoStart.setTime(turno.getHoraStart());
                    calTurnoEnd.setTime(turno.getHoraEnd());

                    horaTurnoStart = calTurnoStart.get(Calendar.HOUR_OF_DAY) + calTurnoStart.get(Calendar.MINUTE) / 60.0;
                    horaTurnoEnd = calTurnoEnd.get(Calendar.HOUR_OF_DAY) + calTurnoEnd.get(Calendar.MINUTE) / 60.0;
                    
                    List<Tiempo> tiempos = null;

                    if (horaActual >= horaTurnoStart && horaActual <= horaTurnoEnd) {
                        turnoActual = turno;
                        tiempos = turnoActual.getTiempoList();
                        for (Tiempo tiempo : tiempos) {
                            tiempoBueno = tiempo.getAtencionBuena();
                            tiempoRegular = tiempo.getAtencionRegular();
                            cerrarLlamada = tiempo.getCerrarLlamada();
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void arrancaAlarma(int botonId, int tBueno, int tRegular, int tCierre) {
//        com.ferretotal.serialservice.ws.AlarmaWebService_Service service = new com.ferretotal.serialservice.ws.AlarmaWebService_Service();
//        com.ferretotal.serialservice.ws.AlarmaWebService port = service.getAlarmaWebServicePort();
//        port.arrancaAlarma(botonId, tBueno, tRegular, tCierre);
        alarmaController.enviarBoton(botonId, tBueno, tRegular, tCierre);
    }

    private static void stopAlarma(int botonId) {
//        com.ferretotal.serialservice.ws.AlarmaWebService_Service service = new com.ferretotal.serialservice.ws.AlarmaWebService_Service();
//        com.ferretotal.serialservice.ws.AlarmaWebService port = service.getAlarmaWebServicePort();
//        port.stopAlarma(botonId);
        alarmaController.detenerBoton(botonId);
    }
    
    

}
