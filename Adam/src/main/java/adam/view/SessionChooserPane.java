package adam.view;

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
	
	public Button getLessonButton() {
		return lessonButton;
	}
	
	public Button getManualButton() {
		return manualButton;
	}
	
//	public ObservableList<Button> getButtons() {
//		ObservableList<Button> buttons = FXCollections.observableArrayList();
//		
//		for (Node node : getChildren()) {
//			if (node instanceof MaterialDesignButton) {
//				buttons.add((Button) node);
//			}
//		}
//		
//		return buttons;
//	}
}
