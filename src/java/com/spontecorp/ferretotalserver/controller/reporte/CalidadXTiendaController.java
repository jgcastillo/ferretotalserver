package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.chart.BarChart;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "calidadXTiendaBean")
@ViewScoped
public class CalidadXTiendaController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Calidad Total de Llamadas por Tienda";
    private String nombreRango = "Tienda";
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

        if (listTiendaFinal.size() > 0) {
            //Recorro la lista de Tiendas Seleccionadas
            for (Tienda tiendaActual : listTiendaFinal) {

                //Busco la lista de Llamadas para la Tienda seleccionada
                setResult(facade.findLlamadas(ReporteHelper.CALIDAD_TOTAL_TIENDA, tiendaActual, fechaInicio, fechaFin));

                boolean buena = false;
                boolean regular = false;
                boolean mala = false;
                boolean automatico = false;

                //Arreglo para manejar las Propiedades(buenas, regulares, malas, cierre automatico)
                Object datos[] = new Object[4];
                ReporteHelper helper = new ReporteHelper();

                //Seteo el Nombre del Objeto (Tienda)
                helper.setNombreObj(tiendaActual.getNombre());

                for (Object[] array : result) {
                    String calidad = (String) array[0];
                    String total = array[1].toString();
                    Tienda store = (Tienda) array[2];

                    //Seteo las Propiedades del Objeto (buenas, regulares, malas, cierre automatico)
                    if (calidad.equalsIgnoreCase(JpaUtilities.ATENCION_BUENA)) {
                        datos[0] = total;
                        buena = true;
                    } else if (calidad.equalsIgnoreCase(JpaUtilities.ATENCION_REGULAR)) {
                        datos[1] = total;
                        regular = true;
                    } else if (calidad.equalsIgnoreCase(JpaUtilities.ATENCION_MALA)) {
                        datos[2] = total;
                        mala = true;
                    } else if (calidad.equalsIgnoreCase(JpaUtilities.CIERRE_AUTOMATICO)) {
                        datos[3] = total;
                        automatico = true;
                    }
                }

                //Si no hay información completo con 0
                if (!buena) {
                    datos[0] = 0;
                }
                if (!regular) {
                    datos[1] = 0;
                }
                if (!mala) {
                    datos[2] = 0;
                }
                if (!automatico) {
                    datos[3] = 0;
                }

                helper.setPropiedadObj(datos);
                reporteData.add(helper);

            }

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

        categoryModel = new CartesianChartModel();
        ChartSeries buenas = new ChartSeries("buenas");
        ChartSeries regulares = new ChartSeries("regulares");
        ChartSeries malas = new ChartSeries("malas");
        ChartSeries automaticas = new ChartSeries("automáticas");

        for (ReporteHelper data : reporteData) {
            Object valor[] = data.getPropiedadObj();
            buenas.set(data.getNombreObj().toString(), Double.valueOf(valor[0].toString()));
            regulares.set(data.getNombreObj().toString(), Double.valueOf(valor[1].toString()));
            malas.set(data.getNombreObj().toString(), Double.valueOf(valor[2].toString()));
            automaticas.set(data.getNombreObj().toString(), Double.valueOf(valor[3].toString()));
        }
        
        categoryModel.addSeries(buenas);
        categoryModel.addSeries(regulares);
        categoryModel.addSeries(malas);
        categoryModel.addSeries(automaticas);

    }
}
