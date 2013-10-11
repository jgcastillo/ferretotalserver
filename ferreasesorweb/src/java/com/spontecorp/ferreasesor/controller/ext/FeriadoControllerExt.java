package com.spontecorp.ferreasesor.controller.ext;

import com.spontecorp.ferreasesor.controller.FeriadoController;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "feriadoControllerExt")
@SessionScoped
public class FeriadoControllerExt extends FeriadoController implements Serializable {
    
    private String[ ] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
                            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
    
    private SelectItem[] mesesItem = {new SelectItem(0, meses[0]),
                        new SelectItem(1, meses[1]),
                        new SelectItem(2, meses[2]),
                        new SelectItem(3, meses[3]),
                        new SelectItem(4, meses[4]),
                        new SelectItem(5, meses[5]),
                        new SelectItem(6, meses[6]),
                        new SelectItem(7, meses[7]),
                        new SelectItem(8, meses[8]),
                        new SelectItem(9, meses[9]),
                        new SelectItem(10, meses[10]),
                        new SelectItem(11, meses[11])};
    
    private SelectItem[] dias = {new SelectItem(1),
        new SelectItem(2), new SelectItem(3), new SelectItem(4),
        new SelectItem(5), new SelectItem(6), new SelectItem(7),
        new SelectItem(8), new SelectItem(9), new SelectItem(10),
        new SelectItem(11), new SelectItem(12), new SelectItem(13),
        new SelectItem(16), new SelectItem(15), new SelectItem(14),
        new SelectItem(17), new SelectItem(18), new SelectItem(19),
        new SelectItem(20), new SelectItem(21), new SelectItem(22),
        new SelectItem(23), new SelectItem(24), new SelectItem(25),
        new SelectItem(26), new SelectItem(27), new SelectItem(28),
        new SelectItem(29), new SelectItem(30), new SelectItem(31)};
    
    public SelectItem[] getDias(){
        return dias;
    }
    
    public SelectItem[] getMeses(){
        return mesesItem;
    }
}
