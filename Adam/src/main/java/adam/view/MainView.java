package adam.view;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainView extends StackPane {
	
	private SessionChooserPane sessionChooserPane;
	private ManualPane manualSessionPane;
	private ChartPane chartPane;
	private Avatar adam;
	
	private Pane currentView;

	public MainView() {
		super();
	}
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		chartPane = new ChartPane("line", "Chart");
		
		getChildren().add(sessionChooserPane);
		
		adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(75);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		getChildren().add(adam.getListeningImageView());
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
		getChildren().remove(currentView);
		getChildren().add(0, toView);
	}
}
