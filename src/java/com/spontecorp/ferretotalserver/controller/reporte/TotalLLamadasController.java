package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.chart.BarChart;
import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@ManagedBean(name = "totalLlamadasBean")
@ViewScoped
public class TotalLLamadasController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Cantidad Total de Llamadas";
    private String nombreRango = "Id de Botón";
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

        List<Object> fechas = new ArrayList<>();
        Map<Object, List<ReporteHelper>> mapTiendaLlamadas = new HashMap<>();

        if (listTiendaFinal.size() > 0) {
            for (Tienda tiendaActual : listTiendaFinal) {

                //Recorro la lista de Tiendas Seleccionadas
                if (tiendaActual != null) {

                    //Seteo la busqueda
                    setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_TOTALES_TIENDA, tiendaActual, fechaInicio, fechaFin));

                    System.out.println("Tienda 1: " + tiendaActual.getNombre());
                    
                    
                    for (Object[] array : getResult()) {
                        ReporteHelper helper = new ReporteHelper();
                        //Armo una lista de Fechas
                        String date = sdf.format((Date) array[0]);
                        if (!fechas.contains(date)) {
                            fechas.add(date);
                        }

                        System.out.println("Tienda 2: " + ((Tienda)array[2]).getNombre() + " Dominio: " + String.valueOf(array[1]));
                        helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
                        helper.setRango(sdf.format((Date) array[0]));
                        helper.setTienda((Tienda)array[2]);

                        reporteData.add(helper);
                    }

                }
            }

            //Lleno el Mapa para agrupar por Fecha
            for (Object currentDate : fechas) {
                List<ReporteHelper> listData = new ArrayList<>();
                for (ReporteHelper reporte : reporteData) {
                    if (reporte.getRango().toString().equals(currentDate)) {
                        listData.add(reporte);
                    }
                }
                mapTiendaLlamadas.put(currentDate, listData);
            }

            //Convierto el Mapa en una lista para mostrar la vista
            for (Map.Entry<Object, List<ReporteHelper>> mapa : mapTiendaLlamadas.entrySet()) {
                ReporteServer reporteServer = new ReporteServer();
                reporteServer.setFecha(convertirFecha((String) mapa.getKey().toString()));
                reporteServer.setReporteHelper(mapa.getValue());
                listReporteServer.add(reporteServer);
            }

            //Ordeno la Lista por Fecha 
            Collections.sort(listReporteServer);

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
        }

        categoryModel = new CartesianChartModel();

        ChartSeries[] stores = new ChartSeries[listTiendaFinal.size()];
        int i = 0;
        for (Tienda store : listTiendaFinal) {
            stores[i] = new ChartSeries();
            i++;
        }

        for (ReporteServer data : listReporteServer) {
            // System.out.println("Fecha: " + sdf.format(data.getFecha()));
            int z = 0;

            //System.out.println("    Valor: " + z);

            for (ReporteHelper report : data.getReporteHelper()) {
                for (Tienda store : listTiendaFinal) {
                    //   System.out.println("        Tienda: " + z + " " + store.getNombre());
                    if (store.getNombre().equalsIgnoreCase(report.getTienda().getNombre())) {
                        stores[z].set(report.getRango().toString(), report.getDominio());
                    }
                }
                z++;
            }
        }

        int j = 0;
        for (Tienda store : listTiendaFinal) {
            stores[j].setLabel(store.getNombre());
            categoryModel.addSeries(stores[j]);
            j++;
        }

//        ChartSeries stores1 = new ChartSeries("Boleíta Center"); 
//        ChartSeries stores2 = new ChartSeries("Sambil"); 
//        ChartSeries stores3 = new ChartSeries("Bello Monte");
//        
//        stores1.set("20/06/2013", 7);  
//        stores1.set("21/06/2013", 0);  
//        stores1.set("22/06/2013", 0);  
//
//        stores2.set("20/06/2013", 13);  
//        stores2.set("21/06/2013", 22);  
//        stores2.set("22/06/2013", 17); 
//
//        stores3.set("20/06/2013", 8);  
//        stores3.set("21/06/2013", 22);  
//        stores3.set("22/06/2013", 17); 
//        
//        categoryModel.addSeries(stores1);  
//        categoryModel.addSeries(stores2);  
//        categoryModel.addSeries(stores3); 

    }
}
