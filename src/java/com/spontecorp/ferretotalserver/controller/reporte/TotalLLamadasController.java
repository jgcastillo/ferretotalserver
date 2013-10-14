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
import java.util.Comparator;
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
            //Recorro la lista de Tiendas Seleccionadas
            for (Tienda tiendaActual : listTiendaFinal) {

                //Busco la lista de Llamadas para la Tienda seleccionada
                setResult(facade.findLlamadas(ReporteHelper.LLAMADAS_TOTALES_TIENDA, tiendaActual, fechaInicio, fechaFin));

                for (Object[] array : getResult()) {
                    ReporteHelper helper = new ReporteHelper();
                    //Armo una lista de Fechas
                    String date = sdf.format((Date) array[0]);
                    if (!fechas.contains(date)) {
                        fechas.add(date);
                    }
                    helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
                    helper.setRango(sdf.format((Date) array[0]));
                    helper.setTienda((Tienda) array[2]);

                    reporteData.add(helper);
                }
                
            }

            //Lleno el Mapa para agrupar por Fecha
            for (Object currentDate : fechas) {
                List<ReporteHelper> listData = new ArrayList<>();
                List<Tienda> listTiendaNew = new ArrayList<>();
                List<Tienda> listTiendaNew2 = new ArrayList<>();
                for (ReporteHelper reporte : reporteData) {
                    List<Tienda> totalTiendas = listTiendaFinal;
                    if (reporte.getRango().toString().equals(currentDate)) {
                        for (Tienda tiendaActual : totalTiendas) {
                            if (tiendaActual.getId() == reporte.getTienda().getId()) {
                                listData.add(reporte);
                                listTiendaNew2.add(tiendaActual);
                                if (listTiendaNew.contains(tiendaActual)) {
                                    listTiendaNew.remove(tiendaActual);
                                }
                            } else {
                                if (!listTiendaNew.contains(tiendaActual) && !listTiendaNew2.contains(tiendaActual)) {
                                    listTiendaNew.add(tiendaActual);
                                }
                            }
                        }
                    }
                }

                //Lleno con cero(0) la cantidad de Llamadas en las fechas,
                //dónde no hubo Llamadas en la Tienda 
                for (Tienda store : listTiendaNew) {
                    ReporteHelper helper = new ReporteHelper();
                    helper.setRango(currentDate);
                    helper.setTienda(store);
                    helper.setDominio(0);
                    listData.add(helper);
                }

                //Ordeno la Lista de Tiendas por Orden alfabético (Asc.)
                Collections.sort(listData, new Comparator<ReporteHelper>() {
                    @Override
                    public int compare(ReporteHelper f1, ReporteHelper f2) {
                        return f1.getTienda().getNombre().toString().compareTo(f2.getTienda().getNombre().toString());
                    }
                });

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

        //Ordeno la Lista de Tiendas por Orden alfabético (Asc.)
        Collections.sort(listTiendaFinal, new Comparator<Tienda>() {
            @Override
            public int compare(Tienda f1, Tienda f2) {
                return f1.getNombre().toString().compareTo(f2.getNombre().toString());
            }
        });

        categoryModel = new CartesianChartModel();

        ChartSeries[] stores = new ChartSeries[listTiendaFinal.size()];
        int i = 0;
        for (Tienda store : listTiendaFinal) {
            stores[i] = new ChartSeries();
            i++;
        }

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
        for (Tienda store : listTiendaFinal) {
            stores[j].setLabel(store.getNombre());
            categoryModel.addSeries(stores[j]);
            j++;
        }

    }
}
