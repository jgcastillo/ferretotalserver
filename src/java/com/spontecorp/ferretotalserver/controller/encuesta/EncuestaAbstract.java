/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.encuesta;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionEncuestas;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author sponte03
 */
public class EncuestaAbstract {

    @EJB
    protected TiendaFacade tiendaFacade;
    protected Tienda tienda;
    protected int tiendaId;
    protected List<Tienda> listTienda;
    protected List<String> selectedTiendas;
    protected List<String> selectedAllTiendas;
    protected Map<String, Integer> tiendas;
    protected Map<String, String> allTiendas;
    private String hostname;
    private Date fechaInicio;
    private Date fechaFin;

    /**
     * Obtengo la Lista de Tiendas seleccionadas
     *
     * @return
     */
    public List<Tienda> obtenerListTiendaSeleccionadas() {

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
        }

        //Ordeno la Lista de Tiendas por Orden alfabético (Asc.)
        if (listTiendaFinal.size() > 0) {
            Collections.sort(listTiendaFinal, new Comparator<Tienda>() {
                @Override
                public int compare(Tienda f1, Tienda f2) {
                    return f1.getNombre().compareTo(f2.getNombre());
                }
            });
        }

        return listTiendaFinal;

    }

    /**
     * Me conecto al WS para enviar la Encuesta a las Tienda(s) Seleccionada(s)
     *
     * @param listTiendaFinal
     * @param current
     */
    public void enviarEncuestaTiendaSeleccionadas(List<Tienda> listTiendaFinal, Encuesta current) {
        try {
            InitialContext context = new InitialContext();
            HttpURLConnectionEncuestas httpURLConnection = (HttpURLConnectionEncuestas) context.lookup("java:module/HttpURLConnectionEncuestas");

            for (Tienda tiendaActual : listTiendaFinal) {
                String url = null;
                //Msg generado en el Response del WS
                String msg = "";

                //Seteo el URL de la Tienda
                hostname = tiendaActual.getUrl();

                //Inicializo el Status de Conexión en False
                //Con esto se verifica la Conexión a la Tienda
                httpURLConnection.setStatusConnection(false);

                if (!hostname.equals("") && hostname != null) {

                    //Construyo el URL
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.ENVIAR_ENCUESTA;
                    setFechaInicio(current.getFechaInicio());
                    setFechaFin(current.getFechaFin());
                    current.setFechaInicioString(getFormatedFechaInicio());
                    current.setFechaFinString(getFormatedFechaFin());

                    //Llamo al método del WS para enviar la Encuesta
                    //a la Tienda seleccionada
                    msg = httpURLConnection.sendSurveyWS(url, current);

                } else {
                    JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: "
                            + tiendaActual.getNombre() + " Problemas al Conectarse.");
                }
                if (!httpURLConnection.getStatusConnection()) {
                    JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + tiendaActual.getNombre()
                            + ". La Encuesta no se ha enviado.");
                } else {
                    JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + tiendaActual.getNombre()
                            + ". "+msg);
                }
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EncuestaAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Me conecto al WS para obtener los Resultados de la Encuesta 
     * de las Tienda(s) Seleccionada(s)
     * @param listTiendaFinal
     * @param current 
     */
    public void obtenerResultadosTiendaSeleccionadas(List<Tienda> listTiendaFinal, Encuesta current) {
        try {
            InitialContext context = new InitialContext();
            HttpURLConnectionEncuestas httpURLConnection = (HttpURLConnectionEncuestas) context.lookup("java:module/HttpURLConnectionEncuestas");

            for (Tienda tiendaActual : listTiendaFinal) {
                String url = null;
                int total = 0;

                //Seteo el URL de la Tienda
                hostname = tiendaActual.getUrl();

                //Inicializo el Status de Conexión en False
                //Con esto se verifica la Conexión a la Tienda
                httpURLConnection.setStatusConnection(false);

                if (!hostname.equals("") && hostname != null) {

                    //Construyo el URL
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.OBTENER_RESULTADOS_ENCUESTA + "/" + current.getId();

                    //Llamo al método del WS para obtener Resultados de la Encuesta
                    //de la Tienda seleccionada
                   total = httpURLConnection.getResultsSurveyWS(url, current, tiendaActual);

                } else {
                    JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: "
                            + tiendaActual.getNombre() + " Problemas al Conectarse.");
                }
                if (!httpURLConnection.getStatusConnection()) {
                    JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + tiendaActual.getNombre()
                            + ". No se han obtenido los Resultados de la Encuesta.");
                } else {
                    if(total > 0){
                        JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + tiendaActual.getNombre()
                            + ". Se obtuvieron " + total +  " resultados con éxito!");
                    }else{
                        JsfUtil.addSuccessMessage("Conexión exitosa con Tienda: " + tiendaActual.getNombre()
                            + ". No se encontraron Resultados para esta Encuesta.");
                    }
                }
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaAbstract.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(EncuestaAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public List<Tienda> getListTienda() {
        listTienda = null;
        if (listTienda == null) {
            listTienda = tiendaFacade.findAll();
        }
        return listTienda;
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

    public Map<String, Integer> getTiendas() {
        List<Tienda> allTiendas = getListTienda();
        tiendas = new HashMap<String, Integer>();

        for (Tienda tiendaItem : allTiendas) {
            tiendas.put(tiendaItem.getNombre(), tiendaItem.getId());
        }

        return tiendas;
    }

    public void setTiendas(Map<String, Integer> tiendas) {
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

    public void setListTienda(List<Tienda> listTienda) {
        this.listTienda = listTienda;
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

    public String getFormatedFechaInicio() {
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaInicio);
    }

    public String getFormatedFechaFin() {
        return new SimpleDateFormat("dd-MM-yyyy").format(fechaFin);
    }
}
