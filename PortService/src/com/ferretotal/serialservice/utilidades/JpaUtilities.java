package com.ferretotal.serialservice.utilidades;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author jgcastillo
 */
public class JpaUtilities {

    private static String PERSITENCE_UNIT = "PortServicePU";
    
    public static int HABILITADO = 1;
    public static int DESHABILITADO = 0;
    
    public static EntityManagerFactory getEntityManagerFactory(){
        return Persistence.createEntityManagerFactory(PERSITENCE_UNIT);
    }
}
