/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

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

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "llamada")
@NamedQueries({
    @NamedQuery(name = "Llamada.findAll", query = "SELECT l FROM Llamada l"),
    @NamedQuery(name = "Llamada.findById", query = "SELECT l FROM Llamada l WHERE l.id = :id"),
    @NamedQuery(name = "Llamada.findByAccion", query = "SELECT l FROM Llamada l WHERE l.accion = :accion"),
    @NamedQuery(name = "Llamada.findByFechaOpen", query = "SELECT l FROM Llamada l WHERE l.fechaOpen = :fechaOpen"),
    @NamedQuery(name = "Llamada.findByHoraOpen", query = "SELECT l FROM Llamada l WHERE l.horaOpen = :horaOpen"),
    @NamedQuery(name = "Llamada.findByFechaClose", query = "SELECT l FROM Llamada l WHERE l.fechaClose = :fechaClose"),
    @NamedQuery(name = "Llamada.findByHoraClose", query = "SELECT l FROM Llamada l WHERE l.horaClose = :horaClose")})
public class Llamada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "accion")
    private int accion;
    @Basic(optional = false)
    @Column(name = "fecha_Open")
    @Temporal(TemporalType.DATE)
    private Date fechaOpen;
    @Basic(optional = false)
    @Column(name = "hora_Open")
    @Temporal(TemporalType.TIME)
    private Date horaOpen;
    @Column(name = "fecha_Close")
    @Temporal(TemporalType.DATE)
    private Date fechaClose;
    @Column(name = "hora_Close")
    @Temporal(TemporalType.TIME)
    private Date horaClose;
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tienda tiendaId;
    @JoinColumn(name = "distribucion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Distribucion distribucionId;
    @JoinColumn(name = "tiempo_id", referencedColumnName = "id")
    @ManyToOne
    private Tiempo tiempoId;

    public Llamada() {
    }

    public Llamada(Integer id) {
        this.id = id;
    }

    public Llamada(Integer id, int accion, Date fechaOpen, Date horaOpen) {
        this.id = id;
        this.accion = accion;
        this.fechaOpen = fechaOpen;
        this.horaOpen = horaOpen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
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

    public Tienda getTiendaId() {
        return tiendaId;
    }

    public void setTiendaId(Tienda tiendaId) {
        this.tiendaId = tiendaId;
    }

    public Distribucion getDistribucionId() {
        return distribucionId;
    }

    public void setDistribucionId(Distribucion distribucionId) {
        this.distribucionId = distribucionId;
    }

    public Tiempo getTiempoId() {
        return tiempoId;
    }

    public void setTiempoId(Tiempo tiempoId) {
        this.tiempoId = tiempoId;
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
        return "com.spontecorp.ferreasesor.entity.Llamada[ id=" + id + " ]";
    }
    
}
