package com.spontecorp.ferreasesor.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Casper
 */
public class EncuestaAux implements Serializable{
    
    private Encuesta encuesta;
    private Pregunta pregunta;
    private List<Pregunta> preguntas;
    private RespuestaConf opcion;
    private List<RespuestaConf> opciones;
    private Map<Pregunta, List<RespuestaConf>> mapOpciones;
    private RespuestaObtenida respuesta;
    
    public EncuestaAux(){
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public RespuestaConf getOpcion() {
        return opcion;
    }

    public List<RespuestaConf> getOpciones() {
        return opciones;
    }

    public void setOpciones(List<RespuestaConf> opciones) {
        this.opciones = opciones;
    }

    public void setOpcion(RespuestaConf opcion) {
        this.opcion = opcion;
    }

    public Map<Pregunta, List<RespuestaConf>> getMapOpciones() {
        return mapOpciones;
    }

    public void setMapOpciones(Map<Pregunta, List<RespuestaConf>> mapOpciones) {
        this.mapOpciones = mapOpciones;
    }

    public RespuestaObtenida getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaObtenida respuesta) {
        this.respuesta = respuesta;
    }
    
    
}
