/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.entity;

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
 * @author jgcastillo
 */
@Entity
@Table(name = "boton")
@NamedQueries({
    @NamedQuery(name = "Boton.findAll", query = "SELECT b FROM Boton b"),
    @NamedQuery(name = "Boton.findById", query = "SELECT b FROM Boton b WHERE b.id = :id"),
    @NamedQuery(name = "Boton.findByAddress", query = "SELECT b FROM Boton b WHERE b.address = :address"),
    @NamedQuery(name = "Boton.findByDisplay", query = "SELECT b FROM Boton b WHERE b.display = :display"),
    @NamedQuery(name = "Boton.findByUbicacion", query = "SELECT b FROM Boton b WHERE b.ubicacion = :ubicacion"),
    @NamedQuery(name = "Boton.findByStatus", query = "SELECT b FROM Boton b WHERE b.status = :status")})
public class Boton implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "address")
    private String address;
    @Basic(optional = false)
    @Column(name = "display")
    private String display;
    @Basic(optional = false)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tienda tiendaId;

    public Boton() {
    }

    public Boton(Integer id) {
        this.id = id;
    }

    public Boton(Integer id, String address, String display, String ubicacion, int status) {
        this.id = id;
        this.address = address;
        this.display = display;
        this.ubicacion = ubicacion;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        if (!(object instanceof Boton)) {
            return false;
        }
        Boton other = (Boton) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ferretotal.serialservice.entity.Boton[ id=" + id + " ]";
    }
    
}
