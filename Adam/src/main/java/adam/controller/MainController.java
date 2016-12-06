package adam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;

import adam.model.Area;
import adam.view.AutoCompleteTextField;
import adam.view.MainView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

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
		
		mainView.getManualSessionPane().getTextInputOnKeyReleasedProperty().set(key ->
		{
			String text = ((TextField)key.getSource()).getText();
			
			LinkedList<String> display = new LinkedList<String>(),
					override = new LinkedList<String>();
			
			String existing = "";
			String[] segments = text.split(" ");
			for (String segment : segments)
			{
				getSuggestions(existing, segment, display, override);
				existing += segment + " ";
			}
			
			for (int i = 0; i < display.size(); i++)
			{
				if (text.contains(display.get(i)))
				{
					display.remove(i);
					override.remove(i);
				}
			}
			
			AutoCompleteTextField textField = mainView.getManualSessionPane().getAutoCompleteTextField();
			textField.setEntries(display, override);
			textField.updateDisplay();
			
			/*
			if (((KeyEvent) key).getCode().equals(KeyCode.ENTER)) {
				String text = ((TextField)key.getSource()).getText();
				if (text.matches(".*GDP\\sUSA\\svs\\sUK.*")) {
					mainView.getChartPane().setTitle("GDP USA vs UK");
					if (!mainView.getChildren().contains(mainView.getChartPane())) {
						mainView.getChildren().add(mainView.getChartPane());
					}
				}
			}
			*/
		});
	}
	private void getSuggestions(String existing, String text, LinkedList<String> display, LinkedList<String> override)
	{
		ArrayList<String> suggestions = Area.estimateNamesFromFragment(text);
		for (String item : suggestions)
		{
			if (display.contains(item))
				continue;
			display.add(item);
			override.add(existing + item);
		}
		
		final String[] special = new String[]
		{
			"GDP", "vs", "show", "me", "bop", "cpi", "captital" 
		};
		
		for (String s : special)
		{
			if (s.contains(text))
			{
				display.add(s);
				override.add(s);
			}
		}
	}
}
