package adam.view;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class QuizPane extends StackPane {

	private Label questionLabel;
	private Label resLabel;
	private Button submitButton;
	private ArrayList<RadioButton> answersList;
	private ToggleGroup answersGroup;
	private VBox questionBox;
	
	public QuizPane() {
		questionLabel = new Label();
		resLabel = new Label();
		submitButton = new Button("Submit");
		submitButton.setDisable(true);
		
		answersGroup = new ToggleGroup();
		
		for (RadioButton rb : answersList) {
			rb = new RadioButton();
			rb.setToggleGroup(answersGroup);
		}
		
		questionBox = new VBox();
		
		getChildren().add(questionBox);
	}
	
	public void changeQuestion(String question, ArrayList<String> answers, ArrayList<Integer> correctAnswerIndex) {
		questionLabel.setText(question);
		
		for (int i = 0; i < answersList.size(); i++) {
			answersList.get(i).setText(answers.get(i));
		}
		
		submitButton.setDisable(true);
	}
	
}
