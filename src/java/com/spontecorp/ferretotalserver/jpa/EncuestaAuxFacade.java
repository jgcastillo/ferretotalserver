package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.EncuestaAux;
import com.spontecorp.ferretotalserver.entity.Pregunta;
import com.spontecorp.ferretotalserver.entity.RespuestaConf;
import com.spontecorp.ferretotalserver.entity.RespuestaObtenida;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Casper
 */
@Stateless
public class EncuestaAuxFacade extends AbstractFacade<EncuestaAux>{
    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;
    private List<Encuesta> encuestas;
    private List<Pregunta> preguntas;
    private List<RespuestaConf> opciones;
    @EJB
    private EncuestaFacade encuestaFacade;
    @EJB
    private PreguntaFacade preguntaFacade;
    @EJB
    private RespuestaConfFacade opcionesFacade;
    @EJB
    private RespuestaObtenidaFacade respuestaFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public EncuestaAuxFacade() {
        super(EncuestaAux.class);
    }

    public List<Encuesta> findEncuestas() {
        if (encuestas == null) {
            encuestas = encuestaFacade.findAll();
        }
        return encuestas;
    }

    public List<Pregunta> findPreguntas(Encuesta encuesta) {
        if (preguntas == null) {
            preguntas = preguntaFacade.findAll(encuesta);
        }
        return preguntas;
    }

    public List<RespuestaConf> findOpciones(Pregunta pregunta) {
        if (opciones == null) {
            opciones = opcionesFacade.findRespuestaConf(pregunta);
        }
        return opciones;
    }
    
    public List<RespuestaObtenida> findRespuestas(Encuesta encuesta){
        String query = "SELECT resp FROM RespuestaObtenida resp WHERE resp.encuestaId = : encuesta";
        Query q = respuestaFacade.getEntityManager().createQuery(query);
        return q.getResultList();
    }
}
