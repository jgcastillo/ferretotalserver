/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;


import com.spontecorp.ferretotalserver.entity.ConfigurationDb;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author sponte03
 */
@Stateless
public class ConfigurationDbFacade extends AbstractFacade<ConfigurationDb> {
    @PersistenceContext(unitName = "FerreAsesorServerPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfigurationDbFacade() {
        super(ConfigurationDb.class);
    }
    
}
