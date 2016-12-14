package adam.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class QuizPane extends StackPane {
/**
	 * Variables for the labels and boxes related to the multiple choice questions 
	 */
	private Label questionLabel;
	private Label resLabel;
	private Button submitButton;
	private ArrayList<CheckBox> answersList;
	private VBox questionBox;
	private int currentQuestion;
	private Questions questionsRes;
	
	public HashMap<String, ArrayList<String>> getQuestionAndAnswers() {
		return questionsRes.getQuestionAndAnswers();
	}

	public HashMap<String, ArrayList<Integer>> getCorrectAnswers() {
		return questionsRes.getCorrectAnswers();
	}
	/**
	 * A constructor to set the question label on the main view and pad it to the center 
	 * Sets the submit button to be located below the question and disables it 
	 * Calls a list of radio buttons and saves it as answers list 
	 * Sets the question box as a vertical box and aligns it to the center 
	 */
	public QuizPane() {
		currentQuestion = 0;
		questionsRes = new Questions();
		
		questionLabel = new Label();
		questionLabel.setWrapText(true);
		questionLabel.setPadding(new Insets(20, 40, 20, 40));
		questionLabel.setStyle(MainView.STYLES.QUESTION_LABEL.toString());
		
		resLabel = new Label();
		submitButton = new JFXButton("Submit");
		submitButton.setDisable(true);
		submitButton.setStyle(MainView.STYLES.BUTTON_RAISED.toString());
		
		answersList = new ArrayList<CheckBox>();
		questionBox = new VBox();
		questionBox.setAlignment(Pos.TOP_CENTER);
		questionBox.setSpacing(10);
		questionBox.setPadding(new Insets(10, 40, 10, 40));
		
		getChildren().add(questionBox);
	}
	/**
	 * A method to change the question by clearing the current question and reseting the submit button to be disabled 
	 * Sets the resLabel to Feedback 
	 * @param question
	 * @param answers
	 * @param correctAnswerIndices
	 */
	public void changeQuestion(String question, ArrayList<String> answers, ArrayList<Integer> correctAnswerIndices) {
		questionLabel.setText(question);
		resLabel.setText("Feedback");
		
		answersList.clear();
		/**
		 * To space the answers and retrieve the specific number of answers for each question from the Questions class 
		 */
		for (int i = 0; i < answers.size(); ++i){
			CheckBox one = new CheckBox(answers.get(i));
			
			one.setWrapText(true);
			
			answersList.add(one);
		}
		
		clearQuestionBox();
		
		submitButton.setDisable(true);
	}
	/**
	 * A getter for the submit button for the question 
	 * @return the submit button 
	 */
	public Button getButton () {
		return submitButton;
	}
	/**
	 * A getter for the resLabel 
	 * @return the resLabel 
	 */
	public Label getResLabel() {
		return resLabel;
	}
	/**
	 * A getter for the current question being displayed to the user 
	 * @return the current question 
	 */
	public int getCurrentQuestionIndex() {
		return currentQuestion;
	}
/**
	 * A method to clear the question and all that apply to it 
	 */
	private void clearQuestionBox() {
		questionBox.getChildren().clear();
		questionBox.getChildren().add(questionLabel);
		questionBox.getChildren().addAll(answersList);
		questionBox.getChildren().addAll(submitButton, resLabel);
	}
	/**
	 * An action handler for the collection of radio buttons in the answer list for each question 
	 * @param handler
	 */
	public void setHandlersForAnswers(EventHandler<ActionEvent> handler) {
		for (CheckBox checkBox : answersList) {
			checkBox.setOnAction(handler);
		}
	}
	/**
	 * A getter method for the answers list as an array list of check boxes 
	 * @return the answers list 
	 */
	public ArrayList<CheckBox> getAnswersList() {
		return answersList;
	}
	
}
