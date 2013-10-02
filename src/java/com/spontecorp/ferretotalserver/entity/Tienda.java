/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sponte03
 */
@Entity
@Table(name = "tienda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tienda.findAll", query = "SELECT t FROM Tienda t"),
    @NamedQuery(name = "Tienda.findById", query = "SELECT t FROM Tienda t WHERE t.id = :id"),
    @NamedQuery(name = "Tienda.findBySucursal", query = "SELECT t FROM Tienda t WHERE t.sucursal = :sucursal"),
    @NamedQuery(name = "Tienda.findByNombre", query = "SELECT t FROM Tienda t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Tienda.findByTelefono", query = "SELECT t FROM Tienda t WHERE t.telefono = :telefono")})
public class Tienda implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<RespuestaObtenida> respuestaObtenidaList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sucursal")
    private int sucursal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Size(max = 65535)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 45)
    @Column(name = "telefono")
    private String telefono;
    @Lob
    @Size(max = 65535)
    @Column(name = "url")
    private String url;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Llamada> llamadaList;

    public Tienda() {
    }

    public Tienda(Integer id) {
        this.id = id;
    }

    public Tienda(Integer id, int sucursal, String nombre) {
        this.id = id;
        this.sucursal = sucursal;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @XmlTransient
    public List<Llamada> getLlamadaList() {
        return llamadaList;
    }

    public void setLlamadaList(List<Llamada> llamadaList) {
        this.llamadaList = llamadaList;
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
        if (!(object instanceof Tienda)) {
            return false;
        }
        Tienda other = (Tienda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferretotalserver.entity.Tienda[ id=" + id + " ]";
    }

    @XmlTransient
    public List<RespuestaObtenida> getRespuestaObtenidaList() {
        return respuestaObtenidaList;
    }

    public void setRespuestaObtenidaList(List<RespuestaObtenida> respuestaObtenidaList) {
        this.respuestaObtenidaList = respuestaObtenidaList;
    }
    
}
