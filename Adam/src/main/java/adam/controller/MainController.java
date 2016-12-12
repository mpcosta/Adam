package adam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import adam.model.Area;
import adam.view.AutoCompleteTextField;
import adam.view.MainView;
import adam.view.ManualPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class MainController {
	
	private MainView mainView;
	private CommandProcessor commandProcessor;
	
	private Thread commandProcessorThread;

	public MainController(MainView mainView) {
		this.mainView = mainView;
		commandProcessor = new CommandProcessor(mainView);
		commandProcessorThread = null;
		
		init();
	}
	
	private void init() {		
		QHandler qHandler = new QHandler(mainView.getQuizPane().getQuestionAndAnswers().entrySet().iterator(), mainView.getQuizPane().getCorrectAnswers());
		
		mainView.getQuizPane().getButton().setOnAction(qHandler);
		
		mainView.getBackButton().setOnAction(handler -> {
			mainView.transition(mainView.getSessionChooserPane());
		});
		
		mainView.getHelpButton().setOnAction(handler -> {
			mainView.getGlobals().showDialog("Instructions", "", "In order to search for..", null, AlertType.INFORMATION);
		});
		
		mainView.getSessionChooserPane().getLessonButtonOnActionProperty().set(handler -> { 
			if (qHandler.isFinished()) {
				qHandler.restart(mainView.getQuizPane().getQuestionAndAnswers().entrySet().iterator());
				mainView.transition(mainView.getQuizPane());
			} else {
				qHandler.handle(null);
				mainView.transition(mainView.getQuizPane());
			}
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
		
		mainView.getManualSessionPane().getTextInputOnKeyReleasedProperty().set(key ->
		{
			if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP || (commandProcessorThread != null && commandProcessorThread.isAlive()))
				return;
			
			String text = ((TextField)key.getSource()).getText();
			
			AutoCompleteTextField textField = mainView.getManualSessionPane().getAutoCompleteTextField();
			
			if (key.getCode() == KeyCode.ENTER)
			{
				Thread thread = new Thread()
				{
					public void run()
					{
						final Pane newPane = commandProcessor.process(text);
						Platform.runLater(new Runnable()
						{
							public void run()
							{
								mainView.removeLoadingScreen();
								if (newPane != null)
								{
									mainView.transition(newPane);
									textField.setEntries(new LinkedList<String>(), new LinkedList<String>());
									textField.updateDisplay();
								}
							}
						});
					}
				};
				mainView.addLoadingScreen();
				thread.start();
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
		});
		
		ToggleButton advancedToggleButton = mainView.getAdvancedToggleButton();
		advancedToggleButton.setOnAction(handler -> {
			ManualPane manualPane = mainView.getManualSessionPane();
			if (advancedToggleButton.isSelected()) {
				advancedToggleButton.setText(manualPane.switchToAdvancedMode());
			} else {
				advancedToggleButton.setText(manualPane.switchToSimpleMode());
			}
		});
		
		Thread thread = new Thread()
		{
			public void run()
			{
				Area.cacheAllNames();
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						ManualPane manualPane = mainView.getManualSessionPane();
						
						manualPane.setCountries(FXCollections.observableArrayList(Area.getAllNames()));
						manualPane.setIndicators(FXCollections.observableArrayList(CommandProcessor.getAllIndicators()));
						manualPane.setGraphs(FXCollections.observableArrayList(CommandProcessor.getAllGraphs()));
						
					}
				});
			}
		};
		thread.start();
	}
	
	private class QHandler implements EventHandler<ActionEvent> {
		
		private Map<String, ArrayList<Integer>> correct;
		private Iterator<Entry<String, ArrayList<String>>> entryIterator;
		private Entry<String, ArrayList<String>> entry;
		
		public QHandler(Iterator<Entry<String, ArrayList<String>>> entryIterator, Map<String, ArrayList<Integer>> correct) {
			this.correct = correct;
			this.entryIterator = entryIterator;
		}
		
		private boolean correctAnswer = false;
		private boolean firstTime = true;
		private boolean finished = false;

		@Override
		public void handle(ActionEvent event) {
			if (entryIterator.hasNext()) {
				if (firstTime) {
					entry = entryIterator.next();
					mainView.getQuizPane().changeQuestion(entry.getKey(), entry.getValue(), correct.get(entry.getKey()));
					mainView.getQuizPane().setHandlersForAnswers(new RadioButtonsHandler(mainView.getQuizPane().getButton()));
					firstTime = false;
				} else {
					if (correctAnswer) {
						entry = entryIterator.next();
						mainView.getQuizPane().changeQuestion(entry.getKey(), entry.getValue(), correct.get(entry.getKey()));
						mainView.getQuizPane().setHandlersForAnswers(new RadioButtonsHandler(mainView.getQuizPane().getButton()));
						correctAnswer = false;
						mainView.getQuizPane().getButton().setText("Submit");
					} else {
						ArrayList<Integer> selectedAnswers = new ArrayList<Integer>();
						for (int i = 0; i < mainView.getQuizPane().getAnswersList().size(); i++) {
							if (mainView.getQuizPane().getAnswersList().get(i).isSelected()) {
								selectedAnswers.add(i);
							}
						}
						
						if (selectedAnswers.equals(correct.get(entry.getKey()))) {
							entry = entryIterator.next();
							correctAnswer = true;
							mainView.getQuizPane().getButton().setText("Next");
							mainView.getQuizPane().getResLabel().setText("Correct answer!");
						} else {
							correctAnswer = false;
							mainView.getQuizPane().getResLabel().setText("Wrong answer!");
						}
					}
				}
			} else {
				mainView.getQuizPane().getChildren().clear();
				finished = true;
				mainView.getQuizPane().getResLabel().setText("Congratulations, you finished the quiz!");
				mainView.getQuizPane().getChildren().add(mainView.getQuizPane().getResLabel());
			}
		}
		
		public boolean isFinished() {
			return finished;
		}
		
		public void restart(Iterator<Entry<String, ArrayList<String>>> entryIterator) {
			correctAnswer = false;
			firstTime = true;
			this.entryIterator = entryIterator;
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
