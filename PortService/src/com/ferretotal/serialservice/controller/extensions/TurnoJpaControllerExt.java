/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.TurnoJpaController;
import com.ferretotal.serialservice.entity.Turno;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
public class TurnoJpaControllerExt extends TurnoJpaController {

    public TurnoJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }

    public Turno findTurno(String nombre) {
        EntityManager em = getEntityManager();
        Turno turno = null;
        try {
            Query query = em.createNamedQuery("Turno.findByNombre", Turno.class);
            query.setParameter("nombre", nombre);
            turno = (Turno) query.getSingleResult();
        } catch (NoResultException e) {
            // do nothing
        } finally {
            em.close();
            return turno;
        }
    }
    
    @Override
    public List<Turno> findTurnoEntities(){
        System.out.println("3.- Llego a findTurnoEntities");
        EntityManager em = getEntityManager();
        List<Turno> turnos = null;
        try {
            Query query = em.createQuery("SELECT t FROM Turno t WHERE t.status = 1");
            turnos = query.getResultList();
            for (Turno turno : turnos) {
                System.out.println("Turno : " + turno.getNombre());
                System.out.println("*** Atención Buena:" + turno.getTiempoList().get(0).getAtencionBuena());
                System.out.println("*** Atención Regular: "+turno.getTiempoList().get(1).getAtencionRegular());
                System.out.println("*** Atención Mala: "+ turno.getTiempoList().get(2).getCerrarLlamada());
            }
        } finally {
            em.close();
            return turnos;
        }
    }
    
}
