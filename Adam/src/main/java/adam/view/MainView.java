package adam.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainView extends BorderPane {
	
	private SessionChooserPane sessionChooserPane;
	private ManualPane manualSessionPane;
	private ChartPane chartPane;
	private Avatar adam;
	private StackPane topPane;
	private QuizPane quizPane;
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		topPane = new StackPane();
		quizPane = new QuizPane();
		
		chartPane = new ChartPane(ChartPane.LINE, "Chart");
		chartPane.setChartData(getRandomChartData());
		
		adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(90);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		topPane.getChildren().add(adam.getListeningImageView());
		
		BorderPane.setAlignment(topPane, Pos.TOP_CENTER);
		
		setCenter(sessionChooserPane);
		setTop(topPane);
	}
	
	public SessionChooserPane getSessionChooserPane() {
		return sessionChooserPane;
	}
	
	public Avatar getAvatar() {
		return adam;
	}
	
	public ManualPane getManualSessionPane() {
		return manualSessionPane;
	}
	
	public QuizPane getQuizPane() {
		return quizPane;
	}
	
	public void setChartPane(ChartPane chart) {
		chartPane = chart;
	}
	
	public void transition(Pane toView) {
		if (toView instanceof ManualPane) {
			topPane.getChildren().add(0, toView);
			getChildren().remove(sessionChooserPane);
			
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
		}
		
		if (toView instanceof QuizPane) {
			setCenter(toView);
			
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
		}
		
		if (toView instanceof ChartPane) {
			setCenter(toView);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ObservableList<XYChart.Series> getRandomChartData() {
		double aValue = 1.56;
		double cValue = 1.06;
		ObservableList<XYChart.Series> answer = FXCollections.observableArrayList();
		Series<String, Double> aSeries = new Series<String, Double>();
		Series<String, Double> cSeries = new Series<String, Double>();
		aSeries.setName("UK");
		cSeries.setName("US");

		for (int i = 2011; i < 2021; i++) {
			aSeries.getData().add(new XYChart.Data(Integer.toString(i), aValue));
			aValue = aValue + Math.random() - .5;
			cSeries.getData().add(new XYChart.Data(Integer.toString(i), cValue));
			cValue = cValue + Math.random() - .5;
		}

		answer.addAll(aSeries, cSeries);
		return answer;
	}
}