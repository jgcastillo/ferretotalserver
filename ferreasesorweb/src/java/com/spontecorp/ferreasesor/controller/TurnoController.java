package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.controller.util.PaginationHelper;
import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.jpa.DistribucionFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "turnoController")
@SessionScoped
public class TurnoController implements Serializable {

    private Turno current;
    private Tienda tienda;
    private DataModel items = null;
    
    @EJB
    private TurnoFacade ejbFacade;
    @EJB
    private TiendaFacade ejbFacadeTienda;
    @EJB
    private AsesorFacade ejbFacadeAsesor;
    @EJB
    private BotonFacade ejbFacadeBoton;
    @EJB
    private DistribucionFacade ejbFacadeDistribucion;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public TurnoController() {
    }

    public Turno getSelected() {
        if (current == null) {
            current = new Turno();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TurnoFacade getFacade() {
        return ejbFacade;
    }

    private TiendaFacade getFacadeTienda() {
        return ejbFacadeTienda;
    }
    
    private BotonFacade getFacadeBoton() {
        return ejbFacadeBoton;
    }
    
    private AsesorFacade getFacadeAsesor() {
        return ejbFacadeAsesor;
    }
    
    private DistribucionFacade getFacadeDistribucion() {
        return ejbFacadeDistribucion;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Turno) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Turno();
        selectedItemIndex = -1;
        return "Create";
    }

    public Tienda getTienda() {
        Tienda tienda;
        tienda = getFacadeTienda().find(JpaUtilities.ID_TIENDA);
        return tienda;
    }

    
    public String create() {
        try {
            //Seteo la Tienda
            tienda = getTienda();
            current.setTiendaId(tienda);
            
            //Guardo el Turno
            getFacade().create(current);
            
            //Listado de Asesores
            List<Asesor> asesores = getFacadeAsesor().findAll();
            //Listado de Botones
            List<Boton> botones = getFacadeBoton().findAll();
            
            //Para el Turno creado se hace el insert en la tabla Distribucion
            for (Asesor asesor : asesores) {
                for (Boton boton : botones) {
                    Distribucion dist = new Distribucion();
                    dist.setAsesorId(asesor.getId());
                    dist.setTurnoId(current.getId());
                    dist.setBotonId(boton.getId());
                    dist.setStatus(JpaUtilities.INHABILITADO);
                    ejbFacadeDistribucion.create(dist);
                }
            }
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TurnoCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Turno) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String updateStatus() {
        tienda = getTienda();
        current.setTiendaId(tienda);
        current.setStatus(JpaUtilities.INHABILITADO);
        update();
        return prepareList();
    }

    public String update() {
        try {
            tienda = getTienda();
            current.setTiendaId(tienda);
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TurnoUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Turno) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TurnoDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            //items = getPagination().createPageDataModel();
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Turno.class)
    public static class TurnoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TurnoController controller = (TurnoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "turnoController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Turno) {
                Turno o = (Turno) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Turno.class.getName());
            }
        }
    }
}
