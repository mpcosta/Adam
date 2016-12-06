package adam.view;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class SessionChooserPane extends BorderPane {

	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox; 
	
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		lessonButton = new JFXButton("Lesson");
		lessonButton.getStyleClass().add("button-raised");
		manualButton = new JFXButton("Manual");
		manualButton.getStyleClass().add("button-raised");
		
		buttonsBox = new HBox();
		buttonsBox.setPrefSize(300, 300);
		buttonsBox.setAlignment(Pos.CENTER);
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		setCenter(buttonsBox);
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getLessonButtonOnActionProperty() {
		return lessonButton.onActionProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getManualButtonOnActionProperty() {
		return manualButton.onActionProperty();
	}
}
