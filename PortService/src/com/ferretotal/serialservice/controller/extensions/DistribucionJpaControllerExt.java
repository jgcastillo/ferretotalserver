package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.DistribucionJpaController;
import com.ferretotal.serialservice.utilidades.GeneralUtilities;
import com.ferretotal.serialservice.utilidades.JpaUtilities;
import com.ferretotal.serialservice.entity.Asesor;
import com.ferretotal.serialservice.entity.Boton;
import com.ferretotal.serialservice.entity.Distribucion;
import com.ferretotal.serialservice.entity.Turno;
import java.util.ArrayList;
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
public class DistribucionJpaControllerExt extends DistribucionJpaController {

    private static Logger logger = LoggerFactory.getLogger(DistribucionJpaControllerExt.class);

    public DistribucionJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Distribucion> findDistribucionEntities(Asesor asesor) {
        EntityManager em = getEntityManager();
        String query = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor";
        List<Distribucion> lista = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("asesor", asesor.getId());
            lista = q.getResultList();
        } finally {
            em.close();
            return lista;
        }
    }

    public List<Distribucion> findDistribucionWAsesorEnabled() {
        EntityManager em = getEntityManager();
        String query = "SELECT d FROM Distribucion d, Asesor a "
                + "WHERE a.id = d.asesorId AND a.status = :status";
        List<Distribucion> lista = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("status", 1);
            lista = q.getResultList();
        } catch(Exception e){
            logger.error("Error ejecutando el query: " + e.getMessage());
        }finally {
            em.close();
            return lista;
        }
    }

    public List<Boton> findBotonesXTurno(Asesor asesor, Turno turno) {
        BotonJpaControllerExt controller = new BotonJpaControllerExt(JpaUtilities.getEntityManagerFactory());
        EntityManager em = getEntityManager();
        List<Boton> lista = null;
        String q = "SELECT d.botonId FROM Distribucion d WHERE d.asesorId = :asesor AND d.turnoId = :turno";
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            List<Integer> listBotonId = query.getResultList();
            
            lista = new ArrayList<>();
            for(Integer botId : listBotonId){
                Boton boton = controller.findBoton(botId);
                lista.add(boton);
            }
            
        } finally {
            em.close();
            return lista;
        }
    }

    public Distribucion findDistribucion(Boton boton, Turno turno) {
        EntityManager em = getEntityManager();
        Distribucion dist = null;
        String q =  "SELECT d FROM Distribucion d "
                + "WHERE d.botonId = :boton AND d.turnoId = :turno "
                + "AND d.status = 1";
        try {
            Query query = em.createQuery(q);

            query.setParameter("boton", boton.getId());
            query.setParameter("turno", turno.getId());
            dist = (Distribucion) query.getSingleResult();
            System.out.println("DistJpaContrExt 96, la distribución traida es: " + dist.getId());
        } catch (Exception e){
            logger.error("Error encontrando distribución: " + e);
        }finally {
            em.close();
            return dist;
        }
    }

    public List<Asesor> findDistribucion() {
        EntityManager em = getEntityManager();
        List<Asesor> lista = null;
        String q = "SELECT DISTINCT d.asesorId FROM Distribucion d";
        try {
            Query query = em.createQuery(q);
            lista = query.getResultList();
        } finally {
            em.close();
            return lista;
        }
    }
    
    public Distribucion findDistribucion(Asesor asesor, Turno turno, Boton boton){
        EntityManager em = getEntityManager();
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.turnoId = :turno AND d.botonId = :boton";
        Distribucion dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            query.setParameter("boton", boton.getId());
            dist = (Distribucion)query.getSingleResult();
        } finally {
            em.close();
            return dist;
        }
    }
    
    public List<Distribucion> findDistribucion(Asesor asesor, int status) {
        EntityManager em = getEntityManager();
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } finally {
            em.close();
            return dist;
        }
    }
    
    public List<Distribucion> findDistribucion(Asesor asesor, Turno turno, int status) {
        EntityManager em = getEntityManager();
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.turnoId = :turno AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } finally {
            em.close();
            return dist;
        }
    }
    
    public List<Distribucion> findDistribucionFeriada() {
        EntityManager em = getEntityManager();
        String q = "SELECT d FROM Distribucion d, Turno t "
                + "WHERE d.turnoId = t.id AND t.feriado = :feriado AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("feriado", GeneralUtilities.FERIADO);
            query.setParameter("status", GeneralUtilities.HABILITADO);
            dist = query.getResultList();
        } finally {
            em.close();
            return dist;
        }
    }
    
    public List<Distribucion> findDistribucionFeriada(Asesor asesor, int status) {
        EntityManager em = getEntityManager();
        String q = "SELECT d FROM Distribucion d, Asesor a, Turno t "
                + "WHERE d.asesorId = :asesor AND d.turnoId = t.id "
                + "AND t.feriado = :feriado AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("feriado", GeneralUtilities.FERIADO);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } finally {
            em.close();
            return dist;
        }
    }
    
    @Override
    public void edit(Distribucion dist){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(dist);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error actualizando la distribucion: " + e.getMessage());
        } finally {
            if(em != null){
                em.close();
            }
        }
        
    }
    
}
