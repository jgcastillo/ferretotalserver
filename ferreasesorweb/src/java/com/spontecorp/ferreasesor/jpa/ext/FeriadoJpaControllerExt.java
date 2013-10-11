package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Feriado;
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
public class FeriadoJpaControllerExt {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Feriado findFeriado(int dia, int mes){
        String q = "SELECT f FROM Feriado f WHERE f.dia = :dia AND f.mes = :mes";
        Feriado feriado = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("dia", dia);
            query.setParameter("mes", mes);
            feriado = (Feriado) query.getSingleResult();
        } catch (NoResultException e) {

        }
        return feriado;
    }
    
    public Feriado findFeriado(String motivo) {
        String q = "SELECT f FROM Feriado f WHERE f.descripcion = :motivo";
        Feriado feriado = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("motivo", motivo);
            feriado = (Feriado) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return feriado;
    }
}
