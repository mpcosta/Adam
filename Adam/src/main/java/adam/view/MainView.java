package adam.view;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainView extends StackPane {
	
	private SessionChooserPane sessionChooserPane;
	private ManualPane manualSessionPane;
	private Avatar adam;

	public MainView() {
		super();
	}
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		
		getChildren().add(sessionChooserPane);
		
		adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(75);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		getChildren().add(adam.getListeningImageView());
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
	
	public void transition(Pane fromView, Pane toView) {
		getChildren().remove(fromView);
		getChildren().add(0, toView);
	}
}
