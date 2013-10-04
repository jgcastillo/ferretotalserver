/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Tienda;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author sponte03
 */
@Stateless
public class TiendaFacade extends AbstractFacade<Tienda> {
    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TiendaFacade() {
        super(Tienda.class);
    }
    
    /**
     * Busco una tienda por la Sucursal
     * @param idSucursal
     * @return 
     */
    public Tienda findTiendaServer(int idSucursal) {
        Tienda result = null;
        try {
            String query = "SELECT t FROM Tienda t WHERE t.sucursal = :idSucursal";
            Query q = em.createQuery(query);
            q.setParameter("idSucursal", idSucursal);
            result = (Tienda) q.getSingleResult();

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return result;
    }
    
}
