/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.Pregunta;
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
public class PreguntaFacade extends AbstractFacade<Pregunta> {
    @PersistenceContext(unitName = "FerreAsesorServerPU")
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
    
    /**
     * Devuelve una Pregunta según los parámetros recibidos
     * @param preguntaLocal
     * @param tipoPregunta
     * @param encuesta
     * @return 
     */
    public Pregunta findPregunta(String preguntaLocal, int tipoPregunta, Encuesta encuesta){
        String q = "SELECT p FROM Pregunta p WHERE p.pregunta = :preguntaLocal AND "
                + "p.tipo = :tipoPregunta AND p.encuestaId = :encuesta";
        Query query = getEntityManager().createQuery(q);
        query.setParameter("preguntaLocal", preguntaLocal);
        query.setParameter("tipoPregunta", tipoPregunta);
        query.setParameter("encuesta", encuesta);
        return (Pregunta) query.getSingleResult();
    }
    
    
}
