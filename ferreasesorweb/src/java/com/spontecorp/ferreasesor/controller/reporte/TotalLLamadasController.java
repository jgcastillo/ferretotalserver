package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.controller.chart.BarChart;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
@ManagedBean (name = "totalLlamadasBean")
@ViewScoped
public class TotalLLamadasController extends LlamadaReporteAbstract implements Serializable{
    
    private String nombreReporte = "Cantidad Total de Llamadas";
    private String nombreRango = "Id de Botón";
    private String nombreDominio = "Cantidad";
    
    /**
     * Metodo para Generar la Tabla de Datos
     * @param actionEvent 
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent){
        
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        
        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);
        //Seteo la busqueda
        setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_TOTALES, fechaInicio, fechaFin));
        
        for(Object[] array : getResult()){
            ReporteHelper helper = new ReporteHelper();
            helper.setRango(sdf.format((Date)array[0]));
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            reporteData.add(helper);
        }
        
        showTable = true;
        chartButtonDisable = false;
    }
    
    @Override
    public StreamedContent getChart(){
        LlamadaFacadeExt facade = new LlamadaFacadeExt();
        //result = facade.findLlamadas(ReporteHelper.LLAMADAS_TOTALES, fechaInicio, fechaFin);
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
        categoryModelPie = new PieChartModel();
        
        ChartSeries cant = new ChartSeries("Cantidad");

        for (ReporteHelper data : reporteData) {
            cant.set(data.getRango(), data.getDominio());
            categoryModelPie.set(data.getRango().toString(), data.getDominio());
        }
        categoryModel.addSeries(cant);
        
    }

}
