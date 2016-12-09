package adam.view;

import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class QuizPane extends StackPane {

	private Label questionLabel;
	private Label resLabel;
	private Button submitButton;
	private ArrayList<RadioButton> answersList;
	private VBox questionBox;
	private int currentQuestion;
	
	public QuizPane() {
		currentQuestion = 0;
		questionLabel = new Label();
		resLabel = new Label();
		submitButton = new JFXButton("Submit");
		submitButton.setDisable(true);
		submitButton.getStyleClass().add("button-raised");
		
		answersList = new ArrayList<RadioButton>();
		questionBox = new VBox();
		questionBox.setAlignment(Pos.TOP_CENTER);
		questionBox.setSpacing(10);
		
		getChildren().add(questionBox);
	}
	
	public void changeQuestion(String question, ArrayList<String> answers, ArrayList<Integer> correctAnswerIndex) {
		questionLabel.setText(question);
		
		answersList.clear();
		
		for (int i = 0; i < answers.size(); ++i){
			RadioButton one = new RadioButton(answers.get(i));
			answersList.add(one);
		}
		
		clearQuestionBox();
		
		submitButton.setDisable(true);
	}
	
	public Button getButton () {
		return submitButton;
	}
	
	public int getCurrentQuestionIndex() {
		return currentQuestion;
	}

	private void clearQuestionBox() {
		questionBox.getChildren().clear();
		questionBox.getChildren().add(questionLabel);
		questionBox.getChildren().addAll(answersList);
		questionBox.getChildren().addAll(submitButton, resLabel);
	}
	
	public void setHandlersForAnswers(EventHandler<ActionEvent> handler) {
		for (RadioButton radioButton : answersList) {
			radioButton.setOnAction(handler);
		}
	}
	
}
