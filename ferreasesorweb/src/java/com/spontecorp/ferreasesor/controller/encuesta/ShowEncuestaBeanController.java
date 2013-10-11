package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author jgcastillo
 */
@ManagedBean (name="showEncuestaBean")
@SessionScoped
public class ShowEncuestaBeanController implements Serializable{
    
    private Encuesta encuesta;
    private Pregunta pregunta;
    private List<Pregunta> preguntas = null;
    @EJB
    private EncuestaFacade encuestaFacade;
    @EJB
    private PreguntaFacade preguntaFacade;
    private String preguntaTexto;
    private String promptPreguntaTextual;
    private String respuestaTexto;
    private static final int ENCUESTA_ACTIVA = 1;

    public ShowEncuestaBeanController() {
    }

    public Encuesta getEncuesta() {
        encuesta = getEncuestaFacade().find(ENCUESTA_ACTIVA);
        return encuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public List<Pregunta> getPreguntas() {
        encuesta = getEncuesta();
        if(preguntas == null){
            preguntas = getPreguntaFacade().findAll(encuesta);
        }
        
        return preguntas;
    }

    public EncuestaFacade getEncuestaFacade() {
        return encuestaFacade;
    }

    public PreguntaFacade getPreguntaFacade() {
        return preguntaFacade;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public String getPromptPreguntaTextual() {
        return promptPreguntaTextual;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }
    
}
