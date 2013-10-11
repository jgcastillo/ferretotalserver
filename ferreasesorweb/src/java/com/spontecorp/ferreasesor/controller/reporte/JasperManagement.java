/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Numericas;
import com.spontecorp.ferreasesor.entity.RespuestaMotivo;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import com.spontecorp.ferreasesor.entity.Turno;
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

    private List<JasperBeanLlamadas> lista = new ArrayList<>();
    private List<JasperBeanEncuestas> listaRespuestas = new ArrayList<>();
    private List<JasperBeanRecolectorInformacion> listaRecolector = new ArrayList<>();
    List<JasperBeanTiempoCalidad> listatiempo = new ArrayList<>();

    public List<JasperBeanLlamadas> FillList(List<Object[]> result) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String nombre = "";
        for (Object[] array : result) {
            if (array[0] instanceof Boton) {
                nombre = ((Boton) array[0]).getUbicacion();
            } else if (array[0] instanceof Asesor) {
                nombre = ((Asesor) array[0]).getNombre() + " " + ((Asesor) array[0]).getApellido();
            } else if (array[0] instanceof Turno) {
                nombre = ((Turno) array[0]).getNombre();
            } else if (array[0] instanceof Date) {
                nombre = sdf.format(((Date) array[0]));
            }
            JasperBeanLlamadas jb = new JasperBeanLlamadas(nombre, ((Long) array[1]).doubleValue());
            lista.add(jb);
        }
        return lista;
    }

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

    public List<JasperBeanLlamadas> FillList(String[] rango, Double[] dominio) {
        for (int i = 0; i < rango.length; i++) {
            JasperBeanLlamadas jb = new JasperBeanLlamadas(rango[i], dominio[i]);
            //System.out.println(dominio[i]);
            lista.add(jb);
        }
        return lista;
    }

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
    
    public List<JasperBeanRecolectorInformacion> FillListRecolector(List<RespuestaMotivo> respList) {
        for (int i = 0; i < respList.size(); i++) {
            JasperBeanRecolectorInformacion jb = new JasperBeanRecolectorInformacion(respList.get(i).getMotivoId().getNombre(), i+1);
            listaRecolector.add(jb);
        }
        return listaRecolector;
    }

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

    public void FillReport(Map parametros, List<JasperBeanLlamadas> lista, String extension, String nombreJasper, String nombreReporte) throws JRException, IOException {
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
            JRXlsExporter xlsxExporter = new JRXlsExporter();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
            xlsxExporter.exportReport();
            FacesContext.getCurrentInstance().responseComplete();
        }
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
    
    /**
     * Reportes Recolector de Información
     * Motivo de Llamado
     * 
     * @param parametros
     * @param lista
     * @param extension
     * @param nombreJasper
     * @param nombreReporte
     * @throws JRException
     * @throws IOException 
     */
    public void FillReportRecolector(Map parametros, List<JasperBeanRecolectorInformacion> lista, String extension, String nombreJasper, String nombreReporte) throws JRException, IOException {
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
}
