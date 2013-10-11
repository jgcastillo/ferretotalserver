package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "tiempoAsesorBean")
@ViewScoped
public class TiempoXAsesorController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Tiempos de Atención por FerreAsesor";
    private String nombreRango = "Asesores";
    private String nombreDominio = "segundos";

    /**
     * Metodo para Generar la Tabla de Datos
     *
     * @param actionEvent
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        Asesor tmpAsesor = new Asesor();
        List<Llamada> llamadas = new ArrayList<>();
        Map<Asesor, List<Llamada>> mapFerreAsesor = new HashMap<>();

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
        setResult(facade.findLlamadas(ReporteHelper.TIEMPOS_X_FERREASESOR, fechaInicio, fechaFin));

        for (Object[] array : result) {
            Llamada llamada = (Llamada) array[0];
            Asesor asesor = (Asesor) array[1];

            if (!tmpAsesor.equals(asesor)) {
                tmpAsesor = asesor;
                llamadas = new ArrayList<>();
                llamadas.add(llamada);
            } else {
                llamadas.add(llamada);
            }
            mapFerreAsesor.put(asesor, llamadas);
        }

        for (Map.Entry<Asesor, List<Llamada>> map : mapFerreAsesor.entrySet()) {
            
            //Arreglo para manejar las Propiedades(min, promedio, max)
            Object[] datos = new Object[3];
            
            ReporteHelper helper = new ReporteHelper();

            total = 0L;
            promedio = 0.0;
            max = 0.0;
            min = 99999999999.9;
            for (Llamada call : map.getValue()) {
                time = (call.getHoraClose().getTime() - call.getHoraOpen().getTime()) / 1000;
                total += time;
                if (time > max) {
                    max = (double) time;
                }

                if (time < min) {
                    min = (double) time;
                }
            }

            promedio = total / (double) map.getValue().size();

            //Seteo el Nombre del Objeto (Asesor)
            helper.setNombreObj(map.getKey().getNombre() + " " + map.getKey().getApellido());

            //Seteo las Propiedades del Objeto (minimo, maximo, promedio)
            datos[0] = df.format(min);
            datos[1] = df.format(promedio);
            datos[2] = df.format(max);
            helper.setPropiedadObj(datos);

            reporteData.add(helper);
        }

        showTable = true;
        chartButtonDisable = false;
        chartButtonStackedDisable = false;
    }

    @Override
    public StreamedContent getChart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Metodo para Generar el Grafico en PrimeFaces
     */
    @Override
    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();
        ChartSeries min = new ChartSeries("min");
        ChartSeries prom = new ChartSeries("prom");
        ChartSeries max = new ChartSeries("max");

        for (ReporteHelper data : reporteData) {
            Object[] valor = data.getPropiedadObj();
            min.set(data.getNombreObj().toString(), Double.valueOf(valor[0].toString()));
            prom.set(data.getNombreObj().toString(), Double.valueOf(valor[1].toString()));
            max.set(data.getNombreObj().toString(), Double.valueOf(valor[2].toString()));
        }
        categoryModel.addSeries(min);
        categoryModel.addSeries(prom);
        categoryModel.addSeries(max);
    }
}
