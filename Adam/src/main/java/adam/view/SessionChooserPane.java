package adam.view;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SessionChooserPane extends StackPane {

	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox; 
	
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		lessonButton = new Button("Lesson");
		manualButton = new Button("Manual");
		
		buttonsBox = new HBox();
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		getChildren().add(buttonsBox);
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getLessonButtonOnActionProperty() {
		return lessonButton.onActionProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getManualButtonOnActionProperty() {
		return manualButton.onActionProperty();
	}
}
