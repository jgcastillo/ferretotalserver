/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Pregunta;
import com.spontecorp.ferretotalserver.entity.RespuestaConf;
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
public class RespuestaConfFacade extends AbstractFacade<RespuestaConf> {

    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RespuestaConfFacade() {
        super(RespuestaConf.class);
    }

    /**
     * 
     * @param pregunta
     * @return 
     */
    public List<RespuestaConf> findRespuestaConf(Pregunta pregunta) {
        String query = "SELECT rc from RespuestaConf rc WHERE rc.preguntaId = :pregunta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        return q.getResultList();
    }

    /**
     *  Obtengo la Respuesta Conf de una Pregunta determinada
     * @param pregunta
     * @param opcion
     * @return 
     */
    public RespuestaConf find(Pregunta pregunta, String opcion) {
        String query = "SELECT rc FROM RespuestaConf rc WHERE rc.preguntaId = :pregunta "
                + "AND rc.opcion = :opcion";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        q.setParameter("opcion", opcion);
        return (RespuestaConf) q.getSingleResult();
    }
    
    /**
     * Obtengo la Respuesta Conf de una Pregunta determinada
     * @param pregunta
     * @return 
     */
    public RespuestaConf find(Pregunta pregunta) {
        String query = "SELECT rc FROM RespuestaConf rc WHERE rc.preguntaId = :pregunta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        return (RespuestaConf) q.getSingleResult();
    }
}
