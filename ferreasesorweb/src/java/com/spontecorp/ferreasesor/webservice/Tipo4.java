/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.webservice;

import java.util.ArrayList;
import java.util.List;

public class Tipo4 {

    private String pregunta;
    private List<Integer> respuestas = new ArrayList<>();

    

    public Tipo4() {
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<Integer> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Integer> respuestas) {
        this.respuestas = respuestas;
    }
}