package adam.controller;

import adam.view.MainView;

public class MainController {
	
	private MainView mainView;

	public MainController(MainView mainView) {
		this.mainView = mainView;
		
		init();
	}
	
	private void init() {
		mainView.getSessionChooserPane().getLessonButton().set(handler -> {
			System.out.println("Lesson transition");
		});
		
		mainView.getSessionChooserPane().getManualButton().set(handler -> {
			mainView.transition(mainView.getSessionChooserPane(), mainView.getManualSessionPane());
		});
		
		mainView.getAvatar().getListeningImageView().setOnMouseClicked(handler -> {
			System.out.println("Listening...");
		});
		
		mainView.getAvatar().getSpeakingImageView().setOnMouseClicked(handler -> {
			System.out.println("Speaking...");
		});
	}
}
