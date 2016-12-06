package adam.controller;

import adam.view.MainView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class MainController {
	
	private MainView mainView;

	public MainController(MainView mainView) {
		this.mainView = mainView;
		
		init();
	}
	
	private void init() {
		mainView.getSessionChooserPane().getLessonButtonOnActionProperty().set(handler -> {
			System.out.println("Lesson transition");
		});
		
		mainView.getSessionChooserPane().getManualButtonOnActionProperty().set(handler -> {
			mainView.transition(mainView.getManualSessionPane());
		});
		
		mainView.getAvatar().getListeningImageView().setOnMouseClicked(handler -> {
			System.out.println("Listening...");
		});
		
		mainView.getAvatar().getSpeakingImageView().setOnMouseClicked(handler -> {
			System.out.println("Speaking...");
		});
		
		mainView.getManualSessionPane().getTextInputTextProperty().addListener((observable, oldValue, newValue) -> {
			// TODO: add matcher for keywords such as GDP, country, etc.
		});
		
		mainView.getManualSessionPane().getTextInputOnKeyPressedProperty().set(key -> {
			if (((KeyEvent) key).getCode().equals(KeyCode.ENTER)) {
				String text = ((TextField)key.getSource()).getText();
				if (text.matches(".*GDP\\sUSA\\svs\\sUK.*")) {
					mainView.getChartPane().setTitle("GDP USA vs UK");
					if (!mainView.getChildren().contains(mainView.getChartPane())) {
						mainView.getChildren().add(mainView.getChartPane());
					}
				}
			}
		});
	}
}
