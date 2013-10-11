/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa;

import com.spontecorp.ferreasesor.entity.Feriado;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jgcastillo
 */
@Stateless
public class FeriadoFacade extends AbstractFacade<Feriado> {
    @PersistenceContext(unitName = "FerreAsesorWebPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FeriadoFacade() {
        super(Feriado.class);
    }
    
}
