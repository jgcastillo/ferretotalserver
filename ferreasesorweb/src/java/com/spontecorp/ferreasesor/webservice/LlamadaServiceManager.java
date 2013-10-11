package com.spontecorp.ferreasesor.webservice;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Llamada;

import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import com.spontecorp.ferreasesor.utilities.WebServicesUtilities;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LlamadaServiceManager implements Serializable {

    private List<LlamadaServer> listaLlamadasServer = new ArrayList<>();
    private LlamadaServer llamadaServer = new LlamadaServer();
    private Llamada llamada;
    private List<Llamada> listaLlamadas;

    /**
     * Obtener Lista de Llamadas de la Tienda por Fecha
     *
     * @param fechaInicio
     * @param fechaFin
     */
    public List<LlamadaServer> llenarListaLlamadasServer(Date fechaInicio, Date fechaFin) {
        try {

            InitialContext context = new InitialContext();
            LlamadaFacadeExt llamadaFacadeExt = (LlamadaFacadeExt) context.lookup("java:module/LlamadaFacadeExt");
            listaLlamadas = llamadaFacadeExt.findLlamadasList(fechaInicio, fechaFin);

            for (int i = 0; i < listaLlamadas.size(); i++) {

                llamadaServer.setHoraOpen(listaLlamadas.get(i).getHoraOpen());
                llamadaServer.setFechaOpen(listaLlamadas.get(i).getFechaOpen());
                llamadaServer.setHoraClose(listaLlamadas.get(i).getHoraClose());
                llamadaServer.setFechaClose(listaLlamadas.get(i).getFechaClose());
                llamadaServer.setDispositivo(obtenerNombreDispositivo(listaLlamadas.get(i)));
                llamadaServer.setTurno(obtenerTurno(listaLlamadas.get(i)));
                llamadaServer.setAsesor(obtenerNombreAsesor(listaLlamadas.get(i)));
                llamadaServer.setTiempo(obtenerTiempoLlamada(listaLlamadas.get(i)));
                llamadaServer.setCalidad(obtenerCalidad(listaLlamadas.get(i)));
                llamadaServer.setFeriado(obtenerIsFeriado(listaLlamadas.get(i)));
                llamadaServer.setTiendaId(obtenerTienda(listaLlamadas.get(i)));
                addLlamadaServer(llamadaServer);
                llamadaServer = new LlamadaServer();
            }

        } catch (NamingException ex) {
            Logger.getLogger(LlamadaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaLlamadasServer;
    }

    /**
     * Obtener Lista de todas las Llmadas de la Tienda
     *
     * @return
     */
    public List<LlamadaServer> llenarListaLlamadasServer() {
        try {

            InitialContext context = new InitialContext();
            LlamadaFacadeExt llamadaFacadeExt = (LlamadaFacadeExt) context.lookup("java:module/LlamadaFacadeExt");
            listaLlamadas = llamadaFacadeExt.findLlamadasList();

            for (int i = 0; i < listaLlamadas.size(); i++) {

                llamadaServer.setHoraOpen(listaLlamadas.get(i).getHoraOpen());
                llamadaServer.setFechaOpen(listaLlamadas.get(i).getFechaOpen());
                llamadaServer.setHoraClose(listaLlamadas.get(i).getHoraClose());
                llamadaServer.setFechaClose(listaLlamadas.get(i).getFechaClose());
                llamadaServer.setDispositivo(obtenerNombreDispositivo(listaLlamadas.get(i)));
                llamadaServer.setTurno(obtenerTurno(listaLlamadas.get(i)));
                llamadaServer.setAsesor(obtenerNombreAsesor(listaLlamadas.get(i)));
                llamadaServer.setTiempo(obtenerTiempoLlamada(listaLlamadas.get(i)));
                llamadaServer.setCalidad(obtenerCalidad(listaLlamadas.get(i)));
                llamadaServer.setFeriado(obtenerIsFeriado(listaLlamadas.get(i)));
                llamadaServer.setTiendaId(obtenerTienda(listaLlamadas.get(i)));
                addLlamadaServer(llamadaServer);
                llamadaServer = new LlamadaServer();
            }

        } catch (NamingException ex) {
            Logger.getLogger(LlamadaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaLlamadasServer;
    }

    /**
     * Obtener Lista de todas las Llmadas de la Tienda que todavía no han sido
     * actualizadas en la BD del Server
     *
     * @param fechaInicio
     * @param horaInicio
     * @return
     */
    public List<LlamadaServer> llenarListaUpdateLlamadasServer(Date fechaInicio, Date horaInicio) {
        try {

            InitialContext context = new InitialContext();
            LlamadaFacadeExt llamadaFacadeExt = (LlamadaFacadeExt) context.lookup("java:module/LlamadaFacadeExt");
            listaLlamadas = llamadaFacadeExt.findLlamadasUpdateList(fechaInicio, horaInicio);

            for (int i = 0; i < listaLlamadas.size(); i++) {

                llamadaServer.setHoraOpen(listaLlamadas.get(i).getHoraOpen());
                llamadaServer.setFechaOpen(listaLlamadas.get(i).getFechaOpen());
                llamadaServer.setHoraClose(listaLlamadas.get(i).getHoraClose());
                llamadaServer.setFechaClose(listaLlamadas.get(i).getFechaClose());
                llamadaServer.setDispositivo(obtenerNombreDispositivo(listaLlamadas.get(i)));
                llamadaServer.setTurno(obtenerTurno(listaLlamadas.get(i)));
                llamadaServer.setAsesor(obtenerNombreAsesor(listaLlamadas.get(i)));
                llamadaServer.setTiempo(obtenerTiempoLlamada(listaLlamadas.get(i)));
                llamadaServer.setCalidad(obtenerCalidad(listaLlamadas.get(i)));
                llamadaServer.setFeriado(obtenerIsFeriado(listaLlamadas.get(i)));
                llamadaServer.setTiendaId(obtenerTienda(listaLlamadas.get(i)));
                addLlamadaServer(llamadaServer);
                llamadaServer = new LlamadaServer();
            }

        } catch (NamingException ex) {
            Logger.getLogger(LlamadaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaLlamadasServer;
    }

    /**
     * Método para obtener la surursal de una tienda de una llamada dada
     *
     * @param llamada
     * @return id de la tienda
     * @throws NamingException
     */
    public Tienda obtenerTienda(Llamada llamada) {

        return llamada.getTiendaId();

    }

    /**
     * Método para retornar el nombre turno de una llamada dada
     *
     * @param ll
     * @return Turno de la Llamada (Nombre)
     * @throws NamingException
     */
    public String obtenerTurno(Llamada llamada) throws NamingException {
        InitialContext context = new InitialContext();
        TurnoFacade turnoFacade = (TurnoFacade) context.lookup("java:module/TurnoFacade");
        int turnoId = llamada.getDistribucionId().getTurnoId();
        Turno turno = turnoFacade.find(turnoId);
        return turno.getNombre();
    }

    /**
     * Método para obtener el valor del valor feriado de una llamada dada
     *
     * @param llamada
     * @return numero identificador (0 Dia normal , 1 dia Feriado)
     */
    public int obtenerIsFeriado(Llamada llamada) throws NamingException {
        InitialContext context = new InitialContext();
        TurnoFacade turnoFacade = (TurnoFacade) context.lookup("java:module/TurnoFacade");
        int turnoId = llamada.getDistribucionId().getTurnoId();
        Turno turno = turnoFacade.find(turnoId);
        return turno.getFeriado();
    }

    /**
     * Método que retorna la calidad de una llamada dada (buena, regular, mala,
     * automatica)
     *
     * @param llamada
     * @return calidad de la atencion
     */
    public String obtenerCalidad(Llamada llamada) {
        int atencionBuena = llamada.getTiempoId().getAtencionBuena();
        int atencionRegular = llamada.getTiempoId().getAtencionRegular();
        int cierreAutomatico = llamada.getTiempoId().getCerrarLlamada();
        String atencion = "";
        int tiempo = (int) ((llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000);

        if (tiempo <= atencionBuena) {
            atencion = WebServicesUtilities.ATENCION_BUENA;
        } else if (tiempo > atencionBuena && tiempo <= atencionRegular) {
            atencion = WebServicesUtilities.ATENCION_REGULAR;
        } else if (tiempo > atencionRegular && tiempo < cierreAutomatico) {
            atencion = WebServicesUtilities.ATENCION_MALA;
        } else if (tiempo >= cierreAutomatico) {
            atencion = WebServicesUtilities.CIERRE_AUTOMATICO;
        }
        return atencion;
    }

    /**
     * Funcion que permite obtener el tiempo en segundos que tardo una llamada
     * dada
     *
     * @param llamada
     * @return Tiempo en Segundos
     */
    public int obtenerTiempoLlamada(Llamada llamada) {
        int tiempo = (int) ((llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000);
        return tiempo;
    }

    /**
     * Método que permite obtener el nombre y apellido de un ferreasesor dada
     * una llamada
     *
     * @param llamada
     * @return Nombre Completo
     */
    public String obtenerNombreAsesor(Llamada llamada) throws NamingException {
        InitialContext context = new InitialContext();
        AsesorFacade asesorFacade = (AsesorFacade) context.lookup("java:module/AsesorFacade");

        int asesorId = llamada.getDistribucionId().getAsesorId();
        Asesor asesor = asesorFacade.find(asesorId);
        String nombreCompleto = asesor.getNombre() + " " + asesor.getApellido();
        return nombreCompleto;
    }

    /**
     * Método para hallar el nombre de un boton dados los datos de la llamada
     *
     * @param llamada
     * @return Nombre de la Ubicacion
     */
    public String obtenerNombreDispositivo(Llamada llamada) throws NamingException {
        InitialContext context = new InitialContext();
        BotonFacade botonFacade = (BotonFacade) context.lookup("java:module/BotonFacade");
        int botonId = llamada.getDistribucionId().getBotonId();
        Boton boton = botonFacade.find(botonId);
        String ubicacion = boton.getUbicacion();
        return ubicacion;
    }

    /**
     * Método para recibir fecha String en formato dd-MM-yyyy y retornar un
     * objeto tipo Date con la fecha dada
     *
     * @param fecha
     * @return Fecha tipo "Date"
     */
    public Date convertirFecha(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            date = formateador.parse(fecha);

        } catch (ParseException ex) {
            Logger.getLogger(LlamadaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * Método para recibir hora String en formato HH:mm:ss y retornar un
     * objeto tipo Date con la hora dada
     *
     * @param hora
     * @return hora tipo "Date"
     */
    public Date convertirHora(String hora) {
        Date date = null;
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
            date = formateador.parse(hora);

        } catch (ParseException ex) {
            Logger.getLogger(LlamadaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    public LlamadaServiceManager() {
    }

    public List<LlamadaServer> getListaLlamadasServer() {
        return listaLlamadasServer;
    }

    public void setListaLlamadasServer(List<LlamadaServer> listaLlamadasServer) {
        this.listaLlamadasServer = listaLlamadasServer;
    }

    public Llamada getLlamada() {
        return llamada;
    }

    public void setLlamada(Llamada llamada) {
        this.llamada = llamada;
    }

    public List<Llamada> getListaLlamadas() {
        return listaLlamadas;
    }

    public void setListaLlamadas(List<Llamada> listaLlamadas) {
        this.listaLlamadas = listaLlamadas;
    }

    public LlamadaServer getLlamadaServer() {
        return llamadaServer;
    }

    public void setLlamadaServer(LlamadaServer llamadaServer) {
        this.llamadaServer = llamadaServer;
    }

    public void addLlamadaServer(LlamadaServer llamadaServer) {
        listaLlamadasServer.add(llamadaServer);
    }
}