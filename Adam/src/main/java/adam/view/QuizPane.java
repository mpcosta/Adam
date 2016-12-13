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
	
	public void changeQuestion(String question, ArrayList<String> answers, ArrayList<Integer> correctAnswerIndices) {
		questionLabel.setText(question);
		resLabel.setText("Feedback");
		
		answersList.clear();
		
		for (int i = 0; i < answers.size(); ++i){
			CheckBox one = new CheckBox(answers.get(i));
			
			one.setWrapText(true);
			
			answersList.add(one);
		}
		
		clearQuestionBox();
		
		submitButton.setDisable(true);
	}
	
	public Button getButton () {
		return submitButton;
	}
	
	public Label getResLabel() {
		return resLabel;
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
		for (CheckBox checkBox : answersList) {
			checkBox.setOnAction(handler);
		}
	}
	
	public ArrayList<CheckBox> getAnswersList() {
		return answersList;
	}
	
}
