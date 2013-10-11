package com.spontecorp.ferreasesor.webservice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultadoEncuesta implements Serializable {

    String nombreEncuesta;
    Integer idGlobal;
    List<Tipo1> preguntasT1= new ArrayList<>();;
    List<Tipo2> preguntasT2= new ArrayList<>();;
    List<Tipo3> preguntasT3= new ArrayList<>();;
    List<Tipo4> preguntasT4= new ArrayList<>();;

    public ResultadoEncuesta() {
        
    }

    public String getNombreEncuesta() {
        return nombreEncuesta;
    }

    public void setNombreEncuesta(String nombreEncuesta) {
        this.nombreEncuesta = nombreEncuesta;
    }

    public Integer getIdGlobal() {
        return idGlobal;
    }

    public void setIdGlobal(Integer idGlobal) {
        this.idGlobal = idGlobal;
    }

    public List<Tipo1> getPreguntasT1() {
        return preguntasT1;
    }

    public void setPreguntasT1(List<Tipo1> preguntasT1) {
        this.preguntasT1 = preguntasT1;
    }

    public List<Tipo2> getPreguntasT2() {
        return preguntasT2;
    }

    public void setPreguntasT2(List<Tipo2> preguntasT2) {
        this.preguntasT2 = preguntasT2;
    }

    public List<Tipo3> getPreguntasT3() {
        return preguntasT3;
    }

    public void setPreguntasT3(List<Tipo3> preguntasT3) {
        this.preguntasT3 = preguntasT3;
    }

    public List<Tipo4> getPreguntasT4() {
        return preguntasT4;
    }

    public void setPreguntasT4(List<Tipo4> preguntasT4) {
        this.preguntasT4 = preguntasT4;
    }
    
    
    

    

 


 

}
