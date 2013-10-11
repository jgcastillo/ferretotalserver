/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ferretotal.serialservice.entity.Tienda;
import com.ferretotal.serialservice.entity.Distribucion;
import com.ferretotal.serialservice.entity.Llamada;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class LlamadaJpaController implements Serializable {

    public LlamadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Llamada llamada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda tiendaId = llamada.getTiendaId();
            if (tiendaId != null) {
                tiendaId = em.getReference(tiendaId.getClass(), tiendaId.getId());
                llamada.setTiendaId(tiendaId);
            }
            Distribucion distribucionId = llamada.getDistribucionId();
            if (distribucionId != null) {
                distribucionId = em.getReference(distribucionId.getClass(), distribucionId.getId());
                llamada.setDistribucionId(distribucionId);
            }
            em.persist(llamada);
            if (tiendaId != null) {
                tiendaId.getLlamadaList().add(llamada);
                tiendaId = em.merge(tiendaId);
            }
            if (distribucionId != null) {
                distribucionId.getLlamadaList().add(llamada);
                distribucionId = em.merge(distribucionId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Llamada llamada) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Llamada persistentLlamada = em.find(Llamada.class, llamada.getId());
            Tienda tiendaIdOld = persistentLlamada.getTiendaId();
            Tienda tiendaIdNew = llamada.getTiendaId();
            Distribucion distribucionIdOld = persistentLlamada.getDistribucionId();
            Distribucion distribucionIdNew = llamada.getDistribucionId();
            if (tiendaIdNew != null) {
                tiendaIdNew = em.getReference(tiendaIdNew.getClass(), tiendaIdNew.getId());
                llamada.setTiendaId(tiendaIdNew);
            }
            if (distribucionIdNew != null) {
                distribucionIdNew = em.getReference(distribucionIdNew.getClass(), distribucionIdNew.getId());
                llamada.setDistribucionId(distribucionIdNew);
            }
            llamada = em.merge(llamada);
            if (tiendaIdOld != null && !tiendaIdOld.equals(tiendaIdNew)) {
                tiendaIdOld.getLlamadaList().remove(llamada);
                tiendaIdOld = em.merge(tiendaIdOld);
            }
            if (tiendaIdNew != null && !tiendaIdNew.equals(tiendaIdOld)) {
                tiendaIdNew.getLlamadaList().add(llamada);
                tiendaIdNew = em.merge(tiendaIdNew);
            }
            if (distribucionIdOld != null && !distribucionIdOld.equals(distribucionIdNew)) {
                distribucionIdOld.getLlamadaList().remove(llamada);
                distribucionIdOld = em.merge(distribucionIdOld);
            }
            if (distribucionIdNew != null && !distribucionIdNew.equals(distribucionIdOld)) {
                distribucionIdNew.getLlamadaList().add(llamada);
                distribucionIdNew = em.merge(distribucionIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = llamada.getId();
                if (findLlamada(id) == null) {
                    throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.");
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
            Llamada llamada;
            try {
                llamada = em.getReference(Llamada.class, id);
                llamada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The llamada with id " + id + " no longer exists.", enfe);
            }
            Tienda tiendaId = llamada.getTiendaId();
            if (tiendaId != null) {
                tiendaId.getLlamadaList().remove(llamada);
                tiendaId = em.merge(tiendaId);
            }
            Distribucion distribucionId = llamada.getDistribucionId();
            if (distribucionId != null) {
                distribucionId.getLlamadaList().remove(llamada);
                distribucionId = em.merge(distribucionId);
            }
            em.remove(llamada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Llamada> findLlamadaEntities() {
        return findLlamadaEntities(true, -1, -1);
    }

    public List<Llamada> findLlamadaEntities(int maxResults, int firstResult) {
        return findLlamadaEntities(false, maxResults, firstResult);
    }

    private List<Llamada> findLlamadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Llamada.class));
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

    public Llamada findLlamada(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Llamada.class, id);
        } finally {
            em.close();
        }
    }

    public int getLlamadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Llamada> rt = cq.from(Llamada.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
