package adam.view;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class ChartPane extends StackPane {

	@SuppressWarnings("rawtypes")
	private XYChart chart;
	private PieChart pieChart;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * A constructor that defines the type of chart and sets the title.
	 * @param type The type of the chart that can be: line, bar or map.
	 * @param title The title of the chart.
	 */
	public ChartPane(String type, String title) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		switch (type) {
			case "line": {
				chart = new LineChart(xAxis, yAxis);
				getChildren().add(chart);
				break;
			}
	
			case "bar": {
				chart = new BarChart(xAxis, yAxis);
				getChildren().add(chart);
				break;
			}
			
			case "pie": {
				pieChart = new PieChart();
				getChildren().add(pieChart);
				break;
			}
	
			case "map": {
				System.err.println("Google Map is not yet implemented, try line/bar chart.");
				break;
			}
			
			default: {
				System.err.println("The type does not exist, try another one.");
				break;
			}
		}
		
		chart.setTitle(title);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * A method for setting the data of the chart to an ObservableList.
	 * @param data An object instance of an ObservableList that represents the data for the chart.
	 * @param isPieChart, we used a boolean to represent if chart is pie or another type 
	 */
	public void setChartData(ObservableList data, boolean isPieChart) {
		if (isPieChart) {
			pieChart.setData(data);
		} else {
			chart.setData(data);
		}
	}
}