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
import com.ferretotal.serialservice.entity.Asesor;
import java.util.ArrayList;
import java.util.List;
import com.ferretotal.serialservice.entity.Boton;
import com.ferretotal.serialservice.entity.Feriado;
import com.ferretotal.serialservice.entity.Llamada;
import com.ferretotal.serialservice.entity.Tienda;
import com.ferretotal.serialservice.entity.Turno;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author jgcastillo
 */
public class TiendaJpaController implements Serializable {

    public TiendaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tienda tienda) {
        if (tienda.getAsesorList() == null) {
            tienda.setAsesorList(new ArrayList<Asesor>());
        }
        if (tienda.getBotonList() == null) {
            tienda.setBotonList(new ArrayList<Boton>());
        }
        if (tienda.getFeriadoList() == null) {
            tienda.setFeriadoList(new ArrayList<Feriado>());
        }
        if (tienda.getLlamadaList() == null) {
            tienda.setLlamadaList(new ArrayList<Llamada>());
        }
        if (tienda.getTurnoList() == null) {
            tienda.setTurnoList(new ArrayList<Turno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Asesor> attachedAsesorList = new ArrayList<Asesor>();
            for (Asesor asesorListAsesorToAttach : tienda.getAsesorList()) {
                asesorListAsesorToAttach = em.getReference(asesorListAsesorToAttach.getClass(), asesorListAsesorToAttach.getId());
                attachedAsesorList.add(asesorListAsesorToAttach);
            }
            tienda.setAsesorList(attachedAsesorList);
            List<Boton> attachedBotonList = new ArrayList<Boton>();
            for (Boton botonListBotonToAttach : tienda.getBotonList()) {
                botonListBotonToAttach = em.getReference(botonListBotonToAttach.getClass(), botonListBotonToAttach.getId());
                attachedBotonList.add(botonListBotonToAttach);
            }
            tienda.setBotonList(attachedBotonList);
            List<Feriado> attachedFeriadoList = new ArrayList<Feriado>();
            for (Feriado feriadoListFeriadoToAttach : tienda.getFeriadoList()) {
                feriadoListFeriadoToAttach = em.getReference(feriadoListFeriadoToAttach.getClass(), feriadoListFeriadoToAttach.getId());
                attachedFeriadoList.add(feriadoListFeriadoToAttach);
            }
            tienda.setFeriadoList(attachedFeriadoList);
            List<Llamada> attachedLlamadaList = new ArrayList<Llamada>();
            for (Llamada llamadaListLlamadaToAttach : tienda.getLlamadaList()) {
                llamadaListLlamadaToAttach = em.getReference(llamadaListLlamadaToAttach.getClass(), llamadaListLlamadaToAttach.getId());
                attachedLlamadaList.add(llamadaListLlamadaToAttach);
            }
            tienda.setLlamadaList(attachedLlamadaList);
            List<Turno> attachedTurnoList = new ArrayList<Turno>();
            for (Turno turnoListTurnoToAttach : tienda.getTurnoList()) {
                turnoListTurnoToAttach = em.getReference(turnoListTurnoToAttach.getClass(), turnoListTurnoToAttach.getId());
                attachedTurnoList.add(turnoListTurnoToAttach);
            }
            tienda.setTurnoList(attachedTurnoList);
            em.persist(tienda);
            for (Asesor asesorListAsesor : tienda.getAsesorList()) {
                Tienda oldTiendaIdOfAsesorListAsesor = asesorListAsesor.getTiendaId();
                asesorListAsesor.setTiendaId(tienda);
                asesorListAsesor = em.merge(asesorListAsesor);
                if (oldTiendaIdOfAsesorListAsesor != null) {
                    oldTiendaIdOfAsesorListAsesor.getAsesorList().remove(asesorListAsesor);
                    oldTiendaIdOfAsesorListAsesor = em.merge(oldTiendaIdOfAsesorListAsesor);
                }
            }
            for (Boton botonListBoton : tienda.getBotonList()) {
                Tienda oldTiendaIdOfBotonListBoton = botonListBoton.getTiendaId();
                botonListBoton.setTiendaId(tienda);
                botonListBoton = em.merge(botonListBoton);
                if (oldTiendaIdOfBotonListBoton != null) {
                    oldTiendaIdOfBotonListBoton.getBotonList().remove(botonListBoton);
                    oldTiendaIdOfBotonListBoton = em.merge(oldTiendaIdOfBotonListBoton);
                }
            }
            for (Feriado feriadoListFeriado : tienda.getFeriadoList()) {
                Tienda oldTiendaIdOfFeriadoListFeriado = feriadoListFeriado.getTiendaId();
                feriadoListFeriado.setTiendaId(tienda);
                feriadoListFeriado = em.merge(feriadoListFeriado);
                if (oldTiendaIdOfFeriadoListFeriado != null) {
                    oldTiendaIdOfFeriadoListFeriado.getFeriadoList().remove(feriadoListFeriado);
                    oldTiendaIdOfFeriadoListFeriado = em.merge(oldTiendaIdOfFeriadoListFeriado);
                }
            }
            for (Llamada llamadaListLlamada : tienda.getLlamadaList()) {
                Tienda oldTiendaIdOfLlamadaListLlamada = llamadaListLlamada.getTiendaId();
                llamadaListLlamada.setTiendaId(tienda);
                llamadaListLlamada = em.merge(llamadaListLlamada);
                if (oldTiendaIdOfLlamadaListLlamada != null) {
                    oldTiendaIdOfLlamadaListLlamada.getLlamadaList().remove(llamadaListLlamada);
                    oldTiendaIdOfLlamadaListLlamada = em.merge(oldTiendaIdOfLlamadaListLlamada);
                }
            }
            for (Turno turnoListTurno : tienda.getTurnoList()) {
                Tienda oldTiendaIdOfTurnoListTurno = turnoListTurno.getTiendaId();
                turnoListTurno.setTiendaId(tienda);
                turnoListTurno = em.merge(turnoListTurno);
                if (oldTiendaIdOfTurnoListTurno != null) {
                    oldTiendaIdOfTurnoListTurno.getTurnoList().remove(turnoListTurno);
                    oldTiendaIdOfTurnoListTurno = em.merge(oldTiendaIdOfTurnoListTurno);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tienda tienda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda persistentTienda = em.find(Tienda.class, tienda.getId());
            List<Asesor> asesorListOld = persistentTienda.getAsesorList();
            List<Asesor> asesorListNew = tienda.getAsesorList();
            List<Boton> botonListOld = persistentTienda.getBotonList();
            List<Boton> botonListNew = tienda.getBotonList();
            List<Feriado> feriadoListOld = persistentTienda.getFeriadoList();
            List<Feriado> feriadoListNew = tienda.getFeriadoList();
            List<Llamada> llamadaListOld = persistentTienda.getLlamadaList();
            List<Llamada> llamadaListNew = tienda.getLlamadaList();
            List<Turno> turnoListOld = persistentTienda.getTurnoList();
            List<Turno> turnoListNew = tienda.getTurnoList();
            List<String> illegalOrphanMessages = null;
            for (Asesor asesorListOldAsesor : asesorListOld) {
                if (!asesorListNew.contains(asesorListOldAsesor)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Asesor " + asesorListOldAsesor + " since its tiendaId field is not nullable.");
                }
            }
            for (Boton botonListOldBoton : botonListOld) {
                if (!botonListNew.contains(botonListOldBoton)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Boton " + botonListOldBoton + " since its tiendaId field is not nullable.");
                }
            }
            for (Feriado feriadoListOldFeriado : feriadoListOld) {
                if (!feriadoListNew.contains(feriadoListOldFeriado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Feriado " + feriadoListOldFeriado + " since its tiendaId field is not nullable.");
                }
            }
            for (Llamada llamadaListOldLlamada : llamadaListOld) {
                if (!llamadaListNew.contains(llamadaListOldLlamada)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Llamada " + llamadaListOldLlamada + " since its tiendaId field is not nullable.");
                }
            }
            for (Turno turnoListOldTurno : turnoListOld) {
                if (!turnoListNew.contains(turnoListOldTurno)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Turno " + turnoListOldTurno + " since its tiendaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Asesor> attachedAsesorListNew = new ArrayList<Asesor>();
            for (Asesor asesorListNewAsesorToAttach : asesorListNew) {
                asesorListNewAsesorToAttach = em.getReference(asesorListNewAsesorToAttach.getClass(), asesorListNewAsesorToAttach.getId());
                attachedAsesorListNew.add(asesorListNewAsesorToAttach);
            }
            asesorListNew = attachedAsesorListNew;
            tienda.setAsesorList(asesorListNew);
            List<Boton> attachedBotonListNew = new ArrayList<Boton>();
            for (Boton botonListNewBotonToAttach : botonListNew) {
                botonListNewBotonToAttach = em.getReference(botonListNewBotonToAttach.getClass(), botonListNewBotonToAttach.getId());
                attachedBotonListNew.add(botonListNewBotonToAttach);
            }
            botonListNew = attachedBotonListNew;
            tienda.setBotonList(botonListNew);
            List<Feriado> attachedFeriadoListNew = new ArrayList<Feriado>();
            for (Feriado feriadoListNewFeriadoToAttach : feriadoListNew) {
                feriadoListNewFeriadoToAttach = em.getReference(feriadoListNewFeriadoToAttach.getClass(), feriadoListNewFeriadoToAttach.getId());
                attachedFeriadoListNew.add(feriadoListNewFeriadoToAttach);
            }
            feriadoListNew = attachedFeriadoListNew;
            tienda.setFeriadoList(feriadoListNew);
            List<Llamada> attachedLlamadaListNew = new ArrayList<Llamada>();
            for (Llamada llamadaListNewLlamadaToAttach : llamadaListNew) {
                llamadaListNewLlamadaToAttach = em.getReference(llamadaListNewLlamadaToAttach.getClass(), llamadaListNewLlamadaToAttach.getId());
                attachedLlamadaListNew.add(llamadaListNewLlamadaToAttach);
            }
            llamadaListNew = attachedLlamadaListNew;
            tienda.setLlamadaList(llamadaListNew);
            List<Turno> attachedTurnoListNew = new ArrayList<Turno>();
            for (Turno turnoListNewTurnoToAttach : turnoListNew) {
                turnoListNewTurnoToAttach = em.getReference(turnoListNewTurnoToAttach.getClass(), turnoListNewTurnoToAttach.getId());
                attachedTurnoListNew.add(turnoListNewTurnoToAttach);
            }
            turnoListNew = attachedTurnoListNew;
            tienda.setTurnoList(turnoListNew);
            tienda = em.merge(tienda);
            for (Asesor asesorListNewAsesor : asesorListNew) {
                if (!asesorListOld.contains(asesorListNewAsesor)) {
                    Tienda oldTiendaIdOfAsesorListNewAsesor = asesorListNewAsesor.getTiendaId();
                    asesorListNewAsesor.setTiendaId(tienda);
                    asesorListNewAsesor = em.merge(asesorListNewAsesor);
                    if (oldTiendaIdOfAsesorListNewAsesor != null && !oldTiendaIdOfAsesorListNewAsesor.equals(tienda)) {
                        oldTiendaIdOfAsesorListNewAsesor.getAsesorList().remove(asesorListNewAsesor);
                        oldTiendaIdOfAsesorListNewAsesor = em.merge(oldTiendaIdOfAsesorListNewAsesor);
                    }
                }
            }
            for (Boton botonListNewBoton : botonListNew) {
                if (!botonListOld.contains(botonListNewBoton)) {
                    Tienda oldTiendaIdOfBotonListNewBoton = botonListNewBoton.getTiendaId();
                    botonListNewBoton.setTiendaId(tienda);
                    botonListNewBoton = em.merge(botonListNewBoton);
                    if (oldTiendaIdOfBotonListNewBoton != null && !oldTiendaIdOfBotonListNewBoton.equals(tienda)) {
                        oldTiendaIdOfBotonListNewBoton.getBotonList().remove(botonListNewBoton);
                        oldTiendaIdOfBotonListNewBoton = em.merge(oldTiendaIdOfBotonListNewBoton);
                    }
                }
            }
            for (Feriado feriadoListNewFeriado : feriadoListNew) {
                if (!feriadoListOld.contains(feriadoListNewFeriado)) {
                    Tienda oldTiendaIdOfFeriadoListNewFeriado = feriadoListNewFeriado.getTiendaId();
                    feriadoListNewFeriado.setTiendaId(tienda);
                    feriadoListNewFeriado = em.merge(feriadoListNewFeriado);
                    if (oldTiendaIdOfFeriadoListNewFeriado != null && !oldTiendaIdOfFeriadoListNewFeriado.equals(tienda)) {
                        oldTiendaIdOfFeriadoListNewFeriado.getFeriadoList().remove(feriadoListNewFeriado);
                        oldTiendaIdOfFeriadoListNewFeriado = em.merge(oldTiendaIdOfFeriadoListNewFeriado);
                    }
                }
            }
            for (Llamada llamadaListNewLlamada : llamadaListNew) {
                if (!llamadaListOld.contains(llamadaListNewLlamada)) {
                    Tienda oldTiendaIdOfLlamadaListNewLlamada = llamadaListNewLlamada.getTiendaId();
                    llamadaListNewLlamada.setTiendaId(tienda);
                    llamadaListNewLlamada = em.merge(llamadaListNewLlamada);
                    if (oldTiendaIdOfLlamadaListNewLlamada != null && !oldTiendaIdOfLlamadaListNewLlamada.equals(tienda)) {
                        oldTiendaIdOfLlamadaListNewLlamada.getLlamadaList().remove(llamadaListNewLlamada);
                        oldTiendaIdOfLlamadaListNewLlamada = em.merge(oldTiendaIdOfLlamadaListNewLlamada);
                    }
                }
            }
            for (Turno turnoListNewTurno : turnoListNew) {
                if (!turnoListOld.contains(turnoListNewTurno)) {
                    Tienda oldTiendaIdOfTurnoListNewTurno = turnoListNewTurno.getTiendaId();
                    turnoListNewTurno.setTiendaId(tienda);
                    turnoListNewTurno = em.merge(turnoListNewTurno);
                    if (oldTiendaIdOfTurnoListNewTurno != null && !oldTiendaIdOfTurnoListNewTurno.equals(tienda)) {
                        oldTiendaIdOfTurnoListNewTurno.getTurnoList().remove(turnoListNewTurno);
                        oldTiendaIdOfTurnoListNewTurno = em.merge(oldTiendaIdOfTurnoListNewTurno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tienda.getId();
                if (findTienda(id) == null) {
                    throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.");
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
            Tienda tienda;
            try {
                tienda = em.getReference(Tienda.class, id);
                tienda.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Asesor> asesorListOrphanCheck = tienda.getAsesorList();
            for (Asesor asesorListOrphanCheckAsesor : asesorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Asesor " + asesorListOrphanCheckAsesor + " in its asesorList field has a non-nullable tiendaId field.");
            }
            List<Boton> botonListOrphanCheck = tienda.getBotonList();
            for (Boton botonListOrphanCheckBoton : botonListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Boton " + botonListOrphanCheckBoton + " in its botonList field has a non-nullable tiendaId field.");
            }
            List<Feriado> feriadoListOrphanCheck = tienda.getFeriadoList();
            for (Feriado feriadoListOrphanCheckFeriado : feriadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Feriado " + feriadoListOrphanCheckFeriado + " in its feriadoList field has a non-nullable tiendaId field.");
            }
            List<Llamada> llamadaListOrphanCheck = tienda.getLlamadaList();
            for (Llamada llamadaListOrphanCheckLlamada : llamadaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Llamada " + llamadaListOrphanCheckLlamada + " in its llamadaList field has a non-nullable tiendaId field.");
            }
            List<Turno> turnoListOrphanCheck = tienda.getTurnoList();
            for (Turno turnoListOrphanCheckTurno : turnoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Turno " + turnoListOrphanCheckTurno + " in its turnoList field has a non-nullable tiendaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tienda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tienda> findTiendaEntities() {
        return findTiendaEntities(true, -1, -1);
    }

    public List<Tienda> findTiendaEntities(int maxResults, int firstResult) {
        return findTiendaEntities(false, maxResults, firstResult);
    }

    private List<Tienda> findTiendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tienda.class));
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

    public Tienda findTienda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tienda> rt = cq.from(Tienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
