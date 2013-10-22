/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.Pregunta;
import com.spontecorp.ferretotalserver.entity.RespuestaObtenida;
import com.spontecorp.ferretotalserver.entity.Tienda;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author sponte03
 */
@Stateless
public class RespuestaObtenidaFacade extends AbstractFacade<RespuestaObtenida> {

    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RespuestaObtenidaFacade() {
        super(RespuestaObtenida.class);
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
    
    public List<RespuestaObtenida> findRespuestaObtenida(Encuesta encuesta, Tienda tienda) {
        String query = "SELECT ro from RespuestaObtenida ro WHERE ro.encuestaId = :encuesta AND ro.tiendaId = :tienda";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("encuesta", encuesta);
        q.setParameter("tienda", tienda);
        return q.getResultList();
    }

    
}
