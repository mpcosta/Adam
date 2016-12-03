package adam.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ManualPane extends StackPane {
	
	private TextField textField;
	
	public ManualPane() {
		super();
		
		setPrefSize(800, 560);
		
		HBox searchBar = new HBox();
		Button button = new Button("GO");
		
		textField = new TextField();
		textField.setPrefWidth(300);
		
		searchBar.setPadding(new Insets(75, 0, 0, 0));
		searchBar.setSpacing(10);
		searchBar.getChildren().addAll(textField, button);
		
		searchBar.setAlignment(Pos.TOP_CENTER);
		
		getChildren().add(searchBar);
	}
}
