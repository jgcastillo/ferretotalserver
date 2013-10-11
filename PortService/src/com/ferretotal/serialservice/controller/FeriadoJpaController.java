/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import com.ferretotal.serialservice.entity.Feriado;
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
public class FeriadoJpaController implements Serializable {

    public FeriadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Feriado feriado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda tiendaId = feriado.getTiendaId();
            if (tiendaId != null) {
                tiendaId = em.getReference(tiendaId.getClass(), tiendaId.getId());
                feriado.setTiendaId(tiendaId);
            }
            em.persist(feriado);
            if (tiendaId != null) {
                tiendaId.getFeriadoList().add(feriado);
                tiendaId = em.merge(tiendaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Feriado feriado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Feriado persistentFeriado = em.find(Feriado.class, feriado.getId());
            Tienda tiendaIdOld = persistentFeriado.getTiendaId();
            Tienda tiendaIdNew = feriado.getTiendaId();
            if (tiendaIdNew != null) {
                tiendaIdNew = em.getReference(tiendaIdNew.getClass(), tiendaIdNew.getId());
                feriado.setTiendaId(tiendaIdNew);
            }
            feriado = em.merge(feriado);
            if (tiendaIdOld != null && !tiendaIdOld.equals(tiendaIdNew)) {
                tiendaIdOld.getFeriadoList().remove(feriado);
                tiendaIdOld = em.merge(tiendaIdOld);
            }
            if (tiendaIdNew != null && !tiendaIdNew.equals(tiendaIdOld)) {
                tiendaIdNew.getFeriadoList().add(feriado);
                tiendaIdNew = em.merge(tiendaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = feriado.getId();
                if (findFeriado(id) == null) {
                    throw new NonexistentEntityException("The feriado with id " + id + " no longer exists.");
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
            Feriado feriado;
            try {
                feriado = em.getReference(Feriado.class, id);
                feriado.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The feriado with id " + id + " no longer exists.", enfe);
            }
            Tienda tiendaId = feriado.getTiendaId();
            if (tiendaId != null) {
                tiendaId.getFeriadoList().remove(feriado);
                tiendaId = em.merge(tiendaId);
            }
            em.remove(feriado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Feriado> findFeriadoEntities() {
        return findFeriadoEntities(true, -1, -1);
    }

    public List<Feriado> findFeriadoEntities(int maxResults, int firstResult) {
        return findFeriadoEntities(false, maxResults, firstResult);
    }

    private List<Feriado> findFeriadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Feriado.class));
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

    public Feriado findFeriado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Feriado.class, id);
        } finally {
            em.close();
        }
    }

    public int getFeriadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Feriado> rt = cq.from(Feriado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
