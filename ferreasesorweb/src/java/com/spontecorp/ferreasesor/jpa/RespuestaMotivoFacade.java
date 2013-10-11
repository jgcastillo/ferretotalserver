/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.RespuestaMotivo;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sponte03
 */
@Stateless
public class RespuestaMotivoFacade extends AbstractFacade<RespuestaMotivo> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(RespuestaMotivoFacade.class);

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RespuestaMotivoFacade() {
        super(RespuestaMotivo.class);
    }
    
    /**
     * Lista de Respuesta de Motivos filtradas por fecha
     * @param fechaInicio
     * @param fechaFin
     * @return 
     */
    public List<RespuestaMotivo> findRespuestaMotivoList(Date fechaInicio, Date fechaFin) {
        List<RespuestaMotivo> result = null;
        String query = "SELECT rm "
                + "FROM RespuestaMotivo rm WHERE rm.fecha >= :fechaInicio AND rm.fecha <= :fechaFin"
                + " ORDER BY rm.fecha DESC";
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            result = q.getResultList();
            System.out.println("result: "+result.size());
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;
    }
    
}
