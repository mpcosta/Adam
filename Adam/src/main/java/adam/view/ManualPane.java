package adam.view;

import java.util.SortedSet;
import java.util.TreeSet;

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
	private SortedSet<String> entries;
	
	public ManualPane() {
		super();
		
		setPrefSize(800, 560);
		
		entries = new TreeSet<String>();
		
		submitButton = new JFXButton("Search");
		submitButton.getStyleClass().add("button-raised");
		
		textField = new AutoCompleteTextField();
		textField.setPrefSize(300, 20);
		textField.getEntries().addAll(getEntries());
		
		searchBar = new HBox();
		searchBar.setPadding(new Insets(75, 10, 0, 10));
		searchBar.setSpacing(10);
		searchBar.setAlignment(Pos.TOP_CENTER);
		
		searchBar.getChildren().addAll(textField, submitButton);
		getChildren().add(searchBar);
	}
	
	public SortedSet<String> getEntries() {
		entries.add("show me");
		entries.add("show me GDP");
		return entries;
	}
	
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyPressedProperty() {
		return textField.onKeyPressedProperty();
	}
	
	public StringProperty getTextInputTextProperty() {
		return textField.textProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getOnActionProperty() {
		return submitButton.onActionProperty();
	}
}