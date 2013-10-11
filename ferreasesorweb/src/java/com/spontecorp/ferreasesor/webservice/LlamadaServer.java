package com.spontecorp.ferreasesor.webservice;

import com.google.gson.annotations.Expose;
import com.spontecorp.ferreasesor.entity.Tienda;
import java.io.Serializable;
import java.util.Date;

public class LlamadaServer implements Serializable {

    @Expose
    private Integer id;
    @Expose
    private Date fechaOpen;
    @Expose
    private Date horaOpen;
    @Expose
    private Date fechaClose;
    @Expose
    private Date horaClose;
    @Expose
    private String dispositivo;
    @Expose
    private String asesor;
    @Expose
    private String turno;
    @Expose
    private String calidad;
    @Expose
    private Integer tiempo;
    @Expose
    private Integer feriado;
    @Expose
    private Tienda tiendaId;

    public LlamadaServer() {
    }

    public LlamadaServer(Integer id, Date fechaOpen, Date horaOpen, Date fechaClose, Date horaClose, String dispositivo, String asesor, String turno, String calidad, int tiempo, int feriado) {
        this.id = id;
        this.fechaOpen = fechaOpen;
        this.horaOpen = horaOpen;
        this.fechaClose = fechaClose;
        this.horaClose = horaClose;
        this.dispositivo = dispositivo;
        this.asesor = asesor;
        this.turno = turno;
        this.calidad = calidad;
        this.tiempo = tiempo;
        this.feriado = feriado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaOpen() {
        return fechaOpen;
    }

    public void setFechaOpen(Date fechaOpen) {
        this.fechaOpen = fechaOpen;
    }

    public Date getHoraOpen() {
        return horaOpen;
    }

    public void setHoraOpen(Date horaOpen) {
        this.horaOpen = horaOpen;
    }

    public Date getFechaClose() {
        return fechaClose;
    }

    public void setFechaClose(Date fechaClose) {
        this.fechaClose = fechaClose;
    }

    public Date getHoraClose() {
        return horaClose;
    }

    public void setHoraClose(Date horaClose) {
        this.horaClose = horaClose;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getAsesor() {
        return asesor;
    }

    public void setAsesor(String asesor) {
        this.asesor = asesor;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getCalidad() {
        return calidad;
    }

    public void setCalidad(String calidad) {
        this.calidad = calidad;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getFeriado() {
        return feriado;
    }

    public void setFeriado(int feriado) {
        this.feriado = feriado;
    }

    public void setTiempo(Integer tiempo) {
        this.tiempo = tiempo;
    }

    public void setFeriado(Integer feriado) {
        this.feriado = feriado;
    }

    public Tienda getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Tienda tiendaId) {
        this.tiendaId = tiendaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesorserver.entity.Llamada[ id=" + id + " ]";
    }
}