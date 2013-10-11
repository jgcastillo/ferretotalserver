/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import com.ferretotal.serialservice.entity.Boton;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ferretotal.serialservice.entity.Tienda;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class BotonJpaController implements Serializable {

    public BotonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Boton boton) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda tiendaId = boton.getTiendaId();
            if (tiendaId != null) {
                tiendaId = em.getReference(tiendaId.getClass(), tiendaId.getId());
                boton.setTiendaId(tiendaId);
            }
            em.persist(boton);
            if (tiendaId != null) {
                tiendaId.getBotonList().add(boton);
                tiendaId = em.merge(tiendaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Boton boton) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Boton persistentBoton = em.find(Boton.class, boton.getId());
            Tienda tiendaIdOld = persistentBoton.getTiendaId();
            Tienda tiendaIdNew = boton.getTiendaId();
            if (tiendaIdNew != null) {
                tiendaIdNew = em.getReference(tiendaIdNew.getClass(), tiendaIdNew.getId());
                boton.setTiendaId(tiendaIdNew);
            }
            boton = em.merge(boton);
            if (tiendaIdOld != null && !tiendaIdOld.equals(tiendaIdNew)) {
                tiendaIdOld.getBotonList().remove(boton);
                tiendaIdOld = em.merge(tiendaIdOld);
            }
            if (tiendaIdNew != null && !tiendaIdNew.equals(tiendaIdOld)) {
                tiendaIdNew.getBotonList().add(boton);
                tiendaIdNew = em.merge(tiendaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boton.getId();
                if (findBoton(id) == null) {
                    throw new NonexistentEntityException("The boton with id " + id + " no longer exists.");
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
            Boton boton;
            try {
                boton = em.getReference(Boton.class, id);
                boton.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boton with id " + id + " no longer exists.", enfe);
            }
            Tienda tiendaId = boton.getTiendaId();
            if (tiendaId != null) {
                tiendaId.getBotonList().remove(boton);
                tiendaId = em.merge(tiendaId);
            }
            em.remove(boton);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Boton> findBotonEntities() {
        return findBotonEntities(true, -1, -1);
    }

    public List<Boton> findBotonEntities(int maxResults, int firstResult) {
        return findBotonEntities(false, maxResults, firstResult);
    }

    private List<Boton> findBotonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Boton.class));
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

    public Boton findBoton(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Boton.class, id);
        } finally {
            em.close();
        }
    }

    public int getBotonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Boton> rt = cq.from(Boton.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
