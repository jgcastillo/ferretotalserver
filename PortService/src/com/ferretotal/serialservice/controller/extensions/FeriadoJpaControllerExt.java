package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.FeriadoJpaController;
import com.ferretotal.serialservice.entity.Feriado;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Casper
 */
public class FeriadoJpaControllerExt extends FeriadoJpaController{

    public FeriadoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Feriado findFeriado(int dia, int mes){
        String q = "SELECT f FROM Feriado f WHERE f.dia = :dia AND f.mes = :mes";
        EntityManager em = getEntityManager();
        Feriado feriado = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("dia", dia);
            query.setParameter("mes", mes);
            feriado = (Feriado) query.getSingleResult();
        } finally {
            em.close();
            return feriado;
        }
    }
    
    public Feriado findFeriado(String motivo) {
        String q = "SELECT f FROM Feriado f WHERE f.descripcion = :motivo";
        EntityManager em = getEntityManager();
        Feriado feriado = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("motivo", motivo);
            feriado = (Feriado) query.getSingleResult();
        } finally {
            em.close();
            return feriado;
        }
    }
}
