/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Llamada;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jgcastillo
 */

public class LlamadaFacade extends AbstractFacade<Llamada> {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LlamadaFacade() {
        super(Llamada.class);
    }

//    /**
//     * Listado de las ultimas 75 Llamadas cerradas accion = 0
//     *
//     * @return
//     */
//    public List<Llamada> findLastCalls() {
//        String query = "SELECT ll from Llamada ll where ll.accion = '0' ORDER BY ll.id DESC";
//        Query q = getEntityManager().createQuery(query);
//        q.setMaxResults(75);
//        return q.getResultList();
//    }
//
//    public List<Llamada> findLlamadasTiempo(Date fechaInicio, Date fechaFin) {
//        EntityManager em = getEntityManager();
//        List<Llamada> result = null;
//        try {
//            String query = "SELECT ll "
//                    + "FROM Llamada ll , Distribucion d, Tiempo t WHERE ll.distribucionId.id = d.id AND ll.tiempoId.id = t.id "
//                    + "AND ll.fechaClose = :fechaInicio AND ll.fechaClose = :fechaFin AND ll.accion = '0'"
//                    + "ORDER BY ll.id";
//
//            Query q = em.createQuery(query);
//            q.setParameter("fechaInicio", fechaInicio);
//            q.setParameter("fechaFin", fechaFin);
//            result = q.getResultList();
//        } catch (Exception e) {
//            System.out.println("El error es: " + e);
//        }
//        return result;
//    }
//
//    /**
//     * Selecciono la última Llamada abierta
//     *
//     * @param boton
//     * @return
//     */
//    public Llamada findLlamadaAbierta(Boton boton) {
//        EntityManager em = getEntityManager();
//        Llamada llamada = null;
//        try {
//            String q = "SELECT ll From Llamada ll INNER JOIN ll.distribucionId d "
//                    + "WHERE (ll.accion = :accion OR ll.accion = :accion1) AND d.botonId = :boton"
//                    + " ORDER BY ll.id DESC";
//            Query query = em.createQuery(q);
//            query.setParameter("accion", 2);
//            query.setParameter("accion1", 1);
//            query.setParameter("boton", boton.getId());
//            
//            llamada = (Llamada) query.getResultList().get(0);
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return llamada;
//    }
}
