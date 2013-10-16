package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.chart.BarChart;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "llamadasXasesorBean")
@ViewScoped
public class LlamadasXAsesorController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Cantidad de Llamadas por Persona";
    private String nombreRango = "FerreAsesor";
    private String nombreDominio = "Cantidad";

    /**
     * Metodo para Generar la Tabla de Datos
     *
     * @param actionEvent
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        listReporteServer = new ArrayList<>();
        listTiendaXAsesor = new ArrayList<>();

        //Obtengo las Tiendas Seleccionadas
        getSelectedAllTiendas();
        getSelectedTiendas();
        //Verifico las fechas
        getFechasVacias();

        //Obtengo la Lista de Tiendas seleccionadas
        List<Tienda> listTiendaFinal = obtenerListTiendaSeleccionadas();
        //Lista de FerreAsesores
        List<Object> ferreasesores = new ArrayList<>();

        if (listTiendaFinal.size() > 0) {
            //Recorro la lista de Tiendas Seleccionadas
            for (Tienda tiendaActual : listTiendaFinal) {
                //Seteo la busqueda
                setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_ASESOR_TIENDA, tiendaActual, fechaInicio, fechaFin));
                List<ReporteHelper> reporteDataTienda = new ArrayList<>();
                for (Object[] array : result) {
                    ReporteHelper helper = new ReporteHelper();
                    //Armo una lista de FerreAsesores
                    String asesor = (String) array[0];
                    if (!ferreasesores.contains(asesor)) {
                        ferreasesores.add(asesor);
                    }
                    helper.setRango(((String) array[0]));
                    helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
                    helper.setTienda((Tienda) array[2]);
                    reporteDataTienda.add(helper);
                }
                if(result.isEmpty()){
                    ReporteHelper helper = new ReporteHelper();
                    helper.setRango(("No hay FerreAsesores disponibles para la fecha."));
                    helper.setDominio(0);
                    helper.setTienda(tiendaActual);
                    reporteDataTienda.add(helper);
                }
                tiendaActual.setReporteHelperList(reporteDataTienda);
                listTiendaXAsesor.add(tiendaActual);
            }

            //Seteo los Datos del Reporte
            setNombreReporte(nombreReporte);
            setNombreRango(nombreRango);
            setNombreDominio(nombreDominio);

            showTable = true;

        }
    }

    @Override
    public StreamedContent getChart() {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        BarChart barChart = new BarChart(nombreReporte, nombreRango, nombreDominio);
        barChart.setResult(getResult());
        barChart.createDataset();
        return barChart.getBarChart();
    }

    @Override
    public void createCategoryModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Metodo para Generar el Grafico en PrimeFaces
     */
    
}