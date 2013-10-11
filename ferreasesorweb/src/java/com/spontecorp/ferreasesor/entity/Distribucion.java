/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "distribucion")
@NamedQueries({
    @NamedQuery(name = "Distribucion.findAll", query = "SELECT d FROM Distribucion d"),
    @NamedQuery(name = "Distribucion.findById", query = "SELECT d FROM Distribucion d WHERE d.id = :id"),
    @NamedQuery(name = "Distribucion.findByAsesorId", query = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesorId"),
    @NamedQuery(name = "Distribucion.findByBotonId", query = "SELECT d FROM Distribucion d WHERE d.botonId = :botonId"),
    @NamedQuery(name = "Distribucion.findByTurnoId", query = "SELECT d FROM Distribucion d WHERE d.turnoId = :turnoId"),
    @NamedQuery(name = "Distribucion.findByStatus", query = "SELECT d FROM Distribucion d WHERE d.status = :status")})
public class Distribucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "asesor_id")
    private int asesorId;
    @Basic(optional = false)
    @Column(name = "boton_id")
    private int botonId;
    @Basic(optional = false)
    @Column(name = "turno_id")
    private int turnoId;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "distribucionId")
    private List<Llamada> llamadaList;
    private transient String asesorNombre;
    private transient String turnoNombre;
    private transient String botonNombre;

    public Distribucion() {
    }

    public Distribucion(Integer id) {
        this.id = id;
    }

    public Distribucion(Integer id, int asesorId, int botonId, int turnoId, int status) {
        this.id = id;
        this.asesorId = asesorId;
        this.botonId = botonId;
        this.turnoId = turnoId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAsesorId() {
        return asesorId;
    }

    public void setAsesorId(int asesorId) {
        this.asesorId = asesorId;
    }

    public int getBotonId() {
        return botonId;
    }

    public void setBotonId(int botonId) {
        this.botonId = botonId;
    }

    public int getTurnoId() {
        return turnoId;
    }

    public void setTurnoId(int turnoId) {
        this.turnoId = turnoId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Llamada> getLlamadaList() {
        return llamadaList;
    }

    public void setLlamadaList(List<Llamada> llamadaList) {
        this.llamadaList = llamadaList;
    }

    public String getAsesorNombre() {
        return asesorNombre;
    }

    public void setAsesorNombre(String asesorNombre) {
        this.asesorNombre = asesorNombre;
    }

    public String getTurnoNombre() {
        return turnoNombre;
    }

    public void setTurnoNombre(String turnoNombre) {
        this.turnoNombre = turnoNombre;
    }

    public String getBotonNombre() {
        return botonNombre;
    }

    public void setBotonNombre(String botonNombre) {
        this.botonNombre = botonNombre;
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
        if (!(object instanceof Distribucion)) {
            return false;
        }
        Distribucion other = (Distribucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.Distribucion[ id=" + id + " ]";
    }
    
}
