/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import com.ferretotal.serialservice.entity.Tiempo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ferretotal.serialservice.entity.Turno;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TiempoJpaController implements Serializable {

    public TiempoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiempo tiempo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno turnoId = tiempo.getTurnoId();
            if (turnoId != null) {
                turnoId = em.getReference(turnoId.getClass(), turnoId.getId());
                tiempo.setTurnoId(turnoId);
            }
            em.persist(tiempo);
            if (turnoId != null) {
                turnoId.getTiempoList().add(tiempo);
                turnoId = em.merge(turnoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiempo tiempo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo persistentTiempo = em.find(Tiempo.class, tiempo.getId());
            Turno turnoIdOld = persistentTiempo.getTurnoId();
            Turno turnoIdNew = tiempo.getTurnoId();
            if (turnoIdNew != null) {
                turnoIdNew = em.getReference(turnoIdNew.getClass(), turnoIdNew.getId());
                tiempo.setTurnoId(turnoIdNew);
            }
            tiempo = em.merge(tiempo);
            if (turnoIdOld != null && !turnoIdOld.equals(turnoIdNew)) {
                turnoIdOld.getTiempoList().remove(tiempo);
                turnoIdOld = em.merge(turnoIdOld);
            }
            if (turnoIdNew != null && !turnoIdNew.equals(turnoIdOld)) {
                turnoIdNew.getTiempoList().add(tiempo);
                turnoIdNew = em.merge(turnoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiempo.getId();
                if (findTiempo(id) == null) {
                    throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiempo tiempo;
            try {
                tiempo = em.getReference(Tiempo.class, id);
                tiempo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiempo with id " + id + " no longer exists.", enfe);
            }
            Turno turnoId = tiempo.getTurnoId();
            if (turnoId != null) {
                turnoId.getTiempoList().remove(tiempo);
                turnoId = em.merge(turnoId);
            }
            em.remove(tiempo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiempo> findTiempoEntities() {
        return findTiempoEntities(true, -1, -1);
    }

    public List<Tiempo> findTiempoEntities(int maxResults, int firstResult) {
        return findTiempoEntities(false, maxResults, firstResult);
    }

    private List<Tiempo> findTiempoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiempo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tiempo findTiempo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiempo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiempoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiempo> rt = cq.from(Tiempo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
