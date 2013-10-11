package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    TurnoFacade turnoFacade;
    protected Date fechaInicio;
    protected Date fechaFin;
    protected List<Llamada> totalLlamadas;
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
        exportarReporte(extension, jasperFileAddress, promedios);

    }
    public void exportarReportePDFPie(ActionEvent actionEvent) throws JRException, IOException {

        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfpie.jasper");
        setPromedios(false);
        exportarReporte(extension, jasperFileAddress, promedios);

    }
    public void exportarReportePDFCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidad.jasper");

        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempo.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFCalidadStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdfcalidadstacked.jasper");

        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReportePDFTiempoStacked(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress;
        jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportepdftiempostacked.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReporteXLSCalidad(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlscalidad.jasper");
        setTiempos(false);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
    }

    public void exportarReporteXLSTiempo(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/reportexlstiempo.jasper");
        setTiempos(true);
        exportarReporteTiempoCalidad(extension, jasperFileAddress);
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
        exportarReporte(extension, jasperFileAddress, promedios);

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

        exportarReporte(extension, jasperFileAddress, promedios);

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
        exportarReporte(extension, jasperFileAddress, promedios);

    }

    /**
     * Exportar Reporte
     *
     * @param extension
     * @param jasperFileAddress
     * @throws JRException
     * @throws IOException
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

    public void exportarReporte(String extension, String jasperFileAddress, boolean promedios) throws JRException, IOException {

        List<JasperBeanLlamadas> myList;
        JasperManagement jm = new JasperManagement();
        String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");

        try {
            if (promedios == true) {

                Object[][] datos;
                DecimalFormat df = new DecimalFormat("##0.0");
                Object[][] llamadasXTurno = new Object[result.size()][2];
                int i = 0;
                for (Object[] obj : result) {
                    llamadasXTurno[i][0] = (Turno) obj[0];
                    llamadasXTurno[i][1] = (Long) obj[1];
                    i++;
                }

                datos = new Object[i + 1][2];
                String[] rango = new String[i + 1];
                Double[] dominioP = new Double[i + 1];
                datos[0][0] = "Diario";
                datos[0][1] = df.format(getPromedioDiario());
                rango[0] = "Diario";
                dominioP[0] = getPromedioDiario();
                for (int j = 1; j < i + 1; j++) {
                    datos[j][0] = ((Turno) llamadasXTurno[j - 1][0]).getNombre();
                    rango[j] = datos[j][0].toString();
                    double valor = ((Long) llamadasXTurno[j - 1][1]).doubleValue() / getDiasEntreFechas();
                    datos[j][1] = df.format(valor);
                    dominioP[j] = (Double) valor;
                }

                //cuando el reporte sea de promedios 
                myList = jm.FillList(rango, dominioP);             

            } else {
                //Revisa los casos y llena la lista de jasperbean
                myList = jm.FillList(result);
            }

            Map parametros = new HashMap();
            parametros.put("fechai", sdf.format(fechaInicio));
            parametros.put("fechaf", sdf.format(fechaFin));
            parametros.put("nombrereporte", nombreReporte);
            parametros.put("nombre", nombreRango);
            parametros.put("cantidad", nombreDominio);
            parametros.put("logo", logoAddress);
            jm.FillReport(parametros, myList, extension, jasperFileAddress, nombreReporte);

        } catch (JRException e) {
            Logger.getLogger(LlamadaReporteAbstract.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
