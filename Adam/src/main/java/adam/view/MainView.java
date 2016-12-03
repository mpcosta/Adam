package adam.view;

import javafx.geometry.Pos;
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
				
//        MenuButton menuButton = new MenuButton();
//        menuButton.getItems().addAll(
//                Stream.of("Sessions", "Lesson", "Manual", "Quiz")
//                    .map(MenuItem::new).collect(Collectors.toList()));
//        
//        setLeft(menuButton);
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
}
