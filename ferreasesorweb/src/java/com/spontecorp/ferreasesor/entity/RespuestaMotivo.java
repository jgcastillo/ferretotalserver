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
 * @author sponte03
 */
@Entity
@Table(name = "respuesta_motivo")
@NamedQueries({
    @NamedQuery(name = "RespuestaMotivo.findAll", query = "SELECT r FROM RespuestaMotivo r"),
    @NamedQuery(name = "RespuestaMotivo.findById", query = "SELECT r FROM RespuestaMotivo r WHERE r.id = :id"),
    @NamedQuery(name = "RespuestaMotivo.findByFecha", query = "SELECT r FROM RespuestaMotivo r WHERE r.fecha = :fecha")})
public class RespuestaMotivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuarioId;
    @JoinColumn(name = "motivo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Motivo motivoId;

    public RespuestaMotivo() {
    }

    public RespuestaMotivo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Motivo getMotivoId() {
        return motivoId;
    }

    public void setMotivoId(Motivo motivoId) {
        this.motivoId = motivoId;
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
        if (!(object instanceof RespuestaMotivo)) {
            return false;
        }
        RespuestaMotivo other = (RespuestaMotivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.RespuestaMotivo[ id=" + id + " ]";
    }
    
}
