package adam.view;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;

public class MainView extends StackPane {
	
	private SessionChooserPane sessionChooserPane;

	public MainView() {
		super();
	}
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		getChildren().add(sessionChooserPane);
		
		Avatar adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(75);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_RIGHT);
		
		getChildren().add(adam.getListeningImageView());
				
//        MenuButton menuButton = new MenuButton();
//        menuButton.getItems().addAll(
//                Stream.of("Sessions", "Lesson", "Manual", "Quiz")
//                    .map(MenuItem::new).collect(Collectors.toList()));
//        
//        setLeft(menuButton);
	}
	
}
