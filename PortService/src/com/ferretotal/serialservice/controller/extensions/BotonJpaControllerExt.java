package com.ferretotal.serialservice.controller.extensions;

import com.ferretotal.serialservice.controller.BotonJpaController;
import com.ferretotal.serialservice.entity.Boton;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class BotonJpaControllerExt extends BotonJpaController{

    private static final Logger logger = LoggerFactory.getLogger(BotonJpaControllerExt.class);
    
    public BotonJpaControllerExt(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Boton findBoton(String ubicacion){
        EntityManager em = getEntityManager();
        Boton boton = null;
        try {
            Query query = em.createQuery("SELECT b FROM Boton b WHERE b.ubicacion = :ubicacion AND b.status = 1");
            query.setParameter("ubicacion", ubicacion);
            boton = (Boton)query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error en base datos: " + e.getMessage());
        } finally {
            em.close();
            return boton;
        }
    }
    
    public Boton findBotonByAddress(String address){
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
            query.setParameter("address", address);
            Boton boton = (Boton) query.getSingleResult();
            return boton;
        } finally {
            em.close();
        }
    }
    
    public boolean botonExiste(String direccion){
        EntityManager em = getEntityManager();
        boolean result = false;
        try {
            Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
            query.setParameter("address", direccion);
            Boton boton = (Boton)query.getSingleResult();    
            result = true;
        } finally {
            em.close();
        }
         
        return result;
    }
    
    @Override
    public List<Boton> findBotonEntities(){
        EntityManager em = getEntityManager();
        List<Boton> botones = null;
        try {
            Query query = em.createQuery("SELECT b FROM Boton b WHERE b.status = 1");
            botones = query.getResultList();
        } finally{
            em.close();
            return botones;
        }
        
    }
    
    public List<String> findBotonUbicacion(){
        EntityManager em = getEntityManager();
        List<String> ubicaciones = null;
        try {
            Query query = em.createQuery("SELECT DISTINCT b.ubicacion FROM Boton b");
            ubicaciones = query.getResultList();
        } catch (Exception e) {
            logger.error("Error en base datos: " + e.getMessage());
        } finally {
            em.close();
            return ubicaciones;
        }
    }

    public Boton findBotonByDireccion(String direccion) {
        EntityManager em = getEntityManager();
        Boton boton = null;
        try {
            Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
            query.setParameter("address", direccion);
            boton = (Boton) query.getSingleResult();
        } catch (Exception e) {
        }
        return boton;
    }
}
