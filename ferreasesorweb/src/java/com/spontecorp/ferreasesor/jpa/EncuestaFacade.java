/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Encuesta;
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
public class EncuestaFacade extends AbstractFacade<Encuesta> {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EncuestaFacade() {
        super(Encuesta.class);
    }

    public Encuesta find(int status) {
        String query = "SELECT e FROM Encuesta e WHERE e.status = :status";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("status", status);
        return (Encuesta) q.getSingleResult();
    }

    public Encuesta findAll(int status) {
        String query = "SELECT e FROM Encuesta e WHERE e.status = :status";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("status", status);
        List<Encuesta> encuesta = q.getResultList();

        if (encuesta.size() > 0) {
            return encuesta.get(0);
        } else {
            return null;
        }
    }
    
    public Encuesta findByStatus(int status){
        Query q = getEntityManager().createNamedQuery("Encuesta.findByStatus", Encuesta.class);
        q.setParameter("status", status);
        return (Encuesta) q.getSingleResult();
    }
}
