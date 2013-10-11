package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.TiempoJpaController;
import com.ferretotal.serialservice.entity.Tiempo;
import com.ferretotal.serialservice.entity.Turno;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Casper
 */
public class TiempoJpaControllerExt extends TiempoJpaController{

    public TiempoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Tiempo findTiempos(Turno turno){
        EntityManager em = getEntityManager();
        Tiempo tiempo = null;
        try{
            String query = "SELECT t FROM Tiempo t WHERE t.turnoId = :turno";
            Query q = em.createQuery(query);
            q.setParameter("turno", turno);
            tiempo = (Tiempo)q.getSingleResult();
        } finally{
            em.close();
            return tiempo;
        }
    }
}
