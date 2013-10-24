package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.entity.Ubicacion;
import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.controller.util.PaginationHelper;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionUbicacion;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.jpa.UbicacionFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

@ManagedBean(name = "ubicacionController")
@SessionScoped
public class UbicacionController implements Serializable {

    private Ubicacion current;
    private DataModel items = null;
    @EJB
    private UbicacionFacade ejbFacade;
    @EJB
    private TiendaFacade tiendaFacade;
    @EJB
    private HttpURLConnectionUbicacion httpURLConnection;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private List<Tienda> listTienda;
    private String hostname;
    private int sucursalId;

    public UbicacionController() {
    }

    public Ubicacion getSelected() {
        if (current == null) {
            current = new Ubicacion();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UbicacionFacade getFacade() {
        return ejbFacade;
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
        current = (Ubicacion) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Ubicacion();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UbicacionCreated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Ubicacion) getItems().getRowData();
        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Se actualizan los Pasillos en Todas las Tiendas vía WS
     *
     * @param actionEvent
     */
    public void updatePasillosTiendas(ActionEvent actionEvent) {
        try {
            //Obtengo el Listado de Tiendas
            List<Tienda> listTiendaFinal = getListTienda();

            //Obtengo el Objeto Json de la lista de Ubicaciones
            String arrUbicacionJson = httpURLConnection.getJsonUbicacionList();

            for (Tienda actual : listTiendaFinal) {
                //Msg generado en el Response del WS
                String msg = "";
                //Seteo el URL de la Tienda
                hostname = actual.getUrl();

                //Inicializo el Status de Conexión en False
                //Con esto se verifica la Conexión a la Tienda
                httpURLConnection.setStatusConnection(false);

                if (!hostname.equals("") && hostname != null) {
                    String url = null;

                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.UPDATE_UBICACION_TIENDA;

                    try {
                        //Llamo al método del WS para Guardar la lista de Ubicaciones
                        //Configuradas en el Server 
                        msg = httpURLConnection.sendUbicacionesWS(url, arrUbicacionJson);
                    } catch (Exception ex) {
                        Logger.getLogger(UbicacionController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: " + actual.getNombre());
                }
                if (!httpURLConnection.getStatusConnection()) {
                    JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + actual.getNombre()
                            + ". La Configuración de Pasillos no fue exitosa.");
                } else {
                    JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + actual.getNombre()
                            + ". " + msg);
                }
            }

        } catch (NamingException ex) {
            Logger.getLogger(UbicacionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se obtiene la lista de Tiendas
     *
     * @return
     */
    public List<Tienda> getListTienda() throws NamingException {
        listTienda = null;
        if (listTienda == null) {
            listTienda = tiendaFacade.findAll();
        }
        return listTienda;
    }

    public void setListTienda(List<Tienda> listTienda) {
        this.listTienda = listTienda;
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UbicacionUpdated"));
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Ubicacion) getItems().getRowData();
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
            return "List";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    public String prepareDelete() {
        getFacade().remove(current);
        JsfUtil.addSuccessMessage("Ubicación eliminada con éxito!");
        recreateModel();
        return prepareList();
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UbicacionDeleted"));
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

    @FacesConverter(forClass = Ubicacion.class)
    public static class UbicacionControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UbicacionController controller = (UbicacionController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ubicacionController");
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
            if (object instanceof Ubicacion) {
                Ubicacion o = (Ubicacion) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Ubicacion.class.getName());
            }
        }
    }
}
