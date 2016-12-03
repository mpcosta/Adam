package adam.controller;

import adam.view.MainView;

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
		
		mainView.getManualSessionPane().getTextInputProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.matches(".*GDP.*")) {
				mainView.transition(mainView.getChartPane());
			}
		});
	}
}
