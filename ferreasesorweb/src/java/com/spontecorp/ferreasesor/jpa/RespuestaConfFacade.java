/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
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
public class RespuestaConfFacade extends AbstractFacade<RespuestaConf> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RespuestaConfFacade() {
        super(RespuestaConf.class);
    }
    
    public List<RespuestaConf> findRespuestaConf(Pregunta pregunta) {
        String query = "SELECT rc from RespuestaConf rc WHERE rc.preguntaId = :pregunta";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("pregunta", pregunta);
        return q.getResultList();
    }
}
