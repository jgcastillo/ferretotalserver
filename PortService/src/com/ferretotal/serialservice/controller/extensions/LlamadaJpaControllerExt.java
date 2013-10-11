package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.LlamadaJpaController;
import com.ferretotal.serialservice.entity.Asesor;
import com.ferretotal.serialservice.entity.Boton;
import com.ferretotal.serialservice.entity.Llamada;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class LlamadaJpaControllerExt extends LlamadaJpaController{

    private static final Logger logger = LoggerFactory.getLogger(LlamadaJpaControllerExt.class);
    
    public LlamadaJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    /**
     * Retorna las llamadas por fecha
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public List<Object[]> findLlamadas(String queryString, Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        List<Object[]> result = null;
        try {
            Query query = em.createQuery(queryString);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            result = query.getResultList();
        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return result;
    }
    
    public List<Llamada> findLlamadasTiempo(String queryString, Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        List<Llamada> result = null;
        try {
            Query query = em.createQuery(queryString);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            result = query.getResultList();
        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return result;
    }
    
    public int getLlamadaCount(Date fechaInicial, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT COUNT(ll) FROM Llamada ll "
                    + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin ";
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicial);
            q.setParameter("fechaFin", fechaFin);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getDiasEntreFechasCount(Date fechaInicial, Date fechaFin) {
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT COUNT(DISTINCT ll.fechaClose) FROM Llamada ll "
                    + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin ";
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicial);
            q.setParameter("fechaFin", fechaFin);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<Llamada> getLLamadaFromBotonAndAsesor(Boton boton, Asesor asesor){
        EntityManager em = getEntityManager();
        List<Llamada> llamadaList;
        try {
            String query = "SELECT ll FROM Llamada ll "
                            + "WHERE ll.botonId = :boton AND ll.asesorId = :asesor";
            Query q = em.createQuery(query);
            q.setParameter("boton", boton);
            q.setParameter("asesor", asesor);
            llamadaList = q.getResultList();
            return llamadaList;
        } finally {
            em.close();
        }
    }
    
    /**
     * Selecciono la última Llamada abierta
     * @param boton
     * @return 
     */
    public Llamada findLlamadaAbierta(Boton boton){
        EntityManager em = getEntityManager();
        Llamada llamada = null;
        try {
            String q = "SELECT ll From Llamada ll INNER JOIN ll.distribucionId d "
                        + "WHERE (ll.accion = :accion OR ll.accion = :accion1) AND d.botonId = :boton"
                        + " ORDER BY ll.id DESC";
            Query query = em.createQuery(q);
            query.setParameter("accion", 2);
            query.setParameter("accion1", 1);
            query.setParameter("boton", boton.getId());
            llamada = (Llamada)query.getResultList().get(0);
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            em.close();
            return llamada;
        }
    }
}
