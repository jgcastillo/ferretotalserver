package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author jgcastillo
 */
public abstract class LlamadaReporteAbstract {

    LlamadaFacadeExt facade = new LlamadaFacadeExt();
    @EJB
    TiendaFacade tiendaFacade;
    protected Date fechaInicio;
    protected Date fechaFin;
    protected Tienda tienda;
    protected List<Tienda> listTienda;
    protected List<Tienda> listTiendaXAsesor;
    protected List<Llamada> totalLlamadas;
    protected List<String> selectedTiendas;
    protected List<String> selectedAllTiendas;
    protected Map<String, Integer> tiendas;
    private Map<String, String> allTiendas;
    protected boolean showTable = false;
    protected boolean showChart = false;
    protected boolean showStackedChart = false;
    protected boolean chartButtonDisable = true;
    protected boolean chartButtonStackedDisable = true;
    protected List<Object[]> result;
    protected List<Llamada> resultLlamadas;
    protected List<ReporteHelper> reporteData;
    protected List<ReporteServer> listReporteServer;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected CartesianChartModel categoryModel;
    protected PieChartModel categoryModelPie;
    private String nombreRango;
    private String nombreDominio;
    private int reporte;
    private String nombreReporte;
    private boolean promedios;
    private double promedioDiario;
    private Long totalCalls;
    private Long diasEntreFechas;
    private boolean tiempos;

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Tienda getTienda() {
        return tienda;
    }

    public void setTienda(Tienda tienda) {
        this.tienda = tienda;
    }

    public List<Tienda> getListTienda() {
        listTienda = null;
        if (listTienda == null) {
            listTienda = tiendaFacade.findAll();
        }
        return listTienda;
    }

    public List<String> getSelectedTiendas() {
        return selectedTiendas;
    }

    public void setSelectedTiendas(List<String> selectedTiendas) {
        this.selectedTiendas = selectedTiendas;
    }

    public List<String> getSelectedAllTiendas() {
        return selectedAllTiendas;
    }

    public void setSelectedAllTiendas(List<String> selectedAllTiendas) {
        this.selectedAllTiendas = selectedAllTiendas;
    }

    public Map<String, Integer> getTiendas() {
        List<Tienda> allTiendas = getListTienda();
        tiendas = new HashMap<String, Integer>();

        for (Tienda tiendaItem : allTiendas) {
            tiendas.put(tiendaItem.getNombre(), tiendaItem.getId());
        }

        return tiendas;
    }

    public void setTiendas(Map<String, Integer> tiendas) {
        this.tiendas = tiendas;
    }

    public Map<String, String> getAllTiendas() {
        allTiendas = new HashMap<String, String>();
        allTiendas.put("Todas las Tiendas", "0");
        return allTiendas;
    }

    public void setAllTiendas(Map<String, String> allTiendas) {
        this.allTiendas = allTiendas;
    }

    public void setListTienda(List<Tienda> listTienda) {
        this.listTienda = listTienda;
    }

    public List<Llamada> getTotalLlamadas() {
        return totalLlamadas;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public boolean isShowStackedChart() {
        return showStackedChart;
    }

    public boolean isChartButtonDisable() {
        return chartButtonDisable;
    }

    public boolean isChartButtonStackedDisable() {
        return chartButtonStackedDisable;
    }

    public List<ReporteHelper> getReporteData() {
        return reporteData;
    }

    public List<ReporteServer> getListReporteServer() {
        return listReporteServer;
    }

    public void setListReporteServer(List<ReporteServer> listReporteServer) {
        this.listReporteServer = listReporteServer;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public List<Object[]> getResult() {
        return result;
    }

    public void setResult(List<Object[]> result) {
        this.result = result;
    }

    public List<Llamada> getResultLlamadas() {
        return resultLlamadas;
    }

    public void setResultLlamadas(List<Llamada> resultLlamadas) {
        this.resultLlamadas = resultLlamadas;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getNombreRango() {
        return nombreRango;
    }

    public void setNombreRango(String nombreRango) {
        this.nombreRango = nombreRango;
    }

    public String getNombreDominio() {
        return nombreDominio;
    }

    public void setNombreDominio(String nombreDominio) {
        this.nombreDominio = nombreDominio;
    }

    public boolean isPromedios() {
        return promedios;
    }

    public void setPromedios(boolean promedios) {
        this.promedios = promedios;
    }

    public double getPromedioDiario() {
        return promedioDiario;
    }

    public void setPromedioDiario(double promedioDiario) {
        this.promedioDiario = promedioDiario;
    }

    public boolean isTiempos() {
        return tiempos;
    }

    public void setTiempos(boolean tiempos) {
        this.tiempos = tiempos;
    }

    public Long getTotalCalls() {
        return totalCalls;
    }

    public void setTotalCalls(Long totalCalls) {
        this.totalCalls = totalCalls;
    }

    public Long getDiasEntreFechas() {
        return diasEntreFechas;
    }

    public void setDiasEntreFechas(Long diasEntreFechas) {
        this.diasEntreFechas = diasEntreFechas;
    }

    public PieChartModel getCategoryModelPie() {
        return categoryModelPie;
    }

    public void setCategoryModelPie(PieChartModel categoryModelPie) {
        this.categoryModelPie = categoryModelPie;
    }

    public List<Tienda> getListTiendaXAsesor() {
        return listTiendaXAsesor;
    }

    public void setListTiendaXAsesor(List<Tienda> listTiendaXAsesor) {
        this.listTiendaXAsesor = listTiendaXAsesor;
    }

    /**
     * Verificar si las fechas son nulas
     */
    public void getFechasVacias() {
        if (fechaInicio == null || fechaFin == null) {
            Calendar cal = new GregorianCalendar();
            fechaFin = new Date();
            cal.setTime(fechaFin);
            int mesActual = cal.get(Calendar.MONTH);
            int yearActual = cal.get(Calendar.YEAR);
            cal.set(yearActual, mesActual, 1);
            fechaInicio = new Date(cal.getTimeInMillis());
        }
    }

    /**
     * Obtengo la Lista de Tiendas seleccionadas
     *
     * @return
     */
    public List<Tienda> obtenerListTiendaSeleccionadas() {

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

        //Ordeno la Lista de Tiendas por Orden alfabético (Asc.)
        if (listTiendaFinal.size() > 0) {
            Collections.sort(listTiendaFinal, new Comparator<Tienda>() {
                @Override
                public int compare(Tienda f1, Tienda f2) {
                    return f1.getNombre().compareTo(f2.getNombre());
                }
            });
        }

        return listTiendaFinal;

    }

    /**
     * Se procesan los Datos para generar la lista de Reporte Server
     *
     * @param listTiendaFinal
     * @param dispositivos
     * @param reporteData
     * @return
     */
    public List<ReporteServer> procesoDatos(List<Tienda> listTiendaFinal, List<Object> criterio, List<ReporteHelper> reporteData, int caso) {

        Map<Object, List<ReporteHelper>> mapTiendaLlamadas = new HashMap<>();

        //Lleno el Mapa para agrupar por Criterio (Fecha, Dispositivo o Asesor)
        for (Object currentDisp : criterio) {
            List<ReporteHelper> listData = new ArrayList<>();
            List<Tienda> listaTiendaSinData = new ArrayList<>();
            List<Tienda> listaTiendaConData = new ArrayList<>();
            for (ReporteHelper reporte : reporteData) {
                List<Tienda> totalTiendas = listTiendaFinal;
                if (reporte.getRango().equals(currentDisp)) {
                    for (Tienda tiendaActual : totalTiendas) {
                        if (tiendaActual.getId() == reporte.getTienda().getId()) {
                            listData.add(reporte);
                            listaTiendaConData.add(tiendaActual);
                            if (listaTiendaSinData.contains(tiendaActual)) {
                                listaTiendaSinData.remove(tiendaActual);
                            }
                        } else {
                            if (!listaTiendaSinData.contains(tiendaActual) && !listaTiendaConData.contains(tiendaActual)) {
                                listaTiendaSinData.add(tiendaActual);
                            }
                        }
                    }
                }
            }

            //Lleno con cero(0) la cantidad de Llamadas en las Dispositivos,
            //dónde no hubo Llamadas en la Tienda para una fecha determinada
            for (Tienda store : listaTiendaSinData) {
                ReporteHelper helper = new ReporteHelper();
                helper.setRango(currentDisp);
                helper.setTienda(store);
                helper.setDominio(0);
                listData.add(helper);
            }

            //Ordeno la Lista de Tiendas por Orden alfabético (Asc.)
            Collections.sort(listData, new Comparator<ReporteHelper>() {
                @Override
                public int compare(ReporteHelper f1, ReporteHelper f2) {
                    return f1.getTienda().getNombre().compareTo(f2.getTienda().getNombre());
                }
            });

            mapTiendaLlamadas.put(currentDisp, listData);
        }

        //Si el Criterio es la Fecha 
        if (caso == JpaUtilities.REPORTE_POR_FECHA) {
            //Convierto el Mapa en una lista para mostrar la vista
            for (Map.Entry<Object, List<ReporteHelper>> mapa : mapTiendaLlamadas.entrySet()) {
                ReporteServer reporteServer = new ReporteServer();
                reporteServer.setFecha(convertirFecha((String) mapa.getKey().toString()));
                reporteServer.setReporteHelper(mapa.getValue());
                listReporteServer.add(reporteServer);
            }
            if (listReporteServer.size() > 0) {
                //Ordeno la Lista por Criterio (Fecha)
                Collections.sort(listReporteServer);
            }
            //Si el Criterio es Deispositio o Asesor
        } else {
            //Convierto el Mapa en una lista para mostrar la vista
            for (Map.Entry<Object, List<ReporteHelper>> mapa : mapTiendaLlamadas.entrySet()) {
                ReporteServer reporteServer = new ReporteServer();
                reporteServer.setCurrent(mapa.getKey().toString());
                reporteServer.setReporteHelper(mapa.getValue());
                listReporteServer.add(reporteServer);
            }
            if (listReporteServer.size() > 0) {
                //Ordeno la Lista por Criterio (Dispositivo o Asesor)
                Collections.sort(listReporteServer, new Comparator<ReporteServer>() {
                    @Override
                    public int compare(ReporteServer f1, ReporteServer f2) {
                        return f1.getCurrent().compareTo(f2.getCurrent());
                    }
                });
            }
        }

        return listReporteServer;

    }

    /**
     * Método para recibir fecha String en formato dd-MM-yyyy y retornar un
     * objeto tipo Date con la fecha dada
     *
     * @param fecha
     * @return Fecha tipo "Date"
     */
    public Date convertirFecha(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            date = formateador.parse(fecha);

        } catch (ParseException ex) {
            Logger.getLogger(TotalLLamadasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     * Metodo para Generar la Tabla de Datos
     *
     * @param actionEvent
     */
    public abstract void populateLlamadas(ActionEvent actionEvent);

    public abstract StreamedContent getChart();

    public void muestraGrafico(ActionEvent event) {
        createCategoryModel();
        showStackedChart = false;
        showChart = true;
    }

    public void muestraStackedGrafico(ActionEvent event) {
        createCategoryModel();
        showChart = false;
        showStackedChart = true;
    }

    /**
     * Metodo para Generar el Grafico en PrimeFaces
     */
    public abstract void createCategoryModel();

    /**
     * Exportar Reporte Tiempo Gráfico Barras .PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempo.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Exportar Reporte Tiempo Gráfico Stacked .PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFTiempoStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempostacked.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Exportar Reporte Tiempo .XLS
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteXLSTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlstiempo.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Exportar Reporte Calidad Gráfico Barras .PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidad.jasper");
        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Exportar Reporte Calidad Gráfico Stacked .PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFCalidadStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidadStacked.jasper");
        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Exportar Reporte Calidad .XLS
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteXLSCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlscalidad.jasper");
        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }
    
    /**
     * Método para preparar el Reporte Tiempo - Calidad
     * para las extensiones .PDF y .XLS
     * @param extension
     * @param jasperFileAddress 
     */
    public void exportarReporteTiempoCalidad(String extension, String jasperFileAddress) {
        try {
            List<JasperBeanTiempoCalidad> myList2;
            JasperManagement jm = new JasperManagement();
            String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");
            Map parametrosTiempo = new HashMap();
            String serie1;
            String serie2;
            String serie3;
            String serie4 = "";
            
            if (tiempos) {
                serie1 = "Mínimo";
                serie2 = "Promedio";
                serie3 = "Máximo";
            } else {
                serie1 = "Buena";
                serie2 = "Regular";
                serie3 = "Mala";
                serie4 = "Cierre Automático";
            }
            
            //Seteo los parámetros del Reporte
            parametrosTiempo.put("logo", logoAddress);
            parametrosTiempo.put("fechai", sdf.format(fechaInicio));
            parametrosTiempo.put("fechaf", sdf.format(fechaFin));
            parametrosTiempo.put("nombrereporte", nombreReporte);
            parametrosTiempo.put("categoria1", nombreRango);
            parametrosTiempo.put("categoria2", nombreDominio);
            parametrosTiempo.put("serie1", serie1);
            parametrosTiempo.put("serie2", serie2);
            parametrosTiempo.put("serie3", serie3);
            parametrosTiempo.put("serie4", serie4);
            
            myList2 = jm.FillListTiempoCalidad(reporteData, tiempos);
            jm.FillReportTiempoCalidad(parametrosTiempo, myList2, extension, jasperFileAddress, nombreReporte);

        } catch (JRException | IOException ex) {
            Logger.getLogger(LlamadaReporteAbstract.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
