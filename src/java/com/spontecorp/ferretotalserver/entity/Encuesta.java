/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.entity;

import com.google.gson.annotations.Expose;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sponte03
 */
@Entity
@Table(name = "encuesta")
@NamedQueries({
    @NamedQuery(name = "Encuesta.findAll", query = "SELECT e FROM Encuesta e"),
    @NamedQuery(name = "Encuesta.findById", query = "SELECT e FROM Encuesta e WHERE e.id = :id"),
    @NamedQuery(name = "Encuesta.findByNombre", query = "SELECT e FROM Encuesta e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Encuesta.findByFechaInicio", query = "SELECT e FROM Encuesta e WHERE e.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Encuesta.findByFechaFin", query = "SELECT e FROM Encuesta e WHERE e.fechaFin = :fechaFin"),
    @NamedQuery(name = "Encuesta.findByStatus", query = "SELECT e FROM Encuesta e WHERE e.status = :status"),
    @NamedQuery(name = "Encuesta.findByGlobal", query = "SELECT e FROM Encuesta e WHERE e.global = :global"),
    @NamedQuery(name = "Encuesta.findByCodigo", query = "SELECT e FROM Encuesta e WHERE e.codigo = :codigo")})
public class Encuesta implements Serializable {
 
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)

    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    
    private Date fechaFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    @Expose
    private int status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "global")
    private int global;
    @Size(max = 45)
    @Column(name = "codigo")
    private String codigo;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "encuestaId")
    @Expose    
    private List<Pregunta> preguntaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encuestaId")
    private List<RespuestaObtenida> respuestaObtenidaList;
    
    @Expose
    @Transient
    private String fechaInicioString;
    
    @Expose
    @Transient
    private String fechaFinString;
    
    public Encuesta() {
    }

    public Encuesta(Integer id) {
        this.id = id;
    }

    public Encuesta(Integer id, String nombre, Date fechaInicio, int status, int global) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.status = status;
        this.global = global;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGlobal() {
        return global;
    }

    public void setGlobal(int global) {
        this.global = global;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<Pregunta> getPreguntaList() {
        return preguntaList;
    }

    public void setPreguntaList(List<Pregunta> preguntaList) {
        this.preguntaList = preguntaList;
    }

  

    public List<RespuestaObtenida> getRespuestaObtenidaList() {
        return respuestaObtenidaList;
    }

    public void setRespuestaObtenidaList(List<RespuestaObtenida> respuestaObtenidaList) {
        this.respuestaObtenidaList = respuestaObtenidaList;
    }

    public String getFechaInicioString() {
        return fechaInicioString;
    }

    public void setFechaInicioString(String fechaInicioString) {
        this.fechaInicioString = fechaInicioString;
    }

    public String getFechaFinString() {
        return fechaFinString;
    }

    public void setFechaFinString(String fechaFinString) {
        this.fechaFinString = fechaFinString;
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
        if (!(object instanceof Encuesta)) {
            return false;
        }
        Encuesta other = (Encuesta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferretotalserver.entity.Encuesta[ id=" + id + " ]";
    }
        
}

