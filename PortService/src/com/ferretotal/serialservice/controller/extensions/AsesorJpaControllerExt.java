package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.AsesorJpaController;
import com.ferretotal.serialservice.entity.Asesor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Casper
 */
public class AsesorJpaControllerExt extends AsesorJpaController{

    public AsesorJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Asesor findAsesor(String nombre, String apellido){
        EntityManager em = getEntityManager();
        Asesor asesor = null;
        try {
            String q = "SELECT a FROM Asesor a WHERE a.nombre = :nombre AND a.apellido = :apellido";
            Query query = em.createQuery(q);
            query.setParameter("nombre", nombre);
            query.setParameter("apellido", apellido);
            asesor = (Asesor)query.getSingleResult();
        } finally {
            em.close();
            return asesor;
        }
    }
    
    public List<Asesor> findAsesorEnabled(){
        EntityManager em = getEntityManager();
        List<Asesor> lista = null;
        try {
            String q = "SELECT a FROM Asesor a WHERE a.status = 1";
            Query query = em.createQuery(q);
            lista = query.getResultList();
        } finally {
            em.close();
            return lista;
        }
  
    }
    
}
