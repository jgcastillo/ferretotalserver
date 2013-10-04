/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import java.util.Date;
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
public class LlamadaFacade extends AbstractFacade<Llamada> {

    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LlamadaFacade() {
        super(Llamada.class);
    }

    public Llamada findLlamadasTienda(Tienda tienda) {
        List<Llamada> result = null;
        try {
            String query = "SELECT ll FROM Llamada ll WHERE ll.tiendaId = :tienda"
                    + " ORDER BY ll.id DESC";
            Query q = em.createQuery(query);
            q.setParameter("tienda", tienda);
            result = q.getResultList();

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
