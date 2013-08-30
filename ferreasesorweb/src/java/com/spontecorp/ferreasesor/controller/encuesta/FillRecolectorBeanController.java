/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.controller.LoginBean;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Categoria;
import com.spontecorp.ferreasesor.entity.Motivo;
import com.spontecorp.ferreasesor.jpa.CategoriaFacade;
import com.spontecorp.ferreasesor.jpa.MotivoFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "fillRecolectorBean")
@ViewScoped
public class FillRecolectorBeanController implements Serializable {

    @EJB
    private CategoriaFacade categoriaFacade;
    @EJB
    private MotivoFacade motivoFacade;
    private List<Categoria> categoriaList = null;
    private Categoria categoria;
    private List<Motivo> motivoList = null;
    private Motivo motivo;
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;
    
    private boolean motivoListDisabled = true;

    public FillRecolectorBeanController() {
    }
    
    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
    
    /**
     * Listado de Categorías por Status (Activo)
     * @return 
     */
     public SelectItem[] getCategoriasAvalaibleSelectOne() {
        categoriaList = categoriaFacade.findCategoria(JpaUtilities.HABILITADO);
        return JsfUtil.getSelectItems(categoriaList, true);
    } 
     
     /**
      * Listado de Motivos por Status y Categoría
      * @return 
      */
     public SelectItem[] getMotivosAvalaibleSelectOne() {
        SelectItem[] arreglo = null;
        if (!motivoListDisabled && (categoria != null)) {
             motivoList = motivoFacade.findMotivo(JpaUtilities.HABILITADO, categoria);
            arreglo = JsfUtil.getSelectItems(motivoList, true);
        }
        return arreglo;
    }

    public void guardaRespuesta() {
        System.out.println("Entro a guardaRespuesta");
        System.out.println("Motivo: "+motivo);
        System.out.println("Usuario: "+loginBean.getUsuario());
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        motivoListDisabled = false;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public boolean isMotivoListDisabled() {
        return motivoListDisabled;
    }
    
    
    
}
