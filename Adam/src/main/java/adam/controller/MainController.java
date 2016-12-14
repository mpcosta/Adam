package adam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import adam.model.Area;
import adam.model.speech.Speech;
import adam.view.AutoCompleteTextField;
import adam.view.MainView;
import adam.view.ManualPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class MainController {
	/**
	 * A main view of the application 
	 * A command processor for all functions regarding the search bar 
	 * Speech for Adam the Avatar!
	 */
	private MainView mainView;
	private CommandProcessor commandProcessor;
	private Speech speech;
	
	private Thread commandProcessorThread;
/**
	 * A constructor for the main view, command processor, and speech 
	 * @param mainView
	 */
	public MainController(MainView mainView) {
		this.mainView = mainView;
		commandProcessor = new CommandProcessor(mainView);
		commandProcessorThread = null;
		speech = new Speech();
		
		init();
	}
	/**
	 * A main controller method for all that happens throughout the application 
	 */
	private void init() {
		/**
		 * For Adam the avatar, 
		 * When the user clicks on the Avatar it will listen to what the User commands 
		 * Locates Adam to the top of the pane 
		 */
		mainView.getAvatar().getStaticImage().setOnMouseClicked(handler -> {
			Thread thread = new Thread() {
				public void run() {
					String result = speech.listenVoiceToString();
					
					speech.speakMessage(result);
					
					if (result.contains("exit")) {
						System.exit(1);
					}
				}
			};
			thread.start();
		});
		/**
		 * A question Handler to get the questions, answers, and correct answers and add it to the main View 	
		 */	
		QHandler qHandler = new QHandler(mainView.getQuizPane().getQuestionAndAnswers().entrySet().iterator(), mainView.getQuizPane().getCorrectAnswers());
		
		mainView.getQuizPane().getButton().setOnAction(qHandler);
		/**
		 * A back button for the user to transition between different panes and the home page 
		 */
		mainView.getBackButton().setOnAction(handler -> {
			mainView.transition(mainView.getSessionChooserPane());
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
		/**
		 * Sets up the command processor on the main view 
		 * Retrieves the source to the text field 
		 */
		mainView.getManualSessionPane().getTextInputOnKeyReleasedProperty().set(key ->
		{
			if (key.getCode() == KeyCode.DOWN || key.getCode() == KeyCode.UP || (commandProcessorThread != null && commandProcessorThread.isAlive()))
				return;
			
			AutoCompleteTextField textField = (AutoCompleteTextField)key.getSource();
			String text = textField.getText();
			
			if (key.getCode() == KeyCode.ENTER)
			{
				processRequest(key);
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
		/**
		 * A toggle button to transition between advanced and simple mode for the graphs/charts to be displayed 
		 */
		ToggleButton advancedToggleButton = mainView.getAdvancedToggleButton();
		advancedToggleButton.setOnAction(handler -> {
			ManualPane manualPane = mainView.getManualSessionPane();
			if (advancedToggleButton.isSelected()) {
				advancedToggleButton.setText(manualPane.switchToAdvancedMode());
			} else {
				advancedToggleButton.setText(manualPane.switchToSimpleMode());
			}
		});
		/**
		 * For the countries, indicators, and graphs to be set by the user in the simple mode 
		 */
		mainView.getManualSessionPane().getSubmitButtonOnActionProperty().set(event ->
		{
			processRequest(event);
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
	/**
	 * A method containing the simple mode view with combo boxes for Country, Graph, Indicator, Year Range 
	 * A void method once the user presses submit to have the loading screen appear until graph/chart is ready to appear on the screen 
	 * Reads the user when they input the year range in the advanced mode 
	 * Constructs the selection model for country combo box, indication combo box, and graph type combo box 
	 * @param event
	 */
	private void processRequest(Event event)
	{
		ManualPane manualPane = mainView.getManualSessionPane();
		AutoCompleteTextField textField;
		String text_construct;
		final boolean advanced = manualPane.isOnAdvancedMode();
		if (advanced)
		{
			textField = null;
			text_construct = manualPane.getCountryComboBox().getSelectionModel().getSelectedItem() + " "
					+ manualPane.getIndicatorComboBox().getSelectionModel().getSelectedItem() + " ";
			String chart = manualPane.getGraphTypeComboBox().getSelectionModel().getSelectedItem();
			text_construct += chart + " ";
			if (chart.contains("map"))
				text_construct += manualPane.getFromYearFieldTextInputProperty().get();
			else
				text_construct += manualPane.getFromYearFieldTextInputProperty().get() + " to " + manualPane.getToYearFieldTextInputProperty().get();
		}
		else
		{
			textField = manualPane.getAutoCompleteTextField();
			text_construct = textField.getText();
		}
		final String text = text_construct;
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
						if (newPane == null)
							return;
						mainView.transition(newPane);
						if (!advanced)
						{
							textField.setEntries(new LinkedList<String>(), new LinkedList<String>());
							textField.updateDisplay();
						}
					}
				});
			}
		};
		mainView.addLoadingScreen();
		thread.start();
	}
	/**
	 * A method for the Question Handler 
	 * Adds the iterator, questions, and correct answers 
	 * @author hagerabdo
	 *
	 */
	private class QHandler implements EventHandler<ActionEvent> {
		
		private Map<String, ArrayList<Integer>> correct;
		private Iterator<Entry<String, ArrayList<String>>> entryIterator;
		private Entry<String, ArrayList<String>> entry;
		
		public QHandler(Iterator<Entry<String, ArrayList<String>>> entryIterator, Map<String, ArrayList<Integer>> correct) {
			this.correct = correct;
			this.entryIterator = entryIterator;
		}
		/**
		 * Private booleans to set the correct answer for the first question of the multiple choice to false and the first question to true 
		 */
		private boolean correctAnswer = false;
		private boolean firstTime = true;
		private boolean finished = false;
/**
		 * For the first question of the multiple choice, be set to Feedback until the User chooses the correct answer then the button to switch to Next 
		 * After the user will be able to go to the next question 
		 * Depending on what the user picks as the answers in oder to match with the correct actual answer 
		 * If able to match then user is able to click to next question if not message will appear "Wrong Answer"
		 * The quiz pane will clear after every question to have a new question appear 
		 * When the user is done with the quiz a finished message will appear 
		 */
		@Override
		public void handle(ActionEvent event) {
			if (entryIterator.hasNext()) {
				if (firstTime) {
					entry = entryIterator.next();
					mainView.getQuizPane().changeQuestion(entry.getKey(), entry.getValue(), correct.get(entry.getKey()));
					mainView.getQuizPane().setHandlersForAnswers(new CheckBoxesHandler(mainView.getQuizPane().getButton()));
					firstTime = false;
				} else {
					if (correctAnswer) {
						entry = entryIterator.next();
						mainView.getQuizPane().changeQuestion(entry.getKey(), entry.getValue(), correct.get(entry.getKey()));
						mainView.getQuizPane().setHandlersForAnswers(new CheckBoxesHandler(mainView.getQuizPane().getButton()));
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
		/**
		 * A boolean to check whether quiz is finished 
		 * @return whether quiz is finished 
		 */
		public boolean isFinished() {
			return finished;
		}
		/**
		 * A restart method for the every question 
		 * @param entryIterator
		 */
		public void restart(Iterator<Entry<String, ArrayList<String>>> entryIterator) {
			correctAnswer = false;
			firstTime = true;
			this.entryIterator = entryIterator;
		}
	}
	/**
	 * Submit buttons set to be disabled until a user chooses an answer then will be enabled  
	 * @author hagerabdo
	 *
	 */
	private static class CheckBoxesHandler implements EventHandler<ActionEvent> {
		
		private Button submitButton;
		
		public CheckBoxesHandler(Button submitButton) {
			this.submitButton = submitButton;
		}

		@Override
		public void handle(ActionEvent event) {
			submitButton.setDisable(((CheckBox) event.getSource()).isDisabled());
		}
		
	}
}
