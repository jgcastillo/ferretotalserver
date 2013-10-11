/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "turno")
@NamedQueries({
    @NamedQuery(name = "Turno.findAll", query = "SELECT t FROM Turno t"),
    @NamedQuery(name = "Turno.findById", query = "SELECT t FROM Turno t WHERE t.id = :id"),
    @NamedQuery(name = "Turno.findByNombre", query = "SELECT t FROM Turno t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Turno.findByHoraStart", query = "SELECT t FROM Turno t WHERE t.horaStart = :horaStart"),
    @NamedQuery(name = "Turno.findByHoraEnd", query = "SELECT t FROM Turno t WHERE t.horaEnd = :horaEnd"),
    @NamedQuery(name = "Turno.findByFeriado", query = "SELECT t FROM Turno t WHERE t.feriado = :feriado"),
    @NamedQuery(name = "Turno.findByStatus", query = "SELECT t FROM Turno t WHERE t.status = :status")})
public class Turno implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "hora_start")
    @Temporal(TemporalType.TIME)
    private Date horaStart;
    @Basic(optional = false)
    @Column(name = "hora_end")
    @Temporal(TemporalType.TIME)
    private Date horaEnd;
    @Basic(optional = false)
    @Column(name = "feriado")
    private int feriado;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "turnoId")
    private List<Tiempo> tiempoList;
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tienda tiendaId;

    public Turno() {
    }

    public Turno(Integer id) {
        this.id = id;
    }

    public Turno(Integer id, String nombre, Date horaStart, Date horaEnd, int feriado, int status) {
        this.id = id;
        this.nombre = nombre;
        this.horaStart = horaStart;
        this.horaEnd = horaEnd;
        this.feriado = feriado;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getHoraStart() {
        return horaStart;
    }

    public void setHoraStart(Date horaStart) {
        this.horaStart = horaStart;
    }

    public Date getHoraEnd() {
        return horaEnd;
    }

    public void setHoraEnd(Date horaEnd) {
        this.horaEnd = horaEnd;
    }

    public int getFeriado() {
        return feriado;
    }

    public void setFeriado(int feriado) {
        this.feriado = feriado;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Tiempo> getTiempoList() {
        return tiempoList;
    }

    public void setTiempoList(List<Tiempo> tiempoList) {
        this.tiempoList = tiempoList;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Turno)) {
            return false;
        }
        Turno other = (Turno) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
