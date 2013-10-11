/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;
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

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "tiempo")
@NamedQueries({
    @NamedQuery(name = "Tiempo.findAll", query = "SELECT t FROM Tiempo t"),
    @NamedQuery(name = "Tiempo.findById", query = "SELECT t FROM Tiempo t WHERE t.id = :id"),
    @NamedQuery(name = "Tiempo.findByAtencionBuena", query = "SELECT t FROM Tiempo t WHERE t.atencionBuena = :atencionBuena"),
    @NamedQuery(name = "Tiempo.findByAtencionRegular", query = "SELECT t FROM Tiempo t WHERE t.atencionRegular = :atencionRegular"),
    @NamedQuery(name = "Tiempo.findByStatus", query = "SELECT t FROM Tiempo t WHERE t.status = :status")})
public class Tiempo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "atencion_buena")
    private int atencionBuena;
    @Basic(optional = false)
    @Column(name = "atencion_regular")
    private int atencionRegular;
    @Basic(optional = false)
    @Column(name = "cerrar_llamada")
    private int cerrarLlamada;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @JoinColumn(name = "turno_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Turno turnoId;

    public Tiempo() {
    }

    public Tiempo(Integer id) {
        this.id = id;
    }

    public Tiempo(Integer id, int atencionBuena, int atencionRegular, int status) {
        this.id = id;
        this.atencionBuena = atencionBuena;
        this.atencionRegular = atencionRegular;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAtencionBuena() {
        return atencionBuena;
    }

    public void setAtencionBuena(int atencionBuena) {
        this.atencionBuena = atencionBuena;
    }

    public int getAtencionRegular() {
        return atencionRegular;
    }

    public void setAtencionRegular(int atencionRegular) {
        this.atencionRegular = atencionRegular;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Turno getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(Turno turnoId) {
        this.turnoId = turnoId;
    }

    public int getCerrarLlamada() {
        return cerrarLlamada;
    }

    public void setCerrarLlamada(int cerrarLlamada) {
        this.cerrarLlamada = cerrarLlamada;
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
        if (!(object instanceof Tiempo)) {
            return false;
        }
        Tiempo other = (Tiempo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.Tiempo[ id=" + id + " ]";
    }
    
}
