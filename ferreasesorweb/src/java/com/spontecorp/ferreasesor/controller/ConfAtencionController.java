package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Distribucion;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "atencionController")
@SessionScoped
public final class ConfAtencionController implements Serializable{

    @EJB
    private com.spontecorp.ferreasesor.jpa.DistribucionFacade ejbFacade;
    private List<Distribucion> items;
    private Distribucion selectedItem;
    private Distribucion[] selectedItems;
    private DistDataModel dataModel;

    public ConfAtencionController() {
        dataModel = new DistDataModel(getItems());
    }

    public List<Distribucion> getItems() {
        return ejbFacade.findAll();
    }

    public Distribucion getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Distribucion selected) {
        this.selectedItem = selected;
    }

    public Distribucion[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Distribucion[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public DistDataModel getDataModel() {
        return dataModel;
    }
    
    public class DistDataModel extends ListDataModel<Distribucion> implements SelectableDataModel {

        public DistDataModel() {
        }

        public DistDataModel(List<Distribucion> list) {
            super(list);
        }

        @Override
        public Distribucion getRowData(String string) {
            List<Distribucion> distrs = (List<Distribucion>) getWrappedData();
            for (Distribucion dist : distrs) {
                if (dist.getAsesorId() == Integer.parseInt(string)) {
                    return dist;
                }
            }
            return null;
        }

        @Override
        public Object getRowKey(Object t) {
            if(t instanceof Distribucion){
                return ((Distribucion)t).getId();
            } else {
                return null;
            }
        }
    }
}
