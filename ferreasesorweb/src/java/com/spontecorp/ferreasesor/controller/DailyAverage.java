/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.controller;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.jpa.TurnoFacade;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "dailyAverage")
@ViewScoped
public class DailyAverage implements Serializable {

    @EJB
    private LlamadaFacadeExt llamadaFacade;
    @EJB
    private TurnoFacade turnoFacade;
    private String promedioTiempo;
    private int totalLlamadas;
    private int calidadAtencion;
    private List<Llamada> llamadas;

    public DailyAverage() {
    }

    private LlamadaFacadeExt getLlamadaFacade() {
        return llamadaFacade;
    }

    public String getPromedioTiempo() {
        return promedioTiempo;
    }

    public void setPromedioTiempo(String promedioTiempo) {
        this.promedioTiempo = promedioTiempo;
    }

    public int getTotalLlamadas() {
        return totalLlamadas;
    }

    public void setTotalLlamadas(int totalLlamadas) {
        this.totalLlamadas = totalLlamadas;
    }

    public int getCalidadAtencion() {
        return calidadAtencion;
    }

    public void setCalidadAtencion(int calidadAtencion) {
        this.calidadAtencion = calidadAtencion;
    }

    private List<Llamada> getLlamadas(Date fecha) {
        if (llamadas == null) {
            llamadas = llamadaFacade.findLlamadasTiempo(fecha, fecha);
        }

        return llamadas;
    }

    /**
     * Seteo la fecha actual y llamo los metodos que calculan Promedio de
     * Atención, Total Llamadas y Total Llamadas Malas
     */
    public void updateEstatistics() {
        Calendar cal = new GregorianCalendar();
        DecimalFormat df = new DecimalFormat("##0.0");

        cal.setTime(new Date());

        int mes = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int thisDay = cal.get(Calendar.DATE);

        cal.set(year, mes, thisDay);
        Date fechaFin = cal.getTime();

        double[] valores = promedioGral(fechaFin);
        int totalMalas = getTotalMalas(fechaFin);

        setPromedioTiempo(df.format(valores[0]) + " seg.");
        setTotalLlamadas((int) valores[1]);
        setCalidadAtencion(totalMalas);
    }

    /**
     * Se calcula el Promedio General
     *
     * @param fecha
     * @return
     */
    public double[] promedioGral(Date fecha) {
        double[] valores = new double[2];
        double promedio = 0.0;
        long total = 0L;

        for (Llamada llamada : getLlamadas(fecha)) {
            long tOpen = llamada.getHoraOpen().getTime();
            //System.out.println("tOpen: " + tOpen);
            long tClose = llamada.getHoraClose().getTime();
            //System.out.println("tClose: " + tClose);
            long tiempo = (tClose - tOpen) / 1000;
            //System.out.println("tiempo: " + tiempo);
            total += tiempo;
            //System.out.println("Total: " + total);
        }

        if (llamadas.size() > 0) {
            promedio = total / llamadas.size();
            //System.out.println("Promedio: " + promedio);

            valores[0] = promedio;
            valores[1] = llamadas.size();
        } else {
            valores[0] = 0.0;
            valores[1] = 0;
        }
        llamadas = null;
        return valores;
    }

    /**
     * Se calcula Total Llamadas Malas
     *
     * @param fecha
     * @return
     */
    public int getTotalMalas(Date fecha) {
        int total = 0;
        for (Llamada llamada : getLlamadas(fecha)) {
            int tiempoRegular = 0;
//            int turnoId = llamada.getDistribucionId().getTurnoId();
//            Turno turno = turnoFacade.find(turnoId);
//            List<Tiempo> tiempos = turno.getTiempoList();
//            for(Tiempo temp : tiempos){
//                if(temp.getTurnoId().equals(turno)){
//                    tiempoRegular = temp.getAtencionRegular();
//                    break;
//                }
//            }
            tiempoRegular = llamada.getTiempoId().getAtencionRegular();
            long dif = (llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000;
            if (dif > tiempoRegular) {
                total++;
            }
        }
        return total;
    }
}
