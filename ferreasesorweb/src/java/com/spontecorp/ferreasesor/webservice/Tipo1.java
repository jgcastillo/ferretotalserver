/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.webservice;

import java.util.ArrayList;
import java.util.List;

public class Tipo1 {

    private String pregunta;
    private List<String> respuestas = new ArrayList<>();

    public Tipo1() {
        
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }
}