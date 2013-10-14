/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.reporte;

import java.util.Date;
import java.util.List;

/**
 *
 * @author sponte03
 */
public class ReporteServer implements Comparable<ReporteServer>{
    
    private Date fecha;
    private String current;
    private List<ReporteHelper> reporteHelper;

    public ReporteServer() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ReporteHelper> getReporteHelper() {
        return reporteHelper;
    }

    public void setReporteHelper(List<ReporteHelper> reporteHelper) {
        this.reporteHelper = reporteHelper;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    public int compareTo(ReporteServer o) {
        if(this.getFecha().before(o.getFecha())){
            return -1;
        } else if(this.getFecha().equals(o.getFecha())) {
            return 0;
        } else {
            return 1;
        }
    }
}
