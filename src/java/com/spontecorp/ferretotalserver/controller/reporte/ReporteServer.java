/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.reporte;

import java.util.List;

/**
 *
 * @author sponte03
 */
public class ReporteServer {
    
    private String fecha;
    private List<ReporteHelper> reporteHelper;

    public ReporteServer() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<ReporteHelper> getReporteHelper() {
        return reporteHelper;
    }

    public void setReporteHelper(List<ReporteHelper> reporteHelper) {
        this.reporteHelper = reporteHelper;
    }
    
}
