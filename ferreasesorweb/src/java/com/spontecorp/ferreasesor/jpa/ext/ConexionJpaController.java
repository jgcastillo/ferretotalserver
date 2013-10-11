/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.entity.Feriado;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.FeriadoFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author jgcastillo
 */
@Stateless
public class ConexionJpaController {
    
    @EJB
    private TiendaFacade controllerTienda;
    @EJB
    private TurnoJpaControllerExt controllerTurno;
    @EJB
    private FeriadoFacade controllerFeriado;
    @EJB
    private FeriadoJpaControllerExt controllerFeriadoExt;

    public ConexionJpaController() {
    }
    
    public Tienda getTienda() {
        Tienda tienda = controllerTienda.find(JpaUtilities.ID_TIENDA);
        return tienda;
    }
    
    public List<Turno> getTurnos() {
        return controllerTurno.findTurnoEntities();
    }
    
    public List<Feriado> findFeriadoEntities() {
        return controllerFeriado.findAll();
    }
    
    public Feriado findFeriado(int dia, int mes) {
        return controllerFeriadoExt.findFeriado(dia, mes);
    }

    public Feriado findFeriado(String motivo) {
        return controllerFeriadoExt.findFeriado(motivo);
    }
}
