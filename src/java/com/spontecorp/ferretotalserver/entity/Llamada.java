/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sponte03
 */
@Entity
@Table(name = "llamada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Llamada.findAll", query = "SELECT l FROM Llamada l"),
    @NamedQuery(name = "Llamada.findById", query = "SELECT l FROM Llamada l WHERE l.id = :id"),
    @NamedQuery(name = "Llamada.findByFechaOpen", query = "SELECT l FROM Llamada l WHERE l.fechaOpen = :fechaOpen"),
    @NamedQuery(name = "Llamada.findByHoraOpen", query = "SELECT l FROM Llamada l WHERE l.horaOpen = :horaOpen"),
    @NamedQuery(name = "Llamada.findByFechaClose", query = "SELECT l FROM Llamada l WHERE l.fechaClose = :fechaClose"),
    @NamedQuery(name = "Llamada.findByHoraClose", query = "SELECT l FROM Llamada l WHERE l.horaClose = :horaClose"),
    @NamedQuery(name = "Llamada.findByDispositivo", query = "SELECT l FROM Llamada l WHERE l.dispositivo = :dispositivo"),
    @NamedQuery(name = "Llamada.findByAsesor", query = "SELECT l FROM Llamada l WHERE l.asesor = :asesor"),
    @NamedQuery(name = "Llamada.findByTurno", query = "SELECT l FROM Llamada l WHERE l.turno = :turno"),
    @NamedQuery(name = "Llamada.findByCalidad", query = "SELECT l FROM Llamada l WHERE l.calidad = :calidad"),
    @NamedQuery(name = "Llamada.findByTiempo", query = "SELECT l FROM Llamada l WHERE l.tiempo = :tiempo"),
    @NamedQuery(name = "Llamada.findByFeriado", query = "SELECT l FROM Llamada l WHERE l.feriado = :feriado")})
public class Llamada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_Open")
    @Temporal(TemporalType.DATE)
    private Date fechaOpen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_Open")
    @Temporal(TemporalType.TIME)
    private Date horaOpen;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_Close")
    @Temporal(TemporalType.DATE)
    private Date fechaClose;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hora_Close")
    @Temporal(TemporalType.TIME)
    private Date horaClose;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "dispositivo")
    private String dispositivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "asesor")
    private String asesor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "turno")
    private String turno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "calidad")
    private String calidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tiempo")
    private int tiempo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feriado")
    private int feriado;
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tienda tiendaId;
    @Transient
    private String feriadoNormal;

    public Llamada() {
    }

    public Llamada(Integer id) {
        this.id = id;
    }

    public Llamada(Integer id, Date fechaOpen, Date horaOpen, Date fechaClose, Date horaClose, String dispositivo, String asesor, String turno, String calidad, int tiempo, int feriado) {
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

    public Tienda getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Tienda tiendaId) {
        this.tiendaId = tiendaId;
    }

    public String getFeriadoNormal() {
        return feriadoNormal;
    }

    public void setFeriadoNormal(String feriadoNormal) {
        this.feriadoNormal = feriadoNormal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Llamada)) {
            return false;
        }
        Llamada other = (Llamada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferretotalserver.entity.Llamada[ id=" + id + " ]";
    }
    
}
