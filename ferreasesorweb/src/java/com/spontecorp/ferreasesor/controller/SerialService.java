package com.spontecorp.ferreasesor.controller;

import Serialio.PortDriverInfo;
import Serialio.SerialPortLocal;
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
import java.io.IOException;
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
import java.util.logging.Level;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialService implements SerialPortEventListener, Serializable {

    private ConexionJpaController conexionController;
    private AlarmaController alarmaController;
    private final static Logger logger = LoggerFactory.getLogger(SerialService.class);
    private CommPortIdentifier selectedPortIdentifier;
    private SerialPort serialPort;
    private boolean connected;
    private InputStream input;
    private Tienda tienda;
    private List<Feriado> feriados;
    private List<Turno> turnos;
    private Enumeration ports;
    private byte[] buffer;
    private int tail;
    private int tiempoBueno;
    private int tiempoRegular;
    private int cerrarLlamada;
    private boolean isFeriado;
    private Turno turnoActual;
    private final static int TIMEOUT = 1;
    private String portSelected;
    
    private Map<String, CommPortIdentifier> portMap;
    
    // Constantes que representan el tipo de selección
    private static final String ATENCION = "Atención";
    private static final String CUENTA = "Cuenta";
    
    // Constantes a guardar en la BD
    protected static final int ACCION_CANCEL = 0;
    protected static final int ACCION_CUENTA = 1;
    protected static final int ACCION_ATENCION = 2;

    /**
     * Instancia ConexionJpaController
     *
     * @return
     * @throws NamingException
     */
    private ConexionJpaController getConexionController() throws NamingException {
        InitialContext context = new InitialContext();
        return (ConexionJpaController) context.lookup("java:module/ConexionJpaController");
    }

    /**
     * Se llama chequeos autómaticos: Tienda - Feriado y Turnos
     */
    public SerialService() {
        chequeos();
        alarmaController = new AlarmaController();
    }

    /**
     * Chequeos autómaticos: Tienda - Feriado y Turnos
     */
    public final void chequeos() {
        try {
            checkTienda();
            checkFeriado();
            checkTurnos();
        } catch (NamingException namingException) {
            System.out.println("Dio un error: " + namingException);
        }
    }

    /**
     * Se verifica la Tienda actual
     *
     * @throws NamingException
     */
    private void checkTienda() throws NamingException {
        InitialContext context = new InitialContext();
        TiendaFacade tiendaFacade = (TiendaFacade) context.lookup("java:module/TiendaFacade");
        tienda = tiendaFacade.find(JpaUtilities.ID_TIENDA);
    }

    /**
     * Se verifica el Turno Feriado
     */
    private void checkFeriado() {
        try {
            conexionController = getConexionController();
            if (conexionController.findFeriadoEntities().isEmpty()) {
                // Emitir mensaje que feriados no están configurados
            } else {
                feriados = conexionController.findFeriadoEntities();
            }
        } catch (Exception e) {
        }
    }

    /**
     * Se verifica el Turno actual
     *
     * @throws NamingException
     */
    private void checkTurnos() throws NamingException {
        try {
            conexionController = getConexionController();
            turnos = null;
            if (turnos == null) {
                turnos = conexionController.getTurnos();
            }

            if (turnos.isEmpty()) {
                // Emitir mensaje que turnos no están configurados
            }
        } catch (Exception e) {
            logger.error("checkTurnos " + e);
        }
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
        for(Map.Entry<String, CommPortIdentifier> map : portMap.entrySet()){
            System.out.println("Puerto(Key): "+ map.getKey() + " CommPortIdentifier(Value)" + map.getValue().getName());
        }
        return portMap;
    }

    /**
     * Se Listan los Puertos disponibles
     *
     * @return
     * @throws IOException
     */
    public List<String> getPortAvalaibleSelectOne() throws IOException {

        SerialPortLocal.getPortList();
        PortDriverInfo[] diList = SerialPortLocal.getDriverInfoList();
        int nPorts = diList.length;
        List<String> items = new ArrayList<>();
        PortDriverInfo pdi;
        for (int i = 0; i < nPorts; i++) {
            pdi = diList[i];
//            if (pdi.getDriverMfgr().equalsIgnoreCase("Prolific")) {
//                items.add(pdi.getPortName() + ": Dispositivo Vellux");
//            } else {
//                items.add(pdi.getPortName() + ": " + pdi.getDriverDesc());
//            }
           // items.add(pdi.getPortName() + ": " + pdi.getDriverDesc());
             items.add(pdi.getPortName());
        }
        return items;
    }

    /**
     * Aquí se conecta el Equipo Vellux al Sistema
     */
    public void connectAction() {
        searchPorts();
        connect(portSelected);
//        try {
//            PortDriverInfo[] diList = SerialPortLocal.getDriverInfoList();
//            int nPorts = diList.length;
//            PortDriverInfo pdi;
//            for (int i = 0; i < nPorts; i++) {
//                pdi = diList[i];
//               // if (pdi.getDriverMfgr().equalsIgnoreCase("Prolific") && pdi.getDriverDesc().equalsIgnoreCase("Prolific USB-to-Serial Comm Port")) {
//                    connect(pdi.getPortName());
//             //   }
//            }
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(SerialService.class.getName()).log(Level.SEVERE, null, ex);
//        }

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
        System.out.println("Entro a serialEvent: " + spe.getEventType());
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
                logger.error("serialEvent: Ha ocurrido un error capturando la llamada: " + e);
            }
        }
    }

    /**
     * Este método está hecho para funcionar con botones Vellux Dependiendo de
     * la Acción se llama el método pararBoton (action = 0) o arrancaBoton
     * (action = 2)
     *
     * @param botonStr
     */
    private void checkBoton(String botonStr) throws NamingException {

        InitialContext context = new InitialContext();
        BotonJpaControllerExt botonController = (BotonJpaControllerExt) context.lookup("java:module/BotonJpaControllerExt");
        String address = botonStr.substring(0, 4).trim();
        String display = botonStr.substring(4, 7).trim();
        int action = Integer.parseInt(botonStr.substring(7).trim());
        Boton boton = botonController.findBotonAddress(address);
        switch (action) {
            case 0:
                pararBoton(boton);
                break;
            /*case 1:
             arrancaBoton(boton, CUENTA);
             break;*/
            case 2:
                arrancaBoton(boton, ATENCION);
                break;
        }
    }

    /**
     * Método invocado al Cerrar el Botón (action = 0)
     *
     * @param boton
     */
    public void pararBoton(Boton boton) {
        //Se detiene el hilo que muestra el Status del Dispositivo
        alarmaController.detenerBoton(boton.getId());

        //Se actualiza la Llamada en la BD (fecha de cierre y status)
        saveLlamada(boton, ACCION_CANCEL);
    }

    /**
     * Método invocado para arrancar el Botón (action = 2)
     *
     * @param boton
     */
    private void arrancaBoton(Boton boton, String tipo) {
        try {
            //Se chequea el Turno actual
            chequeaTurno();

            //Se arranca el hilo que muestra el Status del Dispositivo
            alarmaController.enviarBoton(boton.getId(), tiempoBueno, tiempoRegular, cerrarLlamada);

            //Se inserta la Llamada en la BD
            saveLlamada(boton, ACCION_ATENCION);

        } catch (Exception e) {
            logger.error("arrancaBoton, Error: " + e);
        }

    }

    /**
     * Método para Crear y Actualizar la Llamada dependiendo de la Acción
     *
     * @param boton
     * @param accion
     */
    protected void saveLlamada(Boton boton, int accion) {
        try {
            InitialContext context = new InitialContext();
            DistribucionJpaControllerExt distControl = (DistribucionJpaControllerExt) context.lookup("java:module/DistribucionJpaControllerExt");
            LlamadaFacadeExt llamFacade = (LlamadaFacadeExt) context.lookup("java:module/LlamadaFacadeExt");

            Llamada llamada = null;
            Calendar calFecha = new GregorianCalendar();
            Calendar calHora = new GregorianCalendar();

            Date momento = new Date();
            calFecha.setTime(momento);
            calFecha.set(calFecha.get(Calendar.YEAR), calFecha.get(Calendar.MONTH), calFecha.get(Calendar.DATE));
            
            //Se busca la Distribución asociada al Turno actual
            Distribucion dist = distControl.findDistribucion(boton, turnoActual);

            //Se listan los tiempos correspondientes al Turno actual
            Tiempo tiempoActual = new Tiempo();
            List<Tiempo> tiempos = turnoActual.getTiempoList();

            for (Tiempo tiempo : tiempos) {
                if (tiempo.getStatus() == 1) {
                    tiempoActual = tiempo;
                    break;
                }
            }

            switch (accion) {
                case ACCION_ATENCION:
                case ACCION_CUENTA:
                    //Si la Acción es 1 o 2 Se inserta el registro en la BD
                    llamada = new Llamada();
                    llamada.setDistribucionId(dist);
                    llamada.setAccion(accion);
                    llamada.setFechaOpen(calFecha.getTime());
                    llamada.setHoraOpen(momento);
                    llamada.setTiendaId(getTienda());
                    llamada.setTiempoId(tiempoActual);
                    llamFacade.create(llamada);
                    break;
                case ACCION_CANCEL:
                    //Si la Acción es 0 se edita el Registro del Botón correspondiente
                    //Status y fecha de cierre de la Llamada
                    llamada.setFechaClose(calFecha.getTime());
                    llamada.setHoraClose(momento);
                    llamada.setAccion(ACCION_CANCEL);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error guardando/actualizando la llamada: " + e);
        }
    }

    /**
     * Se verifica el Turno correspondiente
     */
    public void chequeaTurno() {

        try {
            conexionController = getConexionController();
            turnos = null;
            turnos = conexionController.getTurnos();

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

            //Se verifica si el Turno es Feriado
            if (!conexionController.findFeriadoEntities().isEmpty()) {
                if (calActual.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || isTodayFeriado(calActual)) {
                    isFeriado = true;
                }
            }

            //Se recorre la lista de Turnos para setear el Turno actual
            for (Turno turno : turnos) {
                //Si el Turno es Normal (No es Feriado)
                if (!isFeriado && (turno.getFeriado() == JpaUtilities.NORMAL)) {
                    calTurnoStart.setTime(turno.getHoraStart());
                    calTurnoEnd.setTime(turno.getHoraEnd());

                    horaTurnoStart = calTurnoStart.get(Calendar.HOUR_OF_DAY) + calTurnoStart.get(Calendar.MINUTE) / 60.0;
                    horaTurnoEnd = calTurnoEnd.get(Calendar.HOUR_OF_DAY) + calTurnoEnd.get(Calendar.MINUTE) / 60.0;

                    List<Tiempo> tiempos = null;

                    if (horaActual >= horaTurnoStart && horaActual <= horaTurnoEnd) {
                        turnoActual = turno;
                        tiempos = turnoActual.getTiempoList();
                        
                        //Se obtienen los Tiempos configurados para el Turno actual
                        for (Tiempo tiempo : tiempos) {
                            if (tiempo.getStatus() == 1) {
                                tiempoBueno = tiempo.getAtencionBuena();
                                tiempoRegular = tiempo.getAtencionRegular();
                                cerrarLlamada = tiempo.getCerrarLlamada();
                            }
                        }
                        break;
                    }
                } else {
                    //Si el Turno es Feriado
                    if (isFeriado && (turno.getFeriado() == JpaUtilities.FERIADO)) {
                        calTurnoStart.setTime(turno.getHoraStart());
                        calTurnoEnd.setTime(turno.getHoraEnd());

                        horaTurnoStart = calTurnoStart.get(Calendar.HOUR_OF_DAY) + calTurnoStart.get(Calendar.MINUTE) / 60.0;
                        horaTurnoEnd = calTurnoEnd.get(Calendar.HOUR_OF_DAY) + calTurnoEnd.get(Calendar.MINUTE) / 60.0;

                        List<Tiempo> tiempos = null;

                        if (horaActual >= horaTurnoStart && horaActual <= horaTurnoEnd) {
                            turnoActual = turno;
                            tiempos = turnoActual.getTiempoList();
                            
                            //Se obtienen los Tiempos configurados para el Turno actual
                            for (Tiempo tiempo : tiempos) {
                                if (tiempo.getStatus() == 1) {
                                    tiempoBueno = tiempo.getAtencionBuena();
                                    tiempoRegular = tiempo.getAtencionRegular();
                                    cerrarLlamada = tiempo.getCerrarLlamada();
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("chequeaTurno, Error: " + e);
        }
    }

    /**
     * Método para verificar si el Día actual es Feriado
     * @param today
     * @return
     */
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public Turno getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Turno turnoActual) {
        this.turnoActual = turnoActual;
    }

    public void setPortSelected(String portSelected) {
        this.portSelected = portSelected;
    }
    
    
}
