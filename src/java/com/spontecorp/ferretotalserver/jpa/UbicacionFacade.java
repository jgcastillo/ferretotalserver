/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.spontecorp.ferretotalserver.entity.Ubicacion;
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
public class UbicacionFacade extends AbstractFacade<Ubicacion> {
    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UbicacionFacade() {
        super(Ubicacion.class);
    }
    
    /**
     * Lista de Ubicaciones con Status Activo
     * @param idStatus
     * @return 
     */
    public List<Ubicacion> findUbicacionList(int idStatus) {
        List<Ubicacion> list = null;
        try {
            String query = "SELECT u FROM Ubicacion u WHERE u.status = :idStatus";
            Query q = em.createQuery(query);
            q.setParameter("idStatus", idStatus);
            list =  q.getResultList();

        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return list;
    }
    
}
