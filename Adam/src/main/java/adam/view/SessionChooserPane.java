package adam.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SessionChooserPane extends StackPane {
	
	private Label titleLabel;
	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox; 
	
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		titleLabel = new Label("Session type");
		titleLabel.setPadding(new Insets(20));
		
		lessonButton = new Button("Lesson");
		manualButton = new Button("Manual");
		buttonsBox = new HBox();
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		getChildren().add(titleLabel);
		getChildren().add(buttonsBox);
		
		StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);
	}

}
