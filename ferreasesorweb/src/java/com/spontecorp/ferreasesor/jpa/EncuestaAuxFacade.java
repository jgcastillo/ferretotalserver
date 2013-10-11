package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.EncuestaAux;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Casper
 */
@Stateless
public class EncuestaAuxFacade extends AbstractFacade<EncuestaAux> {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
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
    public EntityManager getEntityManager() {
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

    public List<RespuestaObtenida> findRespuestas(Encuesta encuesta) {
        String query = "SELECT resp FROM RespuestaObtenida resp WHERE resp.encuestaId = : encuesta";
        Query q = respuestaFacade.getEntityManager().createQuery(query);
        return q.getResultList();
    }

    public Object findEncuestasByIdGlobal(int idGlobal) {
        try {
            String query = "SELECT enc FROM Encuesta enc WHERE enc.globalId = :idGlobal";
            Query q = respuestaFacade.getEntityManager().createQuery(query);
            q.setParameter("idGlobal", idGlobal);
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
