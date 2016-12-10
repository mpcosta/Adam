package adam.view;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

public class SessionChooserPane extends StackPane {

	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox;
	
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		lessonButton = new JFXButton("Quiz");
		lessonButton.getStyleClass().add("button-raised");
		manualButton = new JFXButton("Search");
		manualButton.getStyleClass().add("button-raised");
		
		buttonsBox = new HBox();
		buttonsBox.setAlignment(Pos.TOP_CENTER);
		buttonsBox.setPadding(new Insets(20, 0, 0, 0));
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		Label greetingLabel = new Label("Welcome to Adam!\nAn innovative way of communicating and\ndisplaying the economics data!");
		greetingLabel.setTextAlignment(TextAlignment.CENTER);
		
		getChildren().addAll(buttonsBox, greetingLabel);
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getLessonButtonOnActionProperty() {
		return lessonButton.onActionProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getManualButtonOnActionProperty() {
		return manualButton.onActionProperty();
	}
}