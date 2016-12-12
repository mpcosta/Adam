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

	public static final int LINE = 0, BAR = 1, PIE = 2, MAP = 3;
	
	@SuppressWarnings("rawtypes")
	private XYChart chart;
	private PieChart pieChart;
	private String title;
	private WorldMap worldMap;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * A constructor that defines the type of chart and sets the title.
	 * @param type The type of the chart that can be: line, bar or map.
	 * @param title The title of the chart.
	 */
	public ChartPane(int type, String title) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();

		switch (type) {
			case LINE: {
				chart = new LineChart(xAxis, yAxis);
				getChildren().add(chart);
				break;
			}
	
			case BAR: {
				chart = new BarChart(xAxis, yAxis);
				getChildren().add(chart);
				break;
			}
			
			case PIE: {
				pieChart = new PieChart();
				getChildren().add(pieChart);
				break;
			}
	
			case MAP: {
				worldMap = new WorldMap();
				getChildren().add(worldMap);
				break;
			}
			
			default: {
				System.err.println("The type does not exist, try another one.");
				break;
			}
		}
		
		this.title = title;
		if (chart != null) {
			chart.setTitle(this.title);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * A method for setting the data of the chart to an ObservableList.
	 * @param data An object instance of an ObservableList that represents the data for the chart.
	 * @param isPieChart, we used a boolean to represent if chart is pie or another type 
	 */
	public void setChartData(ObservableList data)
	{
		if (pieChart == null)
			chart.setData(data);
		else
			pieChart.setData(data);
	}
	
	public void setMapData(ObservableList<String> countriesList, ObservableList<Double> values) {
		worldMap.changeMap(countriesList, values);
	}
	
	public void setTitle(String title) {
		this.title = title;
		chart.setTitle(title);
	}
}