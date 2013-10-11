package com.spontecorp.ferreasesor.controller.reporte;

import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.entity.Tiempo;
import com.spontecorp.ferreasesor.entity.Turno;
import com.spontecorp.ferreasesor.jpa.ext.LlamadaFacadeExt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "calidadTurnoBean")
@ViewScoped
public class CalidadXTurnoController extends LlamadaReporteAbstract implements Serializable {

    private String nombreReporte = "Calidad de Atención por Turno";
    private String nombreRango = "Turnos";
    private String nombreDominio = "atenciones";

    /**
     * Metodo para Generar la Tabla de Datos
     *
     * @param actionEvent
     */
    @Override
    public void populateLlamadas(ActionEvent actionEvent) {
        LlamadaFacadeExt llamadaFacade = new LlamadaFacadeExt();
        reporteData = new ArrayList<>();
        long atencion;
        int atencionBuena;
        int atencionRegular;
        int cierreAutomatico;

        //Verifico las fechas
        getFechasVacias();
        //Seteo los Datos del Reporte
        setNombreReporte(nombreReporte);
        setNombreRango(nombreRango);
        setNombreDominio(nombreDominio);

        //Seteo la busqueda (result)
        setResult(llamadaFacade.findLlamadas(ReporteHelper.CALIDAD_X_TURNO, fechaInicio, fechaFin));

        Map<Turno, int[]> atencionesMap = new HashMap<>();
        for (Object[] array : result) {
            atencionesMap.put((Turno) array[1], new int[4]); // indice: 0 buena, 1 regular, 2 mala, 3 automática
        }

        for (Object[] array : result) {
            Llamada llamada = (Llamada) array[0];
            Turno turno = (Turno) array[1];
            Tiempo tiempo = (Tiempo) array[2];

            atencion = (llamada.getHoraClose().getTime() - llamada.getHoraOpen().getTime()) / 1000;
            atencionBuena = tiempo.getAtencionBuena();
            atencionRegular = tiempo.getAtencionRegular();
            cierreAutomatico = tiempo.getCerrarLlamada();

            int[] temp = atencionesMap.get(turno);
            if (atencion <= atencionBuena) {
                temp[0]++;
            }
            if (atencion > atencionBuena && atencion <= atencionRegular) {
                temp[1]++;
            } else if (atencion > atencionRegular && atencion < cierreAutomatico) {
                temp[2]++;
            } else if (atencion >= cierreAutomatico){
                temp[3]++;
            }
            atencionesMap.put(turno, temp);
        }

        for (Map.Entry<Turno, int[]> mapIterator : atencionesMap.entrySet()) {

            //Arreglo para manejar las Propiedades(buenas, regulares, malas)
            Object datos[] = new Object[4];

            ReporteHelper helper = new ReporteHelper();

            //Seteo el Nombre del Objeto (Turno)
            helper.setNombreObj(mapIterator.getKey().getNombre());

            //Seteo las Propiedades del Objeto (buenas, regulares, malas)
            datos[0] = mapIterator.getValue()[0];
            datos[1] = mapIterator.getValue()[1];
            datos[2] = mapIterator.getValue()[2];
            datos[3] = mapIterator.getValue()[3];
            helper.setPropiedadObj(datos);

            reporteData.add(helper);
        }

        showTable = true;
        chartButtonDisable = false;
        chartButtonStackedDisable = false;
    }

    @Override
    public StreamedContent getChart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Metodo para Generar el Grafico en PrimeFaces
     */
    @Override
    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();
        ChartSeries buenas = new ChartSeries("buenas");
        ChartSeries regulares = new ChartSeries("regulares");
        ChartSeries malas = new ChartSeries("malas");
        ChartSeries automaticas = new ChartSeries("automáticas");

        for (ReporteHelper data : reporteData) {
            Object valor[] = data.getPropiedadObj();
            buenas.set(data.getNombreObj().toString(), Double.valueOf(valor[0].toString()));
            regulares.set(data.getNombreObj().toString(), Double.valueOf(valor[1].toString()));
            malas.set(data.getNombreObj().toString(), Double.valueOf(valor[2].toString()));
            automaticas.set(data.getNombreObj().toString(), Double.valueOf(valor[3].toString()));
        }

        categoryModel.addSeries(buenas);
        categoryModel.addSeries(regulares);
        categoryModel.addSeries(malas);
        categoryModel.addSeries(automaticas);
    }
}
