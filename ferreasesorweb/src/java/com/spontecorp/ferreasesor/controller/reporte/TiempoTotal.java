package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
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
public class TiempoTotal extends LlamadaReporteAbstract implements Serializable {

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
        List<Llamada> llamadas = new ArrayList<>();

        long total = 0L;
        double promedio;
        double min;
        double max;
        long time = 0L;

        Locale locale = new Locale("en", "US");
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyLocalizedPattern("###0.0");

        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);

        //Seteo la busqueda (result)
        llamadas = facade.findLlamadasList(fechaInicio, fechaFin);

        //Arreglo para manejar las Propiedades(min, promedio, max)
        Object[] datos = new Object[3];

        ReporteHelper helper = new ReporteHelper();

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
        
        llamada.set("min.", Double.valueOf(valor[0].toString()));
        llamada.set("prom.", Double.valueOf(valor[1].toString()));
        llamada.set("max.", Double.valueOf(valor[2].toString()));

        categoryModel.addSeries(llamada);

    }
}
