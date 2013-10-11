package com.spontecorp.ferreasesor.controller.chart;

import com.spontecorp.ferreasesor.controller.reporte.ReporteHelper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Casper
 */
public class BarChart implements Serializable{
    
    private StreamedContent barChart;
    private DefaultCategoryDataset dataset;
    private List<Object[]> result;
    private ReporteHelper helper;
    private SimpleDateFormat sdf;
    private String nombreReporte;
    private String nombreRango;
    private String nombreDominio;
    private Logger logger = LoggerFactory.getLogger(BarChart.class);

    public BarChart(String nombreReporte, String nombreRango,String nombreDominio) {
        this.nombreReporte = nombreReporte;
        this.nombreRango = nombreRango;
        this.nombreDominio = nombreDominio;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    
    public void setResult(List<Object[]> result){
        this.result = result;
    }

    public StreamedContent getBarChart() {
        try {
            JFreeChart chart = ChartFactory.createBarChart(
                    nombreReporte,
                    nombreRango,
                    nombreDominio,
                    dataset,
                    PlotOrientation.VERTICAL,
                    false,
                    true,
                    false);
            // Customización de colores de las barras
            CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
            categoryplot.setNoDataMessage("No Existen Datos!");
            categoryplot.setBackgroundPaint(null);
            categoryplot.setInsets(new RectangleInsets(10D, 5D, 5D, 5D));
            categoryplot.setOutlinePaint(Color.black);
            categoryplot.setRangeGridlinePaint(Color.gray);
            categoryplot.setRangeGridlineStroke(new BasicStroke(1.0F));
            
            CategoryAxis axis = categoryplot.getDomainAxis();
            axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
            
            BarRenderer barRenderer = (BarRenderer) categoryplot.getRenderer();
            barRenderer.setMaximumBarWidth(0.10000000000000001D);
            barRenderer.setSeriesPaint(0, Color.BLUE);
            
//            ChartPanel chartPanel = new ChartPanel(chart, false);
//            chartPanel.setPreferredSize(new Dimension(475, 250));
            
            File chartFile = new File("c:/temp/dynamichart");
            ChartUtilities.saveChartAsPNG(chartFile, chart, 475, 350);
            barChart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/jpg");
            
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return barChart;
    }

    public void createDataset(){
        dataset = new DefaultCategoryDataset();
        for(Object[] array : result){
            helper = new ReporteHelper();
            helper.setRango(sdf.format((Date)array[0]));
            helper.setDominio(Integer.valueOf(String.valueOf(array[1])));
            dataset.addValue(helper.getDominio(), "llamadas", helper.getRango().toString());
        }
    }
}
