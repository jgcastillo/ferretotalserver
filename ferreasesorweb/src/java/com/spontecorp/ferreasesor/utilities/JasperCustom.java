/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferreasesor.utilities;

import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;

/**
 *
 * @author sponte07
 */
public class JasperCustom extends JRAbstractChartCustomizer {

    @Override
    public void customize(JFreeChart chart, JRChart jasperChart) {

        //Chart is a bar chart
        if (jasperChart.getChartType() == JRChart.CHART_TYPE_BAR || jasperChart.getChartType() == JRChart.CHART_TYPE_STACKEDBAR) {

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setMaximumBarWidth(0.10);
            renderer.setBaseItemLabelsVisible(false);
        }
    }
}
