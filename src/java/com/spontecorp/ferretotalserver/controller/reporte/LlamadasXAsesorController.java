package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.chart.BarChart;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
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
                    reporteData.add(helper);
                }
            }

            //Se obtiene la lista de Datos procesados
            listReporteServer = procesoDatos(listTiendaFinal, ferreasesores,
                    reporteData, JpaUtilities.REPORTE_POR_FERREASESOR);

            //Seteo los Datos del Reporte
            setNombreReporte(nombreReporte);
            setNombreRango(nombreRango);
            setNombreDominio(nombreDominio);

            showTable = true;
            chartButtonDisable = false;
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

    /**
     * Metodo para Generar el Grafico en PrimeFaces
     */
    @Override
    public void createCategoryModel() {
        //Obtengo la Lista de Tiendas seleccionadas
        List<Tienda> listTiendaFinal = obtenerListTiendaSeleccionadas();

        categoryModel = new CartesianChartModel();

        ChartSeries[] stores = new ChartSeries[listTiendaFinal.size()];
        int i = 0;
        for (Tienda store : listTiendaFinal) {
            stores[i] = new ChartSeries();
            i++;
        }

        //Se recorre la Lista de Datos
        for (ReporteServer data : listReporteServer) {
            int z = 0;
            for (ReporteHelper report : data.getReporteHelper()) {
                for (Tienda store : listTiendaFinal) {
                    if (store.getNombre().equalsIgnoreCase(report.getTienda().getNombre())) {
                        stores[z].set(report.getRango().toString(), report.getDominio());
                    }
                }
                z++;
            }
        }

        int j = 0;
        //Se agregan las Series al CategoryModel
        for (Tienda store : listTiendaFinal) {
            stores[j].setLabel(store.getNombre());
            categoryModel.addSeries(stores[j]);
            j++;
        }
    }
}