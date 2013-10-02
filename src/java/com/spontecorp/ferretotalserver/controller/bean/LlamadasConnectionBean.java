/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionLlamadas;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.naming.NamingException;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "llamadasConnectionBean")
@SessionScoped
public class LlamadasConnectionBean implements Serializable {

    @EJB
    private HttpURLConnectionLlamadas httpURLConnection;
    @EJB
    private TiendaFacade tiendaFacade;
    private int tiendaId;
    private Tienda tienda;
    private List<Tienda> listTienda;
    private List<String> selectedTiendas;
    private List<String> selectedAllTiendas;
    private Map<String, String> tiendas;
    private Map<String, String> allTiendas;
    private Date fechaInicio;
    private Date fechaFin;
    protected boolean showTable = false;
    private List<Llamada> listaLlamadas;
    private List<List<Llamada>> totalListLlamadas;
    private String hostname;

    public LlamadasConnectionBean() {
    }

    /**
     * Consultar Lista de Llamadas por Tienda
     *
     * @param actionEvent
     * @throws Exception
     */
    public void obtenerListLlamadas(ActionEvent actionEvent) {
        //Hago null las Listas de Llamadas
        recreateModel();
        //Obtengo las Tiendas Seleccionadas
        getSelectedAllTiendas();
        getSelectedTiendas();
        //Verifico las fechas
        getFechasVacias();

        listaLlamadas = new ArrayList<>();
        totalListLlamadas = new ArrayList<>();
        showTable = true;
        List<Tienda> listTiendaFinal = new ArrayList<>();

        //Si se selecciona el check todas las Tiendas
        if (getSelectedAllTiendas().size() > 0) {
            for (int i = 0; i < listTienda.size(); i++) {
                listTiendaFinal.add(listTienda.get(i));
            }
            //Si se selecciona el check de cada Tienda
        } else if (selectedTiendas.size() > 0) {
            for (int i = 0; i < selectedTiendas.size(); i++) {
                int idTiendaSelected = Integer.parseInt(selectedTiendas.get(i));
                tienda = tiendaFacade.find(idTiendaSelected);
                listTiendaFinal.add(tienda);
            }
            //Si no se selecciona el check de ninguna Tienda ni el check de Todas las Tiendas
        } else {
            JsfUtil.addErrorMessage("Seleccione la Tienda que desea consultar.");
        }

        if (listTiendaFinal.size() > 0) {
            for (int i = 0; i < listTiendaFinal.size(); i++) {
                String url = null;
                List<Llamada> llamadasTienda = new ArrayList<>();

                //Busco la Tienda seleccionada
                tienda = listTiendaFinal.get(i);

                if (tienda != null) {
                    //Inicializo el Status de Conexión en False
                    //Con esto se verifica la Conexión a la Tienda
                    httpURLConnection.setStatusConnection(false);
                    //Seteo el id de la Sucursal (Tienda)
                    tiendaId = tienda.getSucursal();
                    //Seteo el URL de la Tienda
                    hostname = tienda.getUrl();

                    if (!hostname.equals("") && hostname != null) {
                        //Construyo el URL para conectarme al WS
                        url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.UPDATE_TIENDA_X_FECHA + "/"
                                + getFormatedFechaInicio() + "/" + getFormatedFechaFin() + "/" + tiendaId;
                        //Llamo al método del WS para obtener la lista de Llamadas X Tienda
                        llamadasTienda = httpURLConnection.getLlamadasWS(url);
                        //  if (!llamadasTienda.isEmpty()) {
                        totalListLlamadas.add(llamadasTienda);
                        //  }
                    } else {
                        JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: " + tienda.getNombre());
                    }
                }

                if (!httpURLConnection.getStatusConnection()) {
                    JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + tienda.getNombre());
                } else {
                    JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + tienda.getNombre() + " !");
                }
            }

            //Recorro la lista de Llamadas de cada Tienda
            for (List<Llamada> llamadas : totalListLlamadas) {
                if (llamadas != null) {
                    //Se arma una lista Total de Llamadas
                    for (Llamada lla : llamadas) {
                        listaLlamadas.add(lla);
                    }
                }
            }
            setListaLlamadas(listaLlamadas);
        }
    }

    /**
     * Reinicio la Lista de Llamadas
     */
    public void recreateModel() {
        listaLlamadas = null;
        totalListLlamadas = null;
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

    public List<String> getSelectedTiendas() {
        return selectedTiendas;
    }

    public void setSelectedTiendas(List<String> selectedTiendas) {
        this.selectedTiendas = selectedTiendas;
    }

    public List<String> getSelectedAllTiendas() {
        return selectedAllTiendas;
    }

    public void setSelectedAllTiendas(List<String> selectedAllTiendas) {
        this.selectedAllTiendas = selectedAllTiendas;
    }

    public Map<String, String> getTiendas() throws NamingException {
        List<Tienda> allTiendas = getListTienda();
        tiendas = new HashMap<String, String>();

        for (Tienda tiendaItem : allTiendas) {
            tiendas.put(tiendaItem.getNombre(), tiendaItem.getId().toString());
        }

        return tiendas;
    }

    public void setTiendas(Map<String, String> tiendas) {
        this.tiendas = tiendas;
    }

    public Map<String, String> getAllTiendas() {
        allTiendas = new HashMap<String, String>();
        allTiendas.put("Todas las Tiendas", "0");
        return allTiendas;
    }

    public void setAllTiendas(Map<String, String> allTiendas) {
        this.allTiendas = allTiendas;
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

    public List<Llamada> getListaLlamadas() {
        return listaLlamadas;
    }

    public void setListaLlamadas(List<Llamada> listaLlamadas) {
        this.listaLlamadas = listaLlamadas;
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

    public String getFormatedFechaInicio() {
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaInicio);
    }

    public String getFormatedFechaFin() {
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaFin);
    }
}
