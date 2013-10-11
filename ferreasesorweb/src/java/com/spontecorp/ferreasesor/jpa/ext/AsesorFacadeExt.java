/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.DistribucionFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sponte03
 */

@Stateless
public class AsesorFacadeExt {
    
    @EJB
    private AsesorFacade ejbFacade;
    @EJB
    private DistribucionFacade ejbFacadeDistribucion;
    
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("FerreAsesorWebPU");
    private EntityManager em = emf.createEntityManager();
    private static final Logger logger = LoggerFactory.getLogger(AsesorFacadeExt.class);
    
    /**
     * Selecciono el Asesor actual asignado al Turno Feriado
     * @return 
     */
    public Asesor findAsesorFeriado() {
        Asesor asesor = null;
        try {
            List<Distribucion> distList = findDistribucionFeriada();
            for(Distribucion dist : distList){
                asesor = ejbFacade.find(dist.getAsesorId());
                break;
            }
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return asesor;
    }
    
    /**
     * Habilito o Inhabilito la Distribución del Asesor, según sea el caso
     * @param asesor
     * @param status
     * @return 
     */
    public boolean changeAsesorFeriado(Asesor asesor, int status){
        int oldStatus;
        boolean result = false;
        if(status == JpaUtilities.HABILITADO){
            oldStatus = JpaUtilities.INHABILITADO;
        } else {
            oldStatus = JpaUtilities.HABILITADO;
        }
        try {
            List<Distribucion> listDist = findDistribucionFeriada(asesor, oldStatus);
            for (Distribucion dist : listDist) {
                dist.setStatus(status);
                ejbFacadeDistribucion.edit(dist);
            }
            result = true;
        }catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        } 
        return result;
    }
    
    /**
     * Obtiene la lista de Distribución Feriada
     * @return 
     */
    public List<Distribucion> findDistribucionFeriada() {
        String q = "SELECT d FROM Distribucion d, Turno t "
                + "WHERE d.turnoId = t.id AND t.feriado = :feriado AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("feriado", JpaUtilities.FERIADO);
            query.setParameter("status", JpaUtilities.HABILITADO);
            dist = query.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        } 
        return dist;
    }
    
    /**
     * Obtiene la lista de Distribución Feriada
     * @param asesor
     * @param status
     * @return 
     */
    public List<Distribucion> findDistribucionFeriada(Asesor asesor, int status) {
        String q = "SELECT d FROM Distribucion d, Asesor a, Turno t "
                + "WHERE d.asesorId = :asesor AND d.turnoId = t.id "
                + "AND t.feriado = :feriado AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("feriado", JpaUtilities.FERIADO);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        } 
        return dist;
    }
    
    /**
     * Listado de Asesores Habilitados
     * @return 
     */
    public List<Asesor> findAsesorEnabled(){
        List<Asesor> lista = null;
        try {
            String q = "SELECT a FROM Asesor a WHERE a.status = 1";
            Query query = em.createQuery(q);
            lista = query.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        } 
        return lista;
  
    }
    
}
