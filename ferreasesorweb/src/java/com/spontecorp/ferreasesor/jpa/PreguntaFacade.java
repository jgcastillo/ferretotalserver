/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
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
public class PreguntaFacade extends AbstractFacade<Pregunta> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PreguntaFacade() {
        super(Pregunta.class);
    }
    
    public List<Pregunta> findAll(Encuesta encuesta){
        String q = "SELECT p FROM Pregunta p WHERE p.encuestaId = :encuesta";
        Query query = getEntityManager().createQuery(q);
        query.setParameter("encuesta", encuesta);
        return query.getResultList();
    }
    
}
