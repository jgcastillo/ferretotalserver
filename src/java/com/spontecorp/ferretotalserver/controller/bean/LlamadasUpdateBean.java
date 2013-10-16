/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionLlamadas;
import com.spontecorp.ferretotalserver.jpa.LlamadaFacade;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@ManagedBean(name = "llamadasUpdateBean")
@SessionScoped
public class LlamadasUpdateBean implements Serializable {

    @EJB
    private HttpURLConnectionLlamadas httpURLConnection;
    @EJB
    private LlamadaFacade llamadaFacade;
    @EJB
    private TiendaFacade tiendaFacade;
    private String hostname;
    private int tiendaId;
    private int sucursalId;
    private Tienda tienda;
    private List<Tienda> listTienda;
    private Date fechaInicioLast;
    private Date horaInicioLast;
    private List<String> selectedTiendas;
    private List<String> selectedAllTiendas;
    private Map<String, String> tiendas;
    private Map<String, String> allTiendas;

    public LlamadasUpdateBean() {
    }

    /**
     * Actualización Automática 
     * Método para actualizar la lista de Llamadas
     * por Tienda a la BD del Server
     */
    public void updateLlamadasAtomatica() throws NamingException {
        //Lista de Tiendas
        for (Tienda actual : getListTienda()) {
            //Seteo el id de la Sucursal (Tienda)
            sucursalId = actual.getSucursal();
            //Seteo el URL de la Tienda
            hostname = actual.getUrl();

            if (!hostname.equals("") && hostname != null) {
                //Con el Id de la Sucursal Obtengo el Id de la Tienda
                Tienda tiendaServer = tiendaFacade.findTiendaServer(sucursalId);
                Llamada lastLlamadaTienda = llamadaFacade.findLlamadasTienda(actual);

                String url = null;
                List<Llamada> llamadasTienda = new ArrayList<>();

                if (lastLlamadaTienda == null) {
                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.TOTAL_LLAMADA_TIENDA + "/"
                            + sucursalId;
                } else {
                    //Seteo la Fecha y la Hora Inicio de la última Llamada
                    fechaInicioLast = lastLlamadaTienda.getFechaOpen();
                    horaInicioLast = lastLlamadaTienda.getHoraOpen();
                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.UPDATE_LLAMADA_FECHA + "/"
                            + getFormatedFechaInicio() + "/" + getFormatedHoraInicio() + "/" + sucursalId;
                }
                //Llamo al método del WS para obtener la lista de Llamadas X Tienda 
                llamadasTienda = httpURLConnection.getLlamadasWS(url);
                //Se insertan las Llamadas X Tienda en la BD del Server
                if (llamadasTienda != null) {
                    for (Llamada lla : llamadasTienda) {
                        lla.setTiendaId(tiendaServer);
                        llamadaFacade.create(lla);
                    }
                }
            }
        }
    }

    /**
     * Actualización solicitada por el Usuario 
     * Método para actualizar la lista de Llamadas
     * por Tienda a la BD del Server
     * @param actionEvent
     */
    public void updateLlamadasUsuario(ActionEvent actionEvent) {

        //Obtengo las Tiendas Seleccionadas
        getSelectedAllTiendas();
        getSelectedTiendas();

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
            JsfUtil.addErrorMessage("Seleccione la Tienda que desea Actualizar.");
        }

        for (Tienda actual : listTiendaFinal) {
            //Seteo el id de la Sucursal (Tienda)
            sucursalId = actual.getSucursal();
            //Seteo el URL de la Tienda
            hostname = actual.getUrl();

            //Inicializo el Status de Conexión en False
            //Con esto se verifica la Conexión a la Tienda
            httpURLConnection.setStatusConnection(false);

            if (!hostname.equals("") && hostname != null) {
                //Con el Id de la Sucursal Obtengo el Id de la Tienda
                Tienda tiendaServer = tiendaFacade.findTiendaServer(sucursalId);
                Llamada lastLlamadaTienda = llamadaFacade.findLlamadasTienda(actual);

                String url = null;
                List<Llamada> llamadasTienda = new ArrayList<>();

                if (lastLlamadaTienda == null) {
                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.TOTAL_LLAMADA_TIENDA + "/"
                            + sucursalId;
                } else {
                    //Seteo la Fecha y la Hora Inicio de la última Llamada
                    fechaInicioLast = lastLlamadaTienda.getFechaOpen();
                    horaInicioLast = lastLlamadaTienda.getHoraOpen();
                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.UPDATE_LLAMADA_FECHA + "/"
                            + getFormatedFechaInicio() + "/" + getFormatedHoraInicio() + "/" + sucursalId;
                }
                //Llamo al método del WS para obtener la lista de Llamadas X Tienda 
                llamadasTienda = httpURLConnection.getLlamadasWS(url);
                //Se insertan las Llamadas X Tienda en la BD del Server
                if (llamadasTienda != null) {
                    for (Llamada lla : llamadasTienda) {
                        lla.setTiendaId(tiendaServer);
                        llamadaFacade.create(lla);
                    }
                }
            } else {
                JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: " + actual.getNombre());
            }
            if (!httpURLConnection.getStatusConnection()) {
                JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + actual.getNombre());
            } else {
                JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + actual.getNombre() + " !");
            }
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

    public String getFormatedFechaInicio() {
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaInicioLast);
    }

    public String getFormatedHoraInicio() {
        return new SimpleDateFormat("HH:mm:ss").format(horaInicioLast);
    }
}
