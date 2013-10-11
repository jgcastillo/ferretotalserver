package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Boton;
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
public class BotonJpaControllerExt {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Boton findBoton(String ubicacion) {
        Boton boton = null;
        try {
            Query query = em.createQuery("SELECT b FROM Boton b WHERE b.ubicacion = :ubicacion AND b.status = 1");
            query.setParameter("ubicacion", ubicacion);
            boton = (Boton) query.getSingleResult();
        } catch (Exception e) {
        }
        return boton;
    }

     public Boton findBotonAddress(String address) {
        Boton boton = null;
        try {
            Query query = em.createQuery("SELECT b FROM Boton b WHERE b.address = :address AND b.status = 1");
            query.setParameter("address", address);
            boton = (Boton) query.getSingleResult();
        } catch (Exception e) {
        }
        return boton;
    }
    
    public Boton findBotonByAddress(String address) {
        Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
        query.setParameter("address", address);
        Boton boton = (Boton) query.getSingleResult();        
        System.out.println("el id del boton en la bd: "+boton.getId());
        return boton;

    }

    public boolean botonExiste(String direccion) {
        boolean result = false;
        try {
            Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
            query.setParameter("address", direccion);
            Boton boton = (Boton) query.getSingleResult();
            result = true;
        } catch (NoResultException e){}

        return result;
    }

    public List<Boton> findBotonEntities() {
        List<Boton> botones = null;
        try {
            Query query = em.createQuery("SELECT b FROM Boton b WHERE b.status = 1");
            botones = query.getResultList();
        } catch(NoResultException e) {
            
        }
        return botones;

    }

    public List<String> findBotonUbicacion() {
        List<String> ubicaciones = null;
        try {
            Query query = em.createQuery("SELECT DISTINCT b.ubicacion FROM Boton b");
            ubicaciones = query.getResultList();
        } catch (NoResultException e) {
        } 
            return ubicaciones;
    }

    public Boton findBotonByDireccion(String direccion) {
        Boton boton = null;
        try {
            Query query = em.createNamedQuery("Boton.findByAddress", Boton.class);
            query.setParameter("address", direccion);
            boton = (Boton) query.getSingleResult();
        } catch (NoResultException e) {
        }
        return boton;
    }
}
