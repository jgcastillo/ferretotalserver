/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.reporte;

import com.spontecorp.ferretotalserver.entity.Numericas;
import com.spontecorp.ferretotalserver.entity.RespuestaObtenida;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author sponte03
 */
public class JasperManagement {

    private List<JasperBeanEncuestas> listaRespuestas = new ArrayList<>();
    List<JasperBeanTiempoCalidad> listatiempo = new ArrayList<>();

    /**
     * Preparar Listado Reporte Encuestas
     *
     * @param pregunta
     * @return
     */
    public List<JasperBeanEncuestas> FillListEncuesta(List<RespuestaObtenida> respList) {
        for (int i = 0; i < respList.size(); i++) {
            JasperBeanEncuestas jb = new JasperBeanEncuestas(respList.get(i).getRespuesta(), i+1 );
            listaRespuestas.add(jb);
        }
        return listaRespuestas;
    }

    /**
     * Reporte Encuestas
     * Para Preguntas de Tipo Númerico, Selección y Calificación
     *
     * @param parametros
     * @param lista
     * @param extension
     * @param nombreJasper
     * @param nombreReporte
     * @throws JRException
     * @throws IOException
     */
    public void FillReportEncuesta(Map parametros, List<Numericas> lista, String extension, String nombreJasper, String nombreReporte) throws JRException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        File file = new File(nombreJasper);
        JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
        JRBeanCollectionDataSource jbs = new JRBeanCollectionDataSource(lista);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, jbs);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nombreReporte + "_" + sdf.format((new Date())) + "." + extension);

        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        if ("PDF".equals(extension)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } else {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            JRXlsExporter xlsxExporter = new JRXlsExporter();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            xlsxExporter.exportReport();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    /**
     * Reporte Encuestas
     * Sólo para Preguntas de tipo Textual
     * 
     * @param parametros
     * @param lista
     * @param extension
     * @param nombreJasper
     * @param nombreReporte
     * @throws JRException
     * @throws IOException 
     */
    public void FillReportTextualEncuesta(Map parametros, List<JasperBeanEncuestas> lista, String extension, String nombreJasper, String nombreReporte) throws JRException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        File file = new File(nombreJasper);
        JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
        JRBeanCollectionDataSource jbs = new JRBeanCollectionDataSource(lista);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, jbs);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nombreReporte + "_" + sdf.format((new Date())) + "." + extension);

        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        if ("PDF".equals(extension)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
        } else {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            JRXlsExporter xlsxExporter = new JRXlsExporter();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            xlsxExporter.exportReport();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public JasperManagement() {
        super();
    }
    
    /**
     * Se llena la lista para el Reporte de Tiempo - Calidad
     * @param reporteData
     * @param tiempo
     * @return 
     */
    public List<JasperBeanTiempoCalidad> FillListTiempoCalidad(List<ReporteHelper> reporteData, boolean tiempo) {

        for (int i = 0; i < reporteData.size(); i++) {
            double serie1 = Double.valueOf(reporteData.get(i).getPropiedadObj()[0].toString());
            double serie2 = Double.valueOf(reporteData.get(i).getPropiedadObj()[1].toString());
            double serie3 = Double.valueOf(reporteData.get(i).getPropiedadObj()[2].toString());
            double serie4 = 0;
            if (!tiempo) {
                serie4 = Double.valueOf(reporteData.get(i).getPropiedadObj()[3].toString());
            }
            String propiedad = reporteData.get(i).getNombreObj().toString();
            JasperBeanTiempoCalidad jbt = new JasperBeanTiempoCalidad(propiedad, serie1, serie2, serie3, serie4);
            listatiempo.add(jbt);
        }
        return listatiempo;
    }
    
    /**
     * Se genera el Reporte Tiempo - Calidad
     * @param parametros
     * @param lista
     * @param extension
     * @param nombreJasper
     * @param nombreReporte
     * @throws JRException
     * @throws IOException 
     */
    public void FillReportTiempoCalidad(Map parametros, List<JasperBeanTiempoCalidad> lista, String extension, String nombreJasper, String nombreReporte) throws JRException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        File file = new File(nombreJasper);
        JasperReport reporte = (JasperReport) JRLoader.loadObject(file);
        JRBeanCollectionDataSource jbs = new JRBeanCollectionDataSource(lista);
        JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, jbs);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + nombreReporte + "_" + sdf.format((new Date())) + "." + extension);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

        if ("PDF".equals(extension)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } else {
            httpServletResponse.setContentType("application/vnd.ms-excel");
            JRXlsExporter xlsExporter = new JRXlsExporter();
            xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            xlsExporter.exportReport();
        }

    }
}
