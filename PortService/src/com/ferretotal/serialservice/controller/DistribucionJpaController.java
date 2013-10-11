/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.IllegalOrphanException;
import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import com.ferretotal.serialservice.entity.Distribucion;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ferretotal.serialservice.entity.Llamada;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class DistribucionJpaController implements Serializable {

    public DistribucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Distribucion distribucion) {
        if (distribucion.getLlamadaList() == null) {
            distribucion.setLlamadaList(new ArrayList<Llamada>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Llamada> attachedLlamadaList = new ArrayList<Llamada>();
            for (Llamada llamadaListLlamadaToAttach : distribucion.getLlamadaList()) {
                llamadaListLlamadaToAttach = em.getReference(llamadaListLlamadaToAttach.getClass(), llamadaListLlamadaToAttach.getId());
                attachedLlamadaList.add(llamadaListLlamadaToAttach);
            }
            distribucion.setLlamadaList(attachedLlamadaList);
            em.persist(distribucion);
            for (Llamada llamadaListLlamada : distribucion.getLlamadaList()) {
                Distribucion oldDistribucionIdOfLlamadaListLlamada = llamadaListLlamada.getDistribucionId();
                llamadaListLlamada.setDistribucionId(distribucion);
                llamadaListLlamada = em.merge(llamadaListLlamada);
                if (oldDistribucionIdOfLlamadaListLlamada != null) {
                    oldDistribucionIdOfLlamadaListLlamada.getLlamadaList().remove(llamadaListLlamada);
                    oldDistribucionIdOfLlamadaListLlamada = em.merge(oldDistribucionIdOfLlamadaListLlamada);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Distribucion distribucion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distribucion persistentDistribucion = em.find(Distribucion.class, distribucion.getId());
            List<Llamada> llamadaListOld = persistentDistribucion.getLlamadaList();
            List<Llamada> llamadaListNew = distribucion.getLlamadaList();
            List<String> illegalOrphanMessages = null;
            for (Llamada llamadaListOldLlamada : llamadaListOld) {
                if (!llamadaListNew.contains(llamadaListOldLlamada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Llamada " + llamadaListOldLlamada + " since its distribucionId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Llamada> attachedLlamadaListNew = new ArrayList<Llamada>();
            for (Llamada llamadaListNewLlamadaToAttach : llamadaListNew) {
                llamadaListNewLlamadaToAttach = em.getReference(llamadaListNewLlamadaToAttach.getClass(), llamadaListNewLlamadaToAttach.getId());
                attachedLlamadaListNew.add(llamadaListNewLlamadaToAttach);
            }
            llamadaListNew = attachedLlamadaListNew;
            distribucion.setLlamadaList(llamadaListNew);
            distribucion = em.merge(distribucion);
            for (Llamada llamadaListNewLlamada : llamadaListNew) {
                if (!llamadaListOld.contains(llamadaListNewLlamada)) {
                    Distribucion oldDistribucionIdOfLlamadaListNewLlamada = llamadaListNewLlamada.getDistribucionId();
                    llamadaListNewLlamada.setDistribucionId(distribucion);
                    llamadaListNewLlamada = em.merge(llamadaListNewLlamada);
                    if (oldDistribucionIdOfLlamadaListNewLlamada != null && !oldDistribucionIdOfLlamadaListNewLlamada.equals(distribucion)) {
                        oldDistribucionIdOfLlamadaListNewLlamada.getLlamadaList().remove(llamadaListNewLlamada);
                        oldDistribucionIdOfLlamadaListNewLlamada = em.merge(oldDistribucionIdOfLlamadaListNewLlamada);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = distribucion.getId();
                if (findDistribucion(id) == null) {
                    throw new NonexistentEntityException("The distribucion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distribucion distribucion;
            try {
                distribucion = em.getReference(Distribucion.class, id);
                distribucion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distribucion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Llamada> llamadaListOrphanCheck = distribucion.getLlamadaList();
            for (Llamada llamadaListOrphanCheckLlamada : llamadaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Distribucion (" + distribucion + ") cannot be destroyed since the Llamada " + llamadaListOrphanCheckLlamada + " in its llamadaList field has a non-nullable distribucionId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(distribucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Distribucion> findDistribucionEntities() {
        return findDistribucionEntities(true, -1, -1);
    }

    public List<Distribucion> findDistribucionEntities(int maxResults, int firstResult) {
        return findDistribucionEntities(false, maxResults, firstResult);
    }

    private List<Distribucion> findDistribucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Distribucion.class));
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

    public Distribucion findDistribucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Distribucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistribucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Distribucion> rt = cq.from(Distribucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
