package com.spontecorp.ferreasesor.controller.ext;

import com.spontecorp.ferreasesor.controller.UsuarioController;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Usuario;
import com.spontecorp.ferreasesor.jpa.UsuarioFacade;
import com.spontecorp.ferreasesor.security.SecurePassword;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Casper
 */
@ManagedBean (name="usuarioControllerExt")
@SessionScoped
public class UsuarioControllerExt extends UsuarioController implements Serializable{
    
    @EJB
    private com.spontecorp.ferreasesor.jpa.UsuarioFacade ejbFacade;
    private Usuario current;
    private DataModel items = null;
    private String passwordInDB;
    
    @Override
    public Usuario getSelected() {
        if (current == null) {
            current = new Usuario();
        }
        return current;
    }
    
    private DataModel createPageDataModel() {
        return new ListDataModel(getFacade().findAll());
    }
    
    @Override
    public String prepareList() {
        recreateModel();
        return "List";
    }

    @Override
    public String prepareView() {
        current = (Usuario) getItems().getRowData();
        return "View";
    }
    
    @Override
    public String prepareEdit() {
        current = (Usuario) getItems().getRowData();
        passwordInDB = current.getPsw();
        return "Edit";
    }
    
    @Override
    public String updateStatus() {
        try {
            current.setStatus(0);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    @Override
    public String update() {
        
        if(!passwordInDB.equals(current.getPsw())){
            char[] password = (current.getPsw()).toCharArray();
            current.setPsw(SecurePassword.encript(password));
        } else {
            current.setPsw(passwordInDB);
        }
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    @Override
    public String create(){
        char[] password = (current.getPsw()).toCharArray();
        current.setPsw(SecurePassword.encript(password));
        current.setStatus(JpaUtilities.HABILITADO);
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    private UsuarioFacade getFacade(){
        return ejbFacade;
    }
    
    @Override
    public DataModel getItems() {
        if (items == null) {
            items = createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }
}
