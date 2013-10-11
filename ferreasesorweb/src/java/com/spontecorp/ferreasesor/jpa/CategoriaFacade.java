/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Categoria;
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
public class CategoriaFacade extends AbstractFacade<Categoria> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaFacade() {
        super(Categoria.class);
    }
    
    /**
     * Listado de Categorías por Status
     * @param status
     * @return 
     */
    public List<Categoria> findCategoria(int status) {
        String query = "SELECT c from Categoria c WHERE c.status = :status";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("status", status);
        return q.getResultList();
    }
    
}
