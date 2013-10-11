/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Categoria;
import com.spontecorp.ferreasesor.entity.Motivo;
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
public class MotivoFacade extends AbstractFacade<Motivo> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MotivoFacade() {
        super(Motivo.class);
    }
    
    /**
     * Listado de Motivos por Status y Categorías
     * @param status
     * @param categoria
     * @return 
     */
    public List<Motivo> findMotivo(int status, Categoria categoria) {
        String query = "SELECT m from Motivo m WHERE m.status = :status and m.categoriaId = :categoria";
        Query q = getEntityManager().createQuery(query);
        q.setParameter("status", status);
        q.setParameter("categoria", categoria);
        return q.getResultList();
    }
    
}
