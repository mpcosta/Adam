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
/**
	 * Variables for the main lesson and manual button 
	 */
	private Button lessonButton;
	private Button manualButton;
	private HBox buttonsBox;
	/**
	 * Constructor to set the lesson button to Quiz and sets the manual Button to search 
	 * Sets both buttons to button box and aligns it to the home pane 
	 * Sets a greeting label and aligns it under the buttons box 
	 * 
	 */
	public SessionChooserPane() {
		super();
		
		setPrefSize(800, 560);
		
		lessonButton = new JFXButton("Quiz");
		lessonButton.setStyle(MainView.STYLES.BUTTON_RAISED.toString());
		manualButton = new JFXButton("Search");
		manualButton.setStyle(MainView.STYLES.BUTTON_RAISED.toString());
		
		buttonsBox = new HBox();
		buttonsBox.setAlignment(Pos.TOP_CENTER);
		buttonsBox.setPadding(new Insets(20, 0, 0, 0));
		buttonsBox.setSpacing(10);
		buttonsBox.getChildren().addAll(lessonButton, manualButton);
		
		Label greetingLabel = new Label("Welcome to Adam!\nAn innovative way of communicating and\ndisplaying the economics data!");
		greetingLabel.setTextAlignment(TextAlignment.CENTER);
		
		getChildren().addAll(buttonsBox, greetingLabel);
	}
	/**
	 * A getter for the action handler for the lesson button 
	 * @return the lesson button handler 
	 */
	public ObjectProperty<EventHandler<ActionEvent>> getLessonButtonOnActionProperty() {
		return lessonButton.onActionProperty();
	}
	/**
	 * A getter for the action handler for the manual button 
	 * @return the maanual button handler 
	 */
	public ObjectProperty<EventHandler<ActionEvent>> getManualButtonOnActionProperty() {
		return manualButton.onActionProperty();
	}
}
