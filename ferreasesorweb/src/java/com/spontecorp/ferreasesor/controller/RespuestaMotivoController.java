package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.controller.reporte.JasperBeanRecolectorInformacion;
import com.spontecorp.ferreasesor.controller.reporte.JasperManagement;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.controller.util.PaginationHelper;
import com.spontecorp.ferreasesor.entity.RespuestaMotivo;
import com.spontecorp.ferreasesor.jpa.RespuestaMotivoFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.sf.jasperreports.engine.JRException;

@ManagedBean(name = "respuestaMotivoController")
@SessionScoped
public class RespuestaMotivoController implements Serializable {

    private RespuestaMotivo current;
    private List<RespuestaMotivo> listaRespuestas;
    private DataModel items = null;
    @EJB
    private com.spontecorp.ferreasesor.jpa.RespuestaMotivoFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    protected Date fechaInicio;
    protected Date fechaFin;
    protected boolean showTable = false;

    public RespuestaMotivoController() {
    }

    public RespuestaMotivo getSelected() {
        if (current == null) {
            current = new RespuestaMotivo();
            selectedItemIndex = -1;
        }
        return current;
    }

    private RespuestaMotivoFacade getFacade() {
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
        current = (RespuestaMotivo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new RespuestaMotivo();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RespuestaMotivoCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Verificar si las fechas son nulas
     */
    public void getFechasVacias() {
        if (fechaInicio == null || fechaFin == null) {
            Calendar cal = new GregorianCalendar();
            fechaFin = new Date();
            cal.setTime(fechaFin);
            int mesActual = cal.get(Calendar.MONTH);
            int yearActual = cal.get(Calendar.YEAR);
            cal.set(yearActual, mesActual, 1);
            fechaInicio = new Date(cal.getTimeInMillis());
        }
    }

    public String prepareEdit() {
        current = (RespuestaMotivo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RespuestaMotivoUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (RespuestaMotivo) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("RespuestaMotivoDeleted"));
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
        recreateModel();
        if (items == null) {
            //items = getPagination().createPageDataModel();
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    /**
     * Listado de Respuestas Obtenidas en el Recolector de Información filtradas
     * por fecha
     *
     * @param actionEvent
     */
    public void showRespuestaMotivo(ActionEvent actionEvent) {
        //Verifico las fechas
        getFechasVacias();

        //Se muestra la Tabla
        showTable = true;

    }
    
    /**
     * Exportar Reportes a PDF (Respuestas de Motivos de Llamado)
     * @throws JRException
     * @throws IOException 
     */
    public void exportarReportePDFMotivos() throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportepdfMotivos.jasper");
        exportarReporteMotivos(extension, jasperFileAddress);

    }
    
     /**
     * Exportar Reportes a PDF (Respuestas de Motivos de Llamado)
     * @throws JRException
     * @throws IOException 
     */
    public void exportarReporteXLSMotivos() throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportexlsMotivos.jasper");
        exportarReporteMotivos(extension, jasperFileAddress);

    }
    
    /**
     * Método para exportar Reportes de Respuestas de Motivos de Llamado
     * 
     *
     * @param extension
     * @param jasperFileAddress
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteMotivos(String extension, String jasperFileAddress) throws JRException, IOException {

        JasperManagement jm = new JasperManagement();
        List<JasperBeanRecolectorInformacion> myList;
        String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");

        try {

            //Revisa los casos y llena la lista de jasperbean
            List<RespuestaMotivo> respList = getListaRespuestas();
            myList = jm.FillListRecolector(respList);

            String nombreReporte = "Recolector de Información";
            String nombreRango = "Motivo de Llamado";
            
            int totalRespuestas = respList.size();

            Map parametros = new HashMap();
            parametros.put("nombrereporte", nombreReporte);
            parametros.put("nombre", nombreRango);
            parametros.put("totalRespuestas", totalRespuestas);
            parametros.put("logo", logoAddress);

            jm.FillReportRecolector(parametros, myList, extension, jasperFileAddress, nombreReporte);

        } catch (JRException e) {
            Logger.getLogger(RespuestaMotivoController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public void setShowTable(boolean showTable) {
        this.showTable = showTable;
    }

    public List<RespuestaMotivo> getListaRespuestas() {
        listaRespuestas = null;

        //Listado de Respuesta Motivos filtrado por fechas
        if (listaRespuestas == null) {
            listaRespuestas = getFacade().findRespuestaMotivoList(fechaInicio, fechaFin);
        }

        return listaRespuestas;
    }

    public void setListaRespuestas(List<RespuestaMotivo> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
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

    @FacesConverter(forClass = RespuestaMotivo.class)
    public static class RespuestaMotivoControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RespuestaMotivoController controller = (RespuestaMotivoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "respuestaMotivoController");
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
            if (object instanceof RespuestaMotivo) {
                RespuestaMotivo o = (RespuestaMotivo) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + RespuestaMotivo.class.getName());
            }
        }
    }
}
