/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import com.google.gson.annotations.Expose;
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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Casper
 */
@Entity
@Table(name = "tienda")
@NamedQueries({
    @NamedQuery(name = "Tienda.findAll", query = "SELECT t FROM Tienda t"),
    @NamedQuery(name = "Tienda.findById", query = "SELECT t FROM Tienda t WHERE t.id = :id"),
    @NamedQuery(name = "Tienda.findBySucursal", query = "SELECT t FROM Tienda t WHERE t.sucursal = :sucursal"),
    @NamedQuery(name = "Tienda.findByNombre", query = "SELECT t FROM Tienda t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "Tienda.findByTelefono", query = "SELECT t FROM Tienda t WHERE t.telefono = :telefono")})
public class Tienda implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Encuesta> encuestaList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Expose
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "sucursal")
    @Expose
    private int sucursal;
    @Basic(optional = false)
    @Column(name = "nombre")
    @Expose
    private String nombre;
    @Lob
    @Column(name = "direccion")
    @Expose
    private String direccion;
    @Column(name = "telefono")
    @Expose
    private String telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Asesor> asesorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Boton> botonList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Feriado> feriadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Llamada> llamadaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tiendaId")
    private List<Turno> turnoList;

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

    public List<Asesor> getAsesorList() {
        return asesorList;
    }

    public void setAsesorList(List<Asesor> asesorList) {
        this.asesorList = asesorList;
    }

    public List<Boton> getBotonList() {
        return botonList;
    }

    public void setBotonList(List<Boton> botonList) {
        this.botonList = botonList;
    }

    public List<Feriado> getFeriadoList() {
        return feriadoList;
    }

    public void setFeriadoList(List<Feriado> feriadoList) {
        this.feriadoList = feriadoList;
    }

    public List<Llamada> getLlamadaList() {
        return llamadaList;
    }

    public void setLlamadaList(List<Llamada> llamadaList) {
        this.llamadaList = llamadaList;
    }

    public List<Turno> getTurnoList() {
        return turnoList;
    }

    public void setTurnoList(List<Turno> turnoList) {
        this.turnoList = turnoList;
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
        return nombre;
    }

    @XmlTransient
    public List<Encuesta> getEncuestaList() {
        return encuestaList;
    }

    public void setEncuestaList(List<Encuesta> encuestaList) {
        this.encuestaList = encuestaList;
    }
}
