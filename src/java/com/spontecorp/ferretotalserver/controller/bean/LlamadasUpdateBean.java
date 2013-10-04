/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionLlamadas;
import com.spontecorp.ferretotalserver.jpa.LlamadaFacade;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.InitialContext;
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

    public LlamadasUpdateBean() {
    }

    /**
     *
     */
    public void updateLlamadas() throws NamingException {
        System.out.println("Actualizando lista de llamadas: ");

        //Lista de Tiendas
        for (Tienda actual : getListTienda()) {
            System.out.println("Tienda: " + actual.getNombre());
            //Seteo el id de la Sucursal (Tienda)
            sucursalId = actual.getSucursal();
            //Seteo el URL de la Tienda
            hostname = actual.getUrl();
            //Con el Id de la Sucursal Obtengo el Id de la Tienda
            Tienda tiendaServer = tiendaFacade.findTiendaServer(sucursalId);
            
            Llamada lastLlamadaTienda = llamadaFacade.findLlamadasTienda(actual);

            if (lastLlamadaTienda == null) {
                String url = null;
                List<Llamada> llamadasTienda = new ArrayList<>();

                if (!hostname.equals("") && hostname != null) {
                    //Construyo el URL para conectarme al WS
                    url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.TOTAL_LLAMADA_TIENDA + "/"
                            + sucursalId;
                    //Llamo al método del WS para obtener la lista de Llamadas X Tienda
                    llamadasTienda = httpURLConnection.getLlamadasWS(url);

                    if (llamadasTienda != null) {
                        System.out.println("Total Llamadas encontradas en la Tienda: " + llamadasTienda.size());
                        for (Llamada lla : llamadasTienda) {
                            lla.setTiendaId(tiendaServer);
                            llamadaFacade.create(lla);
                        }
                    }
                }
            } else {
                System.out.println("Ya hay llamadas en la Tienda: " + actual.getNombre());
                System.out.println("Ultima llamada encontrada: " + lastLlamadaTienda);
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
}
