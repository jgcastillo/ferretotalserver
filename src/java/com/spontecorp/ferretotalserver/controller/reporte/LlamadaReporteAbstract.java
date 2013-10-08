package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.jpa.ext.LlamadaFacadeExt;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    protected List<Llamada> totalLlamadas;
    protected List<String> selectedTiendas;  
    protected List<String> selectedAllTiendas;
    protected Map<String,Integer> tiendas; 
    private Map<String, String> allTiendas;
    protected boolean showTable = false;
    protected boolean showChart = false;
    protected boolean showStackedChart = false;
    protected boolean chartButtonDisable = true;
    protected boolean chartButtonStackedDisable = true;
    protected List<Object[]> result;
    protected List<Llamada> resultLlamadas;
    protected List<ReporteHelper> reporteData;
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
        
        for(Tienda tiendaItem : allTiendas){
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
     * Exportar Reporte .PDF
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDF(ActionEvent actionEvent) throws JRException, IOException {

        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdf.jasper");
        setPromedios(false);
        //    exportarReporte(extension, jasperFileAddress, promedios);

    }

    public void exportarReportePDFPie(ActionEvent actionEvent) throws JRException, IOException {

        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfpie.jasper");
        setPromedios(false);
        //   exportarReporte(extension, jasperFileAddress, promedios);

    }

    public void exportarReportePDFCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidad.jasper");

        setTiempos(false);
        //   exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempo.jasper");
        setTiempos(true);
        //    exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFCalidadStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidadstacked.jasper");

        setTiempos(false);
        //    exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFTiempoStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempostacked.jasper");
        setTiempos(true);
        // exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReporteXLSCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlscalidad.jasper");
        setTiempos(false);
        //   exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReporteXLSTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlstiempo.jasper");
        setTiempos(true);
        //  exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    /**
     * Exportar Reporte .XLS
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteXLS(ActionEvent actionEvent) throws JRException, IOException {

        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexls.jasper");
        setPromedios(false);
        setTiempos(false);
        // exportarReporte(extension, jasperFileAddress, promedios);

    }

    /**
     * Exportar Reporte .PDF (promedios)
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFPromedios(ActionEvent actionEvent) throws JRException, IOException {

        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdf.jasper");
        setPromedios(true);

        // exportarReporte(extension, jasperFileAddress, promedios);

    }

    /**
     * Exportar Reporte .XLS (promedios)
     *
     * @param actionEvent
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteXLSPromedios(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLSX";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexls.jasper");
        setPromedios(true);
        setTiempos(false);
        // exportarReporte(extension, jasperFileAddress, promedios);

    }
}
