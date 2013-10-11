package com.spontecorp.ferreasesor.controller.ext;

import com.spontecorp.ferreasesor.controller.BotonController;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean (name = "botonControllerExt")
@SessionScoped
public class BotonControllerExt extends BotonController implements Serializable{
    
    private Boton current;
    @EJB
    private com.spontecorp.ferreasesor.jpa.BotonFacade ejbFacade;
    
    private BotonFacade getFacade() {
        return ejbFacade;
    }
    
    @Override
    public String update() {
        System.out.println("Entro a Update Ext.");
        current = super.getSelected();
        current.setStatus(0);
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BotonUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    public void destroyWorld(ActionEvent actionEvent){  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Error",  "Please try again later.");  
          
        FacesContext.getCurrentInstance().addMessage(null, message);  
    } 
    
    boolean showConfirm = false;

    public boolean isShowConfirm() {
        return showConfirm;
    }

    public void setShowConfirm(boolean showConfirm) {
        this.showConfirm = showConfirm;
    }

    public void overrideVersion() {
        System.out.println("Version alrady exists...Overriding...");
        FacesMessage msg = new FacesMessage("Action is successful");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void getConfirmMsg() {
        System.out.println("Inside getConfirmMsg()....");
        showConfirm = true;
        System.out.println("showConfirm: " + showConfirm);
    }
    
}