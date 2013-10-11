package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
@Stateless
public class DistribucionJpaControllerExt {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;
    @EJB
    private BotonFacade botonController;

    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Distribucion> findDistribucionEntities(Asesor asesor) {
        String query = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor";
        List<Distribucion> lista = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("asesor", asesor.getId());
            lista = q.getResultList();
        } catch (NoResultException e) {
        }
        return lista;
    }

    public List<Distribucion> findDistribucionWAsesorEnabled() {
        String query = "SELECT d FROM Distribucion d, Asesor a "
                + "WHERE a.id = d.asesorId AND a.status = :status";
        List<Distribucion> lista = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("status", 1);
            lista = q.getResultList();
        } catch (NoResultException e) {
        }
        return lista;
    }

    public List<Boton> findBotonesXTurno(Asesor asesor, Turno turno) {
        List<Boton> lista = null;
        String q = "SELECT d.botonId FROM Distribucion d WHERE d.asesorId = :asesor AND d.turnoId = :turno";
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            List<Integer> listBotonId = query.getResultList();

            lista = new ArrayList<>();
            for (Integer botId : listBotonId) {
                Boton boton = botonController.find(botId);
                lista.add(boton);
            }

        } catch (NoResultException e){
        }
        return lista;
    }

    public Distribucion findDistribucion(Boton boton, Turno turno) {
        Distribucion dist = null;
        String q = "SELECT d FROM Distribucion d "
                + "WHERE d.botonId = :boton AND d.turnoId = :turno "
                + "AND d.status = 1";
        try {
            Query query = em.createQuery(q);

            query.setParameter("boton", boton.getId());
            query.setParameter("turno", turno.getId());
            dist = (Distribucion) query.getSingleResult();
            //System.out.println("DistJpaContrExt 96, la distribución traida es: " + dist.getId());
        } catch (NoResultException e) {
        }
        return dist;
    }

    public List<Asesor> findDistribucion() {
        List<Asesor> lista = null;
        String q = "SELECT DISTINCT d.asesorId FROM Distribucion d";
        try {
            Query query = em.createQuery(q);
            lista = query.getResultList();
        } catch (NoResultException e) {
        }
        return lista;
    }

    public Distribucion findDistribucion(Asesor asesor, Turno turno, Boton boton) {
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.turnoId = :turno AND d.botonId = :boton";
        Distribucion dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            query.setParameter("boton", boton.getId());
            dist = (Distribucion) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return dist;
    }

    public List<Distribucion> findDistribucionList(Asesor asesor, Turno turno, Boton boton) {
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.turnoId = :turno AND d.botonId = :boton";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            query.setParameter("boton", boton.getId());
            dist = query.getResultList();
        } catch (NoResultException e) {
        }
        return dist;
    }
    
    public List<Distribucion> findDistribucion(Asesor asesor, int status) {
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } catch (NoResultException e) {
        }
        return dist;
    }

    public List<Distribucion> findDistribucion(Asesor asesor, Turno turno, int status) {
        String q = "SELECT d FROM Distribucion d WHERE d.asesorId = :asesor "
                + "AND d.turnoId = :turno AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("asesor", asesor.getId());
            query.setParameter("turno", turno.getId());
            query.setParameter("status", status);
            dist = query.getResultList();
        } catch (NoResultException e) {
        }
        return dist;
    }

    public List<Distribucion> findDistribucionFeriada() {
        String q = "SELECT d FROM Distribucion d, Turno t "
                + "WHERE d.turnoId = t.id AND t.feriado = :feriado AND d.status = :status";
        List<Distribucion> dist = null;
        try {
            Query query = em.createQuery(q);
            query.setParameter("feriado", JpaUtilities.FERIADO);
            query.setParameter("status", JpaUtilities.HABILITADO);
            dist = query.getResultList();
        } catch (NoResultException e) {
        }
        return dist;
    }

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
        } catch (NoResultException e) {
        }
        return dist;
    }

    public void edit(Distribucion dist) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(dist);
            em.getTransaction().commit();
        } catch (Exception e) {
        } 
    }
}
