package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
@ManagedBean(name = "tiempoTotalBean")
@ViewScoped
public class TiempoXTiendaController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Tiempos de Atención Totales";
    private String nombreRango = "Tiempos";
    private String nombreDominio = "segundos";

    /**
     * Genera la tabla de datos a presentar
     *
     * @param actionEvent
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        listReporteServer = new ArrayList<>();

        long total = 0L;
        double promedio;
        double min;
        double max;
        long time = 0L;

        Locale locale = new Locale("en", "US");
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyLocalizedPattern("###0.0");

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
                List<Llamada> llamadas = new ArrayList<>();

                //Seteo la busqueda (result)
                llamadas = facade.findLlamadasList(tiendaActual, fechaInicio, fechaFin);

                //Arreglo para manejar las Propiedades(min, promedio, max)
                Object[] datos = new Object[3];
                ReporteHelper helper = new ReporteHelper();

                if (llamadas.size() > 0) {
                    total = 0L;
                    max = 0.0;
                    min = 99999999999.9;
                    for (Llamada llamada : llamadas) {
                        time = (llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000;
                        total += time;

                        if (time > max) {
                            max = (double) time;
                        }

                        if (time < min) {
                            min = (double) time;
                        }
                    }
                    promedio = total / (double) llamadas.size();
                    
                    //Seteo las Propiedades del Objeto (minimo, maximo, promedio)
                    datos[0] = df.format(min);
                    datos[1] = df.format(promedio);
                    datos[2] = df.format(max);
                    helper.setPropiedadObj(datos);
                    helper.setNombreObj(tiendaActual.getNombre());
                } else {
                    //Seteo las Propiedades del Objeto en Cero
                    //(minimo, maximo, promedio)
                    datos[0] = df.format(0);
                    datos[1] = df.format(0.0);
                    datos[2] = df.format(0);
                    helper.setPropiedadObj(datos);
                    helper.setNombreObj(tiendaActual.getNombre());
                }
                reporteData.add(helper);
            }
        }

        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);

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
        ChartSeries min = new ChartSeries("min");
        ChartSeries prom = new ChartSeries("prom");
        ChartSeries max = new ChartSeries("max");

        for (ReporteHelper data : reporteData) {
            Object[] valor = data.getPropiedadObj();
            min.set(data.getNombreObj().toString(), Double.valueOf((valor[0].toString())));
            prom.set(data.getNombreObj().toString(), Double.valueOf(valor[1].toString()));
            max.set(data.getNombreObj().toString(), Double.valueOf(valor[2].toString()));
        }

        categoryModel.addSeries(min);
        categoryModel.addSeries(prom);
        categoryModel.addSeries(max);

    }
}
