package adam.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
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
	
	private Pane currentView;

	public MainView() {
		super();
	}
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		topPane = new StackPane();
		
		chartPane = new ChartPane("line", "Chart");
		chartPane.setPadding(new Insets(110, 0, 0 ,0));
		chartPane.setChartData(getRandomChartData(), false);
		
		setCenter(sessionChooserPane);
		
		adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(75);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		topPane.getChildren().add(adam.getListeningImageView());
		
		setTop(topPane);
		
		currentView = sessionChooserPane;
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
	
	public ChartPane getChartPane() {
		return chartPane;
	}
	
	public void transition(Pane toView) {
		if (toView instanceof ManualPane) {
			topPane.getChildren().add(0, toView);
		} else {
			
			setCenter(toView);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ObservableList<XYChart.Series> getRandomChartData() {
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
