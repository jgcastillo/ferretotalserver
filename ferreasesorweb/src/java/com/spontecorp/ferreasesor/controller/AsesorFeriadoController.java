/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.ext.AsesorFacadeExt;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "asesorFeriadoController")
@SessionScoped
public class AsesorFeriadoController implements Serializable {
    
    private Asesor asesor;
    private Asesor oldAsesor;
    
    @EJB
    private AsesorFacade ejbFacade;
    @EJB
    private AsesorFacadeExt ejbFacadeExt;
    
    public AsesorFeriadoController() {
    }
    
    /**
     * Se Habilita la Distribución del Asesor Seleccionado
     * y se Inhabilita la Distribución del Asesor asignado anteriormente
     * @return 
     */
    public String updateAsesorFeriado() {
        try {
            //Busco el Asesor Feriado anterior e Inhabilito su Distribución
            if(oldAsesor != null){
                ejbFacadeExt.changeAsesorFeriado(oldAsesor, JpaUtilities.INHABILITADO);
            }
            //Busco el Asesor Feriado seleccionado y Habilito su Distribución
            ejbFacadeExt.changeAsesorFeriado(asesor, JpaUtilities.HABILITADO);
            
            JsfUtil.addSuccessMessage("Nuevo Asesor Asignado con éxito!");
            return "Create";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    /**
     * Busco el Asesor actual Asignado al Turno Feriado
     * @return 
     */
    public Asesor getOldAsesor() {
        oldAsesor = new Asesor();
        oldAsesor = ejbFacadeExt.findAsesorFeriado();
        return oldAsesor;
    }
    
    /**
     * Lista de Asesores Habilitados
     * @return 
     */
    public SelectItem[] getAsesoresAvalaibleSelectOne() {
        List<Asesor> asesores = ejbFacadeExt.findAsesorEnabled();
        return JsfUtil.getSelectItems(asesores, true);
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }
    
}
