package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.chart.BarChart;
import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

        //Obtengo las Tiendas Seleccionadas
        getSelectedAllTiendas();
        getSelectedTiendas();
        //Verifico las fechas
        getFechasVacias();

        List<Tienda> listTiendaFinal = new ArrayList<>();

        //Si se selecciona el check todas las Tiendas
        if (getSelectedAllTiendas().size() > 0) {
            for (int i = 0; i < listTienda.size(); i++) {
                listTiendaFinal.add(listTienda.get(i));
            }
            //Si se selecciona el check de cada Tienda
        } else if (selectedTiendas.size() > 0) {
            for (int i = 0; i < selectedTiendas.size(); i++) {
                int idTiendaSelected = Integer.parseInt(selectedTiendas.get(i));
                tienda = tiendaFacade.find(idTiendaSelected);
                listTiendaFinal.add(tienda);
            }
            //Si no se selecciona el check de ninguna Tienda ni el check de Todas las Tiendas
        } else {
            JsfUtil.addErrorMessage("Seleccione la Tienda que desea consultar.");
        }

        if (listTiendaFinal.size() > 0) {
            for (Tienda tiendaActual : listTiendaFinal) {
                //Recorro la lista de Tiendas Seleccionadas
                if (tiendaActual != null) {
                    //Seteo la busqueda
                    setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_TOTALES, tiendaActual, fechaInicio, fechaFin));
                    for (Object[] array : getResult()) {
                        ReporteHelper helper = new ReporteHelper();
                        helper.setRango(sdf.format((Date) array[0]));
                        helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
                        helper.setTienda(tiendaActual);
                        reporteData.add(helper);
                    }
                }
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
