package adam.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class SessionChooserPane extends StackPane {

	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox; 
	
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		lessonButton = new MaterialDesignButton("Lesson", Color.ROYALBLUE);
		manualButton = new MaterialDesignButton("Manual", Color.ROYALBLUE);
		
		buttonsBox = new HBox();
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		getChildren().add(buttonsBox);
	}

}
