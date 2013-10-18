/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.encuesta;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;

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
}
