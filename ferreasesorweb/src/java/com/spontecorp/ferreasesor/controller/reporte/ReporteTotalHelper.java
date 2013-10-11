package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Asesor;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Distribucion;
import com.spontecorp.ferreasesor.entity.Llamada;
import javax.ejb.Stateless;

/**
 *
 * @author Casper
 */
@Stateless
public class ReporteTotalHelper {
    
    private Llamada llamada;
    private Distribucion dist;
    private Asesor asesor;
    private Boton boton;
    private Number dominio;

    public Llamada getLlamada() {
        return llamada;
    }

    public void setLlamada(Llamada llamada) {
        this.llamada = llamada;
    }

    public Distribucion getDist() {
        return dist;
    }

    public void setDist(Distribucion dist) {
        this.dist = dist;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public Boton getBoton() {
        return boton;
    }

    public void setBoton(Boton boton) {
        this.boton = boton;
    }

    public Number getDominio() {
        return dominio;
    }

    public void setDominio(Number dominio) {
        this.dominio = dominio;
    }
    
    
    
    /**
     * Query para llenar la tabla SELECT ll.fecha_Open, ll.hora_open,
     * ll.fecha_close, ll.hora_close, b.ubicacion, t.nombre, a.nombre,
     * a.apellido FROM Llamada ll JOIN Distribucion d, Asesor a, Boton b, Turno
     * t WHERE d.id = ll.distribucion_id AND d.asesor_id = a.id AND d.boton_id =
     * b.id AND d.turno_id = t.id;
     */
    
}
