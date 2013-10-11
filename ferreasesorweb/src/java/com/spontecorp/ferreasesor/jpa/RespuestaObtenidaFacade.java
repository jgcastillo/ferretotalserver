/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
@Stateless
public class RespuestaObtenidaFacade extends AbstractFacade<RespuestaObtenida> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public RespuestaObtenidaFacade() {
        super(RespuestaObtenida.class);
    }
    
    public List<RespuestaObtenida> findRespuestaObtenida(Encuesta encuesta) {
        String query = "SELECT rc from RespuestaObtenida rc WHERE rc.encuestaId = :encuesta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("encuesta", encuesta);
        return q.getResultList();
    }
    
    public int findRespuestaObtenida(Pregunta pregunta) {
        String query = "SELECT ro from RespuestaObtenida ro WHERE ro.preguntaId = :pregunta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        return q.getResultList().size();
    }
    
    public int findCantidadRespuestaObtenida(Pregunta pregunta, String respuesta) {
        String query = "SELECT ro from RespuestaObtenida ro WHERE ro.preguntaId = :pregunta AND ro.respuesta =:respuesta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        q.setParameter("respuesta", respuesta);
        return q.getResultList().size();
    }
    
    public List<RespuestaObtenida> findRespuestaObtenidaList(Pregunta pregunta) {
        String query = "SELECT ro from RespuestaObtenida ro WHERE ro.preguntaId = :pregunta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        return q.getResultList();
    }
}
