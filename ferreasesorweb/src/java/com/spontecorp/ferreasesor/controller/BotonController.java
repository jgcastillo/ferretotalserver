package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.controller.util.PaginationHelper;
import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.AsesorFacade;
import com.spontecorp.ferreasesor.jpa.BotonFacade;
import com.spontecorp.ferreasesor.jpa.DistribucionFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import com.spontecorp.ferreasesor.jpa.ext.DistribucionJpaControllerExt;
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

@ManagedBean(name = "botonController")
@SessionScoped
public class BotonController implements Serializable {

    private Boton current;
    private Tienda tienda;
    private DataModel items = null;
    @EJB
    private BotonFacade ejbFacade;
    @EJB
    private TiendaFacade ejbFacadeTienda;
    @EJB
    private TurnoFacade ejbFacadeTurno;
    @EJB
    private AsesorFacade ejbFacadeAsesor;
    @EJB
    private DistribucionFacade ejbFacadeDistribucion;
    @EJB
    private DistribucionJpaControllerExt ejbFacadeDistribucionExt;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private JpaUtilities utils = new JpaUtilities();

    public BotonController() {
    }

    public Boton getSelected() {
        if (current == null) {
            current = new Boton();
            selectedItemIndex = -1;
        }
        return current;
    }

    private BotonFacade getFacade() {
        return ejbFacade;
    }

    private TiendaFacade getFacadeTienda() {
        return ejbFacadeTienda;
    }

    private TurnoFacade getFacadeTurno() {
        return ejbFacadeTurno;
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
        current = (Boton) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Boton();
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

            //Guardo el Boton
            getFacade().create(current);

            //Listado de Turnos
            List<Turno> turnos = getFacadeTurno().findAll();
            //Listado de Asesores
            List<Asesor> asesores = getFacadeAsesor().findAll();

            //Para el Boton creado se hace el insert en la tabla Distribucion
            for (Turno turno : turnos) {
                for (Asesor asesor : asesores) {
                    Distribucion dist = new Distribucion();
                    dist.setAsesorId(asesor.getId());
                    dist.setTurnoId(turno.getId());
                    dist.setBotonId(current.getId());
                    dist.setStatus(JpaUtilities.HABILITADO);
                    ejbFacadeDistribucion.create(dist);
                }
            }

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BotonCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Boton) getItems().getRowData();
        // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String updateStatus() {
        try {
            tienda = getTienda();
            current.setTiendaId(tienda);
            current.setStatus(JpaUtilities.INHABILITADO);
            //Listado de Turnos
            List<Turno> turnos = getFacadeTurno().findAll();
            //Listado de Asesores
            List<Asesor> asesores = getFacadeAsesor().findAll();

            //Inhabilitar la Distribución para este Botón
            for (Turno turno : turnos) {
                for (Asesor asesor : asesores) {
                    List<Distribucion> lista = ejbFacadeDistribucionExt.findDistribucionList(asesor, turno, current);
                    for (Distribucion dist : lista) {
                        dist.setStatus(JpaUtilities.INHABILITADO);
                        ejbFacadeDistribucion.edit(dist);
                    }
                }
            }
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BotonUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            tienda = getTienda();
            current.setTiendaId(tienda);
            getFacade().edit(current);

            //Listado de Turnos
            List<Turno> turnos = getFacadeTurno().findAll();
            //Listado de Asesores
            List<Asesor> asesores = getFacadeAsesor().findAll();

            if (current.getStatus() == JpaUtilities.INHABILITADO) {
                //Inhabilitar la Distribución para este Botón
                for (Turno turno : turnos) {
                    for (Asesor asesor : asesores) {
                        List<Distribucion> lista = ejbFacadeDistribucionExt.findDistribucionList(asesor, turno, current);
                        for (Distribucion dist : lista) {
                            dist.setStatus(JpaUtilities.INHABILITADO);
                            ejbFacadeDistribucion.edit(dist);
                        }
                    }
                }
            } else {
                //Habilito la Distribución para este Botón
                for (Turno turno : turnos) {
                    for (Asesor asesor : asesores) {
                        List<Distribucion> lista = ejbFacadeDistribucionExt.findDistribucionList(asesor, turno, current);
                        for (Distribucion dist : lista) {
                            dist.setStatus(JpaUtilities.HABILITADO);
                            ejbFacadeDistribucion.edit(dist);
                        }
                    }
                }
            }

            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BotonUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Boton) getItems().getRowData();
        // selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        //if (selectedItemIndex >= 0) {
        //   return "View";
        // } else {
        // all items were removed - go back to list
        recreateModel();
        return "List";
        //  }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("BotonDeleted"));
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

    @FacesConverter(forClass = Boton.class)
    public static class BotonControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            BotonController controller = (BotonController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "botonController");
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
            if (object instanceof Boton) {
                Boton o = (Boton) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Boton.class.getName());
            }
        }
    }
}
