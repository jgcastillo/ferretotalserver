package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.EncuestaAux;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaAuxFacade;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaConfFacade;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Casper
 */
@ManagedBean(name = "encuestaAuxBean")
@SessionScoped
public class EncuestaAuxController implements Serializable {

    private Tienda tienda;
    //private Encuesta encuestaCurrent;
    private EncuestaAux encuestaCurrent;
    private Encuesta encuesta;
    private List<Pregunta> preguntas;
    private List<RespuestaConf> opciones;
    private Map<Pregunta,List<RespuestaConf>> mapOpciones;
    @EJB
    private EncuestaAuxFacade encuestaAuxFacade;
    @EJB
    private EncuestaFacade encuestaFacade;
    @EJB
    private PreguntaFacade preguntaFacade;
    @EJB
    private RespuestaConfFacade opcionFacade;
    private static final int ACTIVO = 1;
    private static final int INACTIVO = 0;

    public EncuestaAuxController() {
    }

    private EncuestaAuxFacade getEncuestaAuxFacade() {
        return encuestaAuxFacade;
    }

    private EncuestaFacade getEncuestaFacade() {
        return encuestaFacade;
    }

    private PreguntaFacade getPreguntaFacade() {
        return preguntaFacade;
    }

    private RespuestaConfFacade getOpcionFacade() {
        return opcionFacade;
    }

    public EncuestaAux getEncuestaCurrent() {
        if (encuestaCurrent == null) {
            encuestaCurrent = new EncuestaAux();
            encuestaCurrent.setEncuesta(findEncuestaActive());
            encuestaCurrent.setPreguntas(getPreguntas());
            encuestaCurrent.setMapOpciones(getMapOpciones());
        }
        return encuestaCurrent;
    }

    private Encuesta findEncuestaActive() {
        encuesta = getEncuestaFacade().findByStatus(ACTIVO);
        return encuesta;
    }

    private List<Pregunta> getPreguntas() {
        if (preguntas == null) {
            preguntas = getPreguntaFacade().findAll(encuesta);
        }
        return preguntas;
    }

    private Map<Pregunta, List<RespuestaConf>> getMapOpciones() {
        if (mapOpciones == null) {
            mapOpciones = new HashMap<>();
            for (Pregunta pregunta : preguntas) {
                opciones = getOpcionFacade().findRespuestaConf(pregunta);
                mapOpciones.put(pregunta, opciones);
            }
        }
        return mapOpciones;
    }
//    private Pregunta preguntaCurrent;
//    private RespuestaConf opcionCurrent;
//    private RespuestaObtenida respuestaCurrent;
//    private List<Encuesta> encuestas;
//    private List<Pregunta> preguntas;
//    private List<RespuestaConf> opciones;
//    private List<RespuestaObtenida> respuestas;
//    private String preguntaSeleccionItem;
//    private String prompt;
//    private List<String> preguntaSeleccionItems;
//    @EJB
//    private TiendaFacade tiendaFacade;
//    @EJB
//    private EncuestaFacade encuestaFacade;
//    @EJB
//    private PreguntaFacade preguntaFacade;
//    @EJB
//    private RespuestaConfFacade opcionFacade;
//    @EJB
//    private RespuestaObtenidaFacade respuestaFacade;
//    private static final int ACTIVO = 1;
//    private static final int INACTIVO = 0;
//
//    public EncuestaAuxController() {
//    }
//
//    public EncuestaAux getEncuestaSelected() {
//        if (encuestaCurrent == null) {
//            encuestaCurrent = new EncuestaAux();
//        }
//        return encuestaCurrent;
//    }
//
//    public Pregunta getPreguntaSelected() {
//        if (preguntaCurrent == null) {
//            preguntaCurrent = new Pregunta();
//        }
//        return preguntaCurrent;
//    }
//
//    public RespuestaConf getOpcionSelected() {
//        if (opcionCurrent == null) {
//            opcionCurrent = new RespuestaConf();
//        }
//        return opcionCurrent;
//    }
//
//    public RespuestaObtenida getRespuestaSelected() {
//        if (respuestaCurrent == null) {
//            respuestaCurrent = new RespuestaObtenida();
//        }
//        return respuestaCurrent;
//    }
//
//    private TiendaFacade getTiendaFacade() {
//        return tiendaFacade;
//    }
//
//    private EncuestaFacade getEncuestaFacade() {
//        return encuestaFacade;
//    }
//
//    private PreguntaFacade getPreguntaFacade() {
//        return preguntaFacade;
//    }
//
//    private RespuestaConfFacade getOpcionFacade() {
//        return opcionFacade;
//    }
//
//    private RespuestaObtenidaFacade getRespuestaFacade() {
//        return respuestaFacade;
//    }
//
//    public String getPreguntaSeleccionItem() {
//        return preguntaSeleccionItem;
//    }
//
//    public void setPreguntaSeleccionItem(String preguntaSeleccionItem) {
//        this.preguntaSeleccionItem = preguntaSeleccionItem;
//    }
//
//    public String getPrompt() {
//        return prompt;
//    }
//
//    public void setPrompt(String prompt) {
//        this.prompt = prompt;
//    }
//
//    public List<Encuesta> getEncuestas() {
//        if (encuestas == null) {
//            encuestas = getEncuestaFacade().findAll();
//        }
//        return encuestas;
//    }
//    public List<Pregunta> getPreguntas() {
//        if (preguntas == null) {
//            preguntas = getPreguntaFacade().findAll(encuestaCurrent);
//        }
//        return preguntas;
//    }
//
//    public List<RespuestaConf> getOpciones() {
//        if (opciones == null) {
//            opciones = getOpcionFacade().findAll();
//        }
//        return opciones;
//    }
//
//    public List<RespuestaConf> getOpciones(Pregunta pregunta) {
//        if (opciones == null) {
//            opciones = getOpcionFacade().findRespuestaConf(pregunta);
//        }
//        return opciones;
//    }
//
//    public List<RespuestaObtenida> getRespuestas() {
//        if (respuestas == null) {
//            respuestas = getRespuestaFacade().findAll();
//        }
//        return respuestas;
//    }
//
//    public List<RespuestaObtenida> getRespuestas(Encuesta encuesta) {
//        if (respuestas == null) {
//            respuestas = getRespuestaFacade().findRespuestaObtenida(encuesta);
//        }
//        return respuestas;
//    }
//
//    public List<String> getpreguntaSeleccionItems() {
//        return preguntaSeleccionItems;
//    }
//
//    public void setpreguntaSeleccionItems(List<String> preguntaSeleccionItems) {
//        this.preguntaSeleccionItems = preguntaSeleccionItems;
//    }
//
//    public String prepareCreateEncuesta() {
//        encuestaCurrent = new Encuesta();
//        return "createSurvey?faces-redirect=true";
//    }
//
//    public String createEncuesta() {
//        tienda = getTiendaFacade().find(1);
//        encuestaCurrent.setTiendaId(tienda);
//        encuestaCurrent.setStatus(INACTIVO);
//        getEncuestaFacade().create(encuestaCurrent);
//        return prepareCancelEncuesta();
//    }
//
//    private void recreateEncuestaModel() {
//        encuestas = null;
//    }
//
//    private String prepareCancelEncuesta() {
//        encuestaCurrent = null;
//        recreateEncuestaModel();
//        return "encuestaMain?faces-redirect = true";
//    }
//    
//    public Encuesta getEncuestaActiva() {
//        encuestaCurrent = getEncuestaFacade().findByStatus(ACTIVO);
//        return encuestaCurrent;
//    }
//
//    public String prepareCreatePregunta() {
//        recreatePreguntaModel();
//        preguntaCurrent = new Pregunta();
//        return "createQuestions?faces-redirect=true";
//    }
//
//    public String createPregunta() {
//        preguntaCurrent.setEncuestaId(encuestaCurrent);
//        getPreguntaFacade().create(preguntaCurrent);
//        switch (preguntaCurrent.getTipo()) {
//            case 1:
//            case 2:
//            case 4: {
//                opcionCurrent = new RespuestaConf();
//                opcionCurrent.setPreguntaId(preguntaCurrent);
//                opcionCurrent.setPrompt(prompt);
//                break;
//            }
//            case 3: {
//                for (String opcion : preguntaSeleccionItems) {
//                    opcionCurrent = new RespuestaConf();
//                    opcionCurrent.setPreguntaId(preguntaCurrent);
//                    opcionCurrent.setOpcion(opcion);
//                    opcionFacade.create(opcionCurrent);
//                }
//                break;
//            }
//        }
//        getPreguntaFacade().create(preguntaCurrent);
//        return prepareCancelPregunta();
//    }
//
//    private void recreatePreguntaModel() {
//        preguntas = null;
//    }
//
//    private String prepareCancelPregunta() {
//        preguntaCurrent = null;
//        recreatePreguntaModel();
//        return "createQuestions?faces-redirect=true";
//    }
//    
//    public void addItemToSeleccion(ActionEvent event) {
//        if (preguntaSeleccionItems.add(preguntaSeleccionItem)) {
//            JsfUtil.addSuccessMessage("Opción agregada");
//        } else {
//            JsfUtil.addErrorMessage("Opción no se pudo agregar");
//        }
//    }
//
//    public void removeItemFromSeleccion() {
//        if (preguntaSeleccionItems.remove(preguntaSeleccionItem)) {
//            JsfUtil.addSuccessMessage("Opción eliminar");
//        } else {
//            JsfUtil.addErrorMessage("Opción no se pudo eliminar");
//        }
//    }
}
