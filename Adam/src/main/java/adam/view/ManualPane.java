package adam.view;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ManualPane extends StackPane {
	
	private AutoCompleteTextField textField;
	private Button submitButton;
	private HBox searchBar;
	
	public ManualPane() {
		super();
		
//		setPrefSize(800, 560);
		
		submitButton = new JFXButton("Search");
		submitButton.getStyleClass().add("button-raised");
		
		textField = new AutoCompleteTextField();
		textField.setPrefSize(300, 20);
		
		searchBar = new HBox();
		searchBar.setPadding(new Insets(75, 10, 0, 10));
		searchBar.setSpacing(10);
		searchBar.setAlignment(Pos.TOP_CENTER);
		
		searchBar.getChildren().addAll(textField, submitButton);
		getChildren().add(searchBar);
	}
	
	public AutoCompleteTextField getAutoCompleteTextField() {
		return textField;
	}
	
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyPressedProperty() {
		return textField.onKeyPressedProperty();
	}
	
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyReleasedProperty()
	{
		return textField.onKeyReleasedProperty();
	}
	
	public StringProperty getTextInputTextProperty() {
		return textField.textProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getOnActionProperty() {
		return submitButton.onActionProperty();
	}
}
