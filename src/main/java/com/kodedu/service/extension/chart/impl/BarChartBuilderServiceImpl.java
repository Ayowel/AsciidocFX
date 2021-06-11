package com.kodedu.service.extension.chart;

import com.kodedu.config.ExtensionConfigBean;
import com.kodedu.controller.ApplicationController;
import com.kodedu.other.Current;
import com.kodedu.service.ThreadService;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

/**
 * Created by usta on 31.03.2015.
 */
@Component("bar-bean")
public class BarChartBuilderService extends XYChartBuilderService {

    public BarChartBuilderService(ThreadService threadService, Current current, ApplicationController controller, ExtensionConfigBean extensionConfigBean) {
        super(threadService, current, controller, extensionConfigBean);
    }

    @Override
    protected XYChart<String, Number> createXYChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final XYChart<String, Number> chart = new BarChart<String, Number>(xAxis, yAxis);
        chart.getStyleClass().add("chart-extension");
        return chart;
    }


}
