package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Ubicacion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UbicacionFacade extends AbstractFacade<Ubicacion> {

    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UbicacionFacade() {
        super(Ubicacion.class);
    }
}
