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
@Table(name = "feriado")
@NamedQueries({
    @NamedQuery(name = "Feriado.findAll", query = "SELECT f FROM Feriado f"),
    @NamedQuery(name = "Feriado.findById", query = "SELECT f FROM Feriado f WHERE f.id = :id"),
    @NamedQuery(name = "Feriado.findByDia", query = "SELECT f FROM Feriado f WHERE f.dia = :dia"),
    @NamedQuery(name = "Feriado.findByMes", query = "SELECT f FROM Feriado f WHERE f.mes = :mes"),
    @NamedQuery(name = "Feriado.findByYear", query = "SELECT f FROM Feriado f WHERE f.year = :year"),
    @NamedQuery(name = "Feriado.findByDescripcion", query = "SELECT f FROM Feriado f WHERE f.descripcion = :descripcion")})
public class Feriado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "dia")
    private int dia;
    @Basic(optional = false)
    @Column(name = "mes")
    private int mes;
    @Column(name = "year")
    private Integer year;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "tienda_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Tienda tiendaId;

    public Feriado() {
    }

    public Feriado(Integer id) {
        this.id = id;
    }

    public Feriado(Integer id, int dia, int mes, String descripcion) {
        this.id = id;
        this.dia = dia;
        this.mes = mes;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Feriado)) {
            return false;
        }
        Feriado other = (Feriado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.ferretotal.serialservice.entity.Feriado[ id=" + id + " ]";
    }
    
}
