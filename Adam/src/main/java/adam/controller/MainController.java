package adam.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;

import adam.model.Area;
import adam.model.Country;
import adam.model.Region;
import adam.view.AutoCompleteTextField;
import adam.view.ChartPane;
import adam.view.MainView;
import javafx.collections.ObservableList;
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
			if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP)
				return;
			String text = ((TextField)key.getSource()).getText();
			
			AutoCompleteTextField textField = mainView.getManualSessionPane().getAutoCompleteTextField();
			
			if (key.getCode() == KeyCode.ENTER)
			{
				processCommand(text);
				textField.setEntries(new LinkedList<String>(), new LinkedList<String>());
				textField.updateDisplay();
				return;
			}
			
			LinkedList<String> display = new LinkedList<String>(),
					override = new LinkedList<String>();
			
			String existing = "";
			String[] segments = text.split(" ");
			for (String segment : segments)
			{
				getSuggestions(existing, segment, text, display, override);
				existing += segment + " ";
			}
			
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
		
		Thread thread = new Thread()
		{
			public void run()
			{
				Area.cacheAllNames();
			}
		};
		thread.start();
	}
	
	private void getSuggestions(String existing, String segment, String full, LinkedList<String> display, LinkedList<String> override)
	{
		final String[] special = new String[] //TODO: Example keywords that have no functionality currently.  
		{
			"GDP", "vs", "show", "tell", "me", "the", "BOP", "CPI", "capital" 
		};
		
		ArrayList<String> suggestions = Area.estimateNamesFromFragment(segment);
		for (String s : special)
		{
			if (s.contains(segment))
				suggestions.add(s);
		}
		
		for (String suggestion : suggestions)
		{
			if (full.contains(suggestion) && !full.endsWith(suggestion))
				return;
		}
		
		for (String item : suggestions)
		{
			if (display.contains(item))
				continue;
			display.add(item);
			override.add(existing + item + " ");
		}
	}
	
	private void processCommand(String command)
	{
		
	}
	
	private void displayChart(int type, String title, ObservableList data)
	{
		ChartPane pane = new ChartPane(type, title);
		pane.setChartData(data);
		mainView.transition(pane);
	}
}
