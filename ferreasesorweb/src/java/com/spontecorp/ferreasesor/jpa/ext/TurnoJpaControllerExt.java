package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Turno;
import java.util.List;
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
public class TurnoJpaControllerExt {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Turno findTurno(String nombre) {
        Turno turno = null;
        try {
            Query query = em.createNamedQuery("Turno.findByNombre", Turno.class);
            query.setParameter("nombre", nombre);
            turno = (Turno) query.getSingleResult();
        } catch (NoResultException e) {
            // do nothing
        } 
            return turno;
    }

    public List<Turno> findTurnoEntities() {
        //System.out.println("3.- Llego a findTurnoEntities");
        List<Turno> turnos = null;
        try {
            Query query = em.createQuery("SELECT t FROM Turno t WHERE t.status = 1");
            turnos = query.getResultList();
         //   int i = 0;
//            for (Turno turno : turnos) {
//                System.out.println("Turno : " + turno.getNombre());
//                System.out.println("*** Atención Buena:" + turno.getTiempoList().get(i).getAtencionBuena());
//                System.out.println("*** Atención Regular: " + turno.getTiempoList().get(i).getAtencionRegular());
//                System.out.println("*** Atención Mala: " + turno.getTiempoList().get(i).getCerrarLlamada());
//                i++;
//            }
        } catch (NoResultException e) {

        }
        return turnos;
    }
}
