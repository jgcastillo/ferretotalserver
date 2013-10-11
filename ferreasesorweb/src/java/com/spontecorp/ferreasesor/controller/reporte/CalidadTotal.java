package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Tiempo;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "calidadTotalBean")
@ViewScoped
public class CalidadTotal extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Calidad de Atención en la Tienda";
    private String nombreRango = "Calidad";
    private String nombreDominio = "Cantidad";
    private List<Llamada> llamadas;

    /**
     * Genera la tabla de datos a presentar
     *
     * @param actionEvent
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        long atencion;
        int atencionBuena = 0;
        int atencionRegular = 0;
        int cierreAutomatico = 0;

        int cantBuenas = 0;
        int cantRegulares = 0;
        int cantMalas = 0;
        int cantCerradasAut = 0;

        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);
        //Seteo la busqueda
        llamadas = facade.findLlamadasList(fechaInicio, fechaFin);
        Object[] datos = new Object[4];

        for (Llamada llamada : llamadas) {
            int turnoId = llamada.getDistribucionId().getTurnoId();
            Turno turno = turnoFacade.find(turnoId);
            //List<Tiempo> tiempos = turno.getTiempoList();
            atencionBuena = llamada.getTiempoId().getAtencionBuena();
            atencionRegular = llamada.getTiempoId().getAtencionRegular();
            cierreAutomatico = llamada.getTiempoId().getCerrarLlamada();

//            for (Tiempo tiempo : tiempos) {
//                if (tiempo.getTurnoId().equals(turno)) {
//                    atencionBuena = tiempo.getAtencionBuena();
//                    atencionRegular = tiempo.getAtencionRegular();
//                    cierreAutomatico = tiempo.getCerrarLlamada();
//                    break;
//                }
//            }
            atencion = (llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000;

            if (atencion <= atencionBuena) {
                cantBuenas++;
            } else if (atencion > atencionBuena && atencion <= atencionRegular) {
                cantRegulares++;
            } else if (atencion > atencionRegular && atencion < cierreAutomatico) {
                cantMalas++;
            } else if (atencion >= cierreAutomatico) {
                cantCerradasAut++;
            }
        }

        datos[0] = cantBuenas;
        datos[1] = cantRegulares;
        datos[2] = cantMalas;
        datos[3] = cantCerradasAut;

        ReporteHelper helper = new ReporteHelper();
        helper.setPropiedadObj(datos);
        helper.setNombreObj("");
        reporteData.add(helper);

        showTable = true;
        chartButtonDisable = false;
        chartButtonStackedDisable = false;
    }

    @Override
    public StreamedContent getChart() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();

        ChartSeries llamada = new ChartSeries("Llamadas");

        ReporteHelper helper = reporteData.get(0);
        Object[] valor = helper.getPropiedadObj();

        llamada.set("buenas", Double.valueOf(valor[0].toString()));
        llamada.set("regulares", Double.valueOf(valor[1].toString()));
        llamada.set("malas", Double.valueOf(valor[2].toString()));
        llamada.set("automáticas", Double.valueOf(valor[3].toString()));

        categoryModel.addSeries(llamada);
    }
}
