package adam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import adam.model.Area;
import adam.view.AutoCompleteTextField;
import adam.view.ChartPane;
import adam.view.MainView;
import adam.view.res.QuizRes;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class MainController {
	
	private MainView mainView;
	private QuizRes quizRes;
	private CommandProcessor commandProcessor;

	public MainController(MainView mainView) {
		this.mainView = mainView;
		this.quizRes = new QuizRes();
		commandProcessor = new CommandProcessor();
		
		init();
	}
	
	private void init() {		
		QHandler qHandler = new QHandler(quizRes.getQuestionAndAnswer().entrySet().iterator(), quizRes.getCorrectAnswer());
		
		mainView.getQuizPane().getButton().setOnAction(qHandler);
		
		mainView.getSessionChooserPane().getLessonButtonOnActionProperty().set(handler -> { 
			qHandler.handle(null);
			mainView.transition(mainView.getQuizPane());
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
			if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP || key.getSource() instanceof ContextMenu)
				return;
			
			String text = ((TextField)key.getSource()).getText();
			
			AutoCompleteTextField textField = mainView.getManualSessionPane().getAutoCompleteTextField();
			
			if (key.getCode() == KeyCode.ENTER)
			{
				Pane newPane = commandProcessor.process(text);
				if (newPane != null)
				{
					mainView.transition(newPane);
					textField.setEntries(new LinkedList<String>(), new LinkedList<String>());
					textField.updateDisplay();
				}
				return;
			}
			
			LinkedList<String> display = new LinkedList<String>(),
					override = new LinkedList<String>();
			
			String existing = "";
			String[] segments = text.split(" ");
			for (String segment : segments)
			{
				commandProcessor.getSuggestions(existing, segment, text, display, override);
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
	
	private void displayChart(int type, String title, ObservableList data)
	{
		ChartPane pane = new ChartPane(type, title);
		pane.setChartData(data);
		mainView.transition(pane);
	}
	
	private class QHandler implements EventHandler<ActionEvent> {
		
		private Map<String, ArrayList<Integer>> correct;
		private Iterator<Entry<String, ArrayList<String>>> entryIterator;
		
		public QHandler(Iterator<Entry<String, ArrayList<String>>> entryIterator, Map<String, ArrayList<Integer>> correct) {
			this.correct = correct;
			this.entryIterator = entryIterator;
		}

		@Override
		public void handle(ActionEvent event) {
			if (entryIterator.hasNext()) {
				Entry<String, ArrayList<String>> currentEntry = entryIterator.next();
				mainView.getQuizPane().changeQuestion(currentEntry.getKey(), currentEntry.getValue(), correct.get(currentEntry.getKey()));
				mainView.getQuizPane().setHandlersForAnswers(new RadioButtonsHandler(mainView.getQuizPane().getButton()));
			} else {
				System.out.println("No other questions to answer");
			}
		}
		
	}
	
	private static class RadioButtonsHandler implements EventHandler<ActionEvent> {
		
		private Button submitButton;
		
		public RadioButtonsHandler(Button submitButton) {
			this.submitButton = submitButton;
		}

		@Override
		public void handle(ActionEvent event) {
			submitButton.setDisable(((RadioButton) event.getSource()).isDisabled());
		}
		
	}
}
