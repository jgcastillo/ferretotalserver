package com.spontecorp.ferreasesor.service;

import com.spontecorp.ferreasesor.entity.Usuario;
import com.spontecorp.ferreasesor.jpa.AbstractFacade;
import com.spontecorp.ferreasesor.jpa.UsuarioFacade;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author jgcastillo
 */
public class UsuarioService extends UsuarioFacade{
 
    public Usuario findUsuario(String user) {
        //EntityManager em = getEntityManager();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(AbstractFacade.PERSISTENCE_UNIT);
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createNamedQuery("Usuario.findByUser", Usuario.class);
            query.setParameter("user", user);
            return (Usuario) query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
