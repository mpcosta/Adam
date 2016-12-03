package adam.view;

import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class ManualPane extends StackPane {
	
	private TextField textBox;
	
	public ManualPane() {
		super();
		
		setPrefSize(800, 560);
		
		getChildren().add(textBox);
	}
}
