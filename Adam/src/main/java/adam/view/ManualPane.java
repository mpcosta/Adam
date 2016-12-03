package adam.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ManualPane extends StackPane {
	
	private TextField textField;
	private Button submitButton;
	private HBox searchBar;
	
	public ManualPane() {
		super();
		
		setPrefSize(800, 560);
		
		submitButton = new Button("GO");
		
		textField = new TextField();
		textField.setPrefWidth(300);
		
		searchBar = new HBox();
		searchBar.setPadding(new Insets(75, 0, 0, 0));
		searchBar.setSpacing(10);
		searchBar.setAlignment(Pos.TOP_CENTER);
		
		searchBar.getChildren().addAll(textField, submitButton);
		getChildren().add(searchBar);
	}
	
	public StringProperty getTextInputProperty() {
		return textField.textProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getOnActionProperty() {
		return submitButton.onActionProperty();
	}
}
