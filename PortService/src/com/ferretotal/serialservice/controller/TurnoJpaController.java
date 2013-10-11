/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller;

import com.ferretotal.serialservice.controller.exceptions.IllegalOrphanException;
import com.ferretotal.serialservice.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.ferretotal.serialservice.entity.Tienda;
import com.ferretotal.serialservice.entity.Tiempo;
import com.ferretotal.serialservice.entity.Turno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TurnoJpaController implements Serializable {

    public TurnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turno turno) {
        if (turno.getTiempoList() == null) {
            turno.setTiempoList(new ArrayList<Tiempo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda tiendaId = turno.getTiendaId();
            if (tiendaId != null) {
                tiendaId = em.getReference(tiendaId.getClass(), tiendaId.getId());
                turno.setTiendaId(tiendaId);
            }
            List<Tiempo> attachedTiempoList = new ArrayList<Tiempo>();
            for (Tiempo tiempoListTiempoToAttach : turno.getTiempoList()) {
                tiempoListTiempoToAttach = em.getReference(tiempoListTiempoToAttach.getClass(), tiempoListTiempoToAttach.getId());
                attachedTiempoList.add(tiempoListTiempoToAttach);
            }
            turno.setTiempoList(attachedTiempoList);
            em.persist(turno);
            if (tiendaId != null) {
                tiendaId.getTurnoList().add(turno);
                tiendaId = em.merge(tiendaId);
            }
            for (Tiempo tiempoListTiempo : turno.getTiempoList()) {
                Turno oldTurnoIdOfTiempoListTiempo = tiempoListTiempo.getTurnoId();
                tiempoListTiempo.setTurnoId(turno);
                tiempoListTiempo = em.merge(tiempoListTiempo);
                if (oldTurnoIdOfTiempoListTiempo != null) {
                    oldTurnoIdOfTiempoListTiempo.getTiempoList().remove(tiempoListTiempo);
                    oldTurnoIdOfTiempoListTiempo = em.merge(oldTurnoIdOfTiempoListTiempo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turno turno) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turno persistentTurno = em.find(Turno.class, turno.getId());
            Tienda tiendaIdOld = persistentTurno.getTiendaId();
            Tienda tiendaIdNew = turno.getTiendaId();
            List<Tiempo> tiempoListOld = persistentTurno.getTiempoList();
            List<Tiempo> tiempoListNew = turno.getTiempoList();
            List<String> illegalOrphanMessages = null;
            for (Tiempo tiempoListOldTiempo : tiempoListOld) {
                if (!tiempoListNew.contains(tiempoListOldTiempo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tiempo " + tiempoListOldTiempo + " since its turnoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tiendaIdNew != null) {
                tiendaIdNew = em.getReference(tiendaIdNew.getClass(), tiendaIdNew.getId());
                turno.setTiendaId(tiendaIdNew);
            }
            List<Tiempo> attachedTiempoListNew = new ArrayList<Tiempo>();
            for (Tiempo tiempoListNewTiempoToAttach : tiempoListNew) {
                tiempoListNewTiempoToAttach = em.getReference(tiempoListNewTiempoToAttach.getClass(), tiempoListNewTiempoToAttach.getId());
                attachedTiempoListNew.add(tiempoListNewTiempoToAttach);
            }
            tiempoListNew = attachedTiempoListNew;
            turno.setTiempoList(tiempoListNew);
            turno = em.merge(turno);
            if (tiendaIdOld != null && !tiendaIdOld.equals(tiendaIdNew)) {
                tiendaIdOld.getTurnoList().remove(turno);
                tiendaIdOld = em.merge(tiendaIdOld);
            }
            if (tiendaIdNew != null && !tiendaIdNew.equals(tiendaIdOld)) {
                tiendaIdNew.getTurnoList().add(turno);
                tiendaIdNew = em.merge(tiendaIdNew);
            }
            for (Tiempo tiempoListNewTiempo : tiempoListNew) {
                if (!tiempoListOld.contains(tiempoListNewTiempo)) {
                    Turno oldTurnoIdOfTiempoListNewTiempo = tiempoListNewTiempo.getTurnoId();
                    tiempoListNewTiempo.setTurnoId(turno);
                    tiempoListNewTiempo = em.merge(tiempoListNewTiempo);
                    if (oldTurnoIdOfTiempoListNewTiempo != null && !oldTurnoIdOfTiempoListNewTiempo.equals(turno)) {
                        oldTurnoIdOfTiempoListNewTiempo.getTiempoList().remove(tiempoListNewTiempo);
                        oldTurnoIdOfTiempoListNewTiempo = em.merge(oldTurnoIdOfTiempoListNewTiempo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = turno.getId();
                if (findTurno(id) == null) {
                    throw new NonexistentEntityException("The turno with id " + id + " no longer exists.");
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
            Turno turno;
            try {
                turno = em.getReference(Turno.class, id);
                turno.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turno with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tiempo> tiempoListOrphanCheck = turno.getTiempoList();
            for (Tiempo tiempoListOrphanCheckTiempo : tiempoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Turno (" + turno + ") cannot be destroyed since the Tiempo " + tiempoListOrphanCheckTiempo + " in its tiempoList field has a non-nullable turnoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tienda tiendaId = turno.getTiendaId();
            if (tiendaId != null) {
                tiendaId.getTurnoList().remove(turno);
                tiendaId = em.merge(tiendaId);
            }
            em.remove(turno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Turno> findTurnoEntities() {
        return findTurnoEntities(true, -1, -1);
    }

    public List<Turno> findTurnoEntities(int maxResults, int firstResult) {
        return findTurnoEntities(false, maxResults, firstResult);
    }

    private List<Turno> findTurnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turno.class));
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

    public Turno findTurno(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turno.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turno> rt = cq.from(Turno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
