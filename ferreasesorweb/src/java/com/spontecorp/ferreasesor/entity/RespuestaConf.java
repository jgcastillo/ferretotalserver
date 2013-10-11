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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author jgcastillo
 */
@Entity
@Table(name = "respuesta_conf")
@NamedQueries({
    @NamedQuery(name = "RespuestaConf.findAll", query = "SELECT r FROM RespuestaConf r"),
    @NamedQuery(name = "RespuestaConf.findById", query = "SELECT r FROM RespuestaConf r WHERE r.id = :id"),
    @NamedQuery(name = "RespuestaConf.findByOpcion", query = "SELECT r FROM RespuestaConf r WHERE r.opcion = :opcion"),
    @NamedQuery(name = "RespuestaConf.findByPrompt", query = "SELECT r FROM RespuestaConf r WHERE r.prompt = :prompt")})
public class RespuestaConf implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    @Expose
    private Integer id;
    @Size(min = 1, max = 45)
    @Column(name = "opcion")
    @Expose
    private String opcion;
    @Size(max = 45)
    @Column(name = "prompt")
    @Expose
    private String prompt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "respuestaConfId")
    @Expose
    private List<RespuestaObtenida> respuestaObtenidaList;    
    @JoinColumn(name = "pregunta_id", referencedColumnName = "id")
    @ManyToOne(optional = false)  
    private Pregunta preguntaId;
    @Transient
    private int totalOptions;
    
    public RespuestaConf() {
    }

    public RespuestaConf(Integer id) {
        this.id = id;
    }

    public RespuestaConf(Integer id, String opcion) {
        this.id = id;
        this.opcion = opcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public List<RespuestaObtenida> getRespuestaObtenidaList() {
        return respuestaObtenidaList;
    }

    public void setRespuestaObtenidaList(List<RespuestaObtenida> respuestaObtenidaList) {
        this.respuestaObtenidaList = respuestaObtenidaList;
    }

    public Pregunta getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Pregunta preguntaId) {
        this.preguntaId = preguntaId;
    }

    public int getTotalOptions() {
        return totalOptions;
    }

    public void setTotalOptions(int totalOptions) {
        this.totalOptions = totalOptions;
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
        if (!(object instanceof RespuestaConf)) {
            return false;
        }
        RespuestaConf other = (RespuestaConf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return prompt;
    }
    
}
