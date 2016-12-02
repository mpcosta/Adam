package adam.view;

import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class ChartPane extends StackPane {

	@SuppressWarnings("rawtypes")
	private XYChart chart;

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
				break;
			}
	
			case "bar": {
				chart = new BarChart(xAxis, yAxis);
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
		getChildren().add(chart);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * A method for setting the data of the chart to an ObservableList.
	 * @param data An object instance of an ObservableList that represents the data for the chart.
	 */
	public void setChartData(ObservableList<XYChart.Series> data) {
		chart.setData(data);
	}
}