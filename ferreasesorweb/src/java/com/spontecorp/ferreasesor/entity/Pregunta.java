/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.entity;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "pregunta")
@NamedQueries({
    @NamedQuery(name = "Pregunta.findAll", query = "SELECT p FROM Pregunta p"),
    @NamedQuery(name = "Pregunta.findById", query = "SELECT p FROM Pregunta p WHERE p.id = :id"),
    @NamedQuery(name = "Pregunta.findByTipo", query = "SELECT p FROM Pregunta p WHERE p.tipo = :tipo")})
public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "pregunta")
    @Expose
    private String pregunta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipo")
    @Expose
    private int tipo;
    @JoinColumn(name = "encuesta_id", referencedColumnName = "id")
    @ManyToOne
    private Encuesta encuestaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntaId")
    private List<RespuestaObtenida> respuestaObtenidaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "preguntaId")
    @Expose
    private List<RespuestaConf> respuestaConfList;
    @Transient
    private int totalRespuestas;
    @Transient
    private List<Integer> listRespObtenidas = new ArrayList<>();
    @Transient
    private List<Numericas> listaNumericas;
    @Transient
    private CartesianChartModel categoryModel;
    @Transient
    private PieChartModel categoryModelPie;

    public Pregunta() {
    }

    public Pregunta(Integer id) {
        this.id = id;
    }

    public Pregunta(Integer id, String pregunta, int tipo) {
        this.id = id;
        this.pregunta = pregunta;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Encuesta getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(Encuesta encuestaId) {
        this.encuestaId = encuestaId;
    }

    public List<RespuestaObtenida> getRespuestaObtenidaList() {
        return respuestaObtenidaList;
    }

    public void setRespuestaObtenidaList(List<RespuestaObtenida> respuestaObtenidaList) {
        this.respuestaObtenidaList = respuestaObtenidaList;
    }

    public List<RespuestaConf> getRespuestaConfList() {
        return respuestaConfList;
    }

    public void setRespuestaConfList(List<RespuestaConf> respuestaConfList) {
        this.respuestaConfList = respuestaConfList;
    }

    public int getTotalRespuestas() {
        return totalRespuestas;
    }

    public void setTotalRespuestas(int totalRespuestas) {
        this.totalRespuestas = totalRespuestas;
    }

    public List<Integer> getListRespObtenidas() {
        return listRespObtenidas;
    }

    public void setListRespObtenidas(List<Integer> listRespObtenidas) {
        this.listRespObtenidas = listRespObtenidas;
    }

    public List<Numericas> getListaNumericas() {
        return listaNumericas;
    }

    public void setListaNumericas(List<Numericas> listaNumericas) {
        this.listaNumericas = listaNumericas;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public PieChartModel getCategoryModelPie() {
        return categoryModelPie;
    }

    public void setCategoryModelPie(PieChartModel categoryModelPie) {
        this.categoryModelPie = categoryModelPie;
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
        if (!(object instanceof Pregunta)) {
            return false;
        }
        Pregunta other = (Pregunta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.spontecorp.ferreasesor.entity.Pregunta[ id=" + id + " ]";
    }
    
}
