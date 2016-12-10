package adam.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainView extends BorderPane {
	
	private SessionChooserPane sessionChooserPane;
	private ManualPane manualSessionPane;
	private ChartPane chartPane;
	private StackPane topPane;
	private QuizPane quizPane;
	
	private Avatar adam;
	
	private Button backButton;
	private Button helpButton;
	
	private JFXSpinner loadingSpinner;
	
	private Globals globals;
	
	public void initComponents() {
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		topPane = new StackPane();
		quizPane = new QuizPane();
		loadingSpinner = new JFXSpinner();
		chartPane = new ChartPane(ChartPane.LINE, "Chart");
		globals = new Globals();
		
		adam = new Avatar(getScene());
		adam.getListeningImageView().setFitHeight(90);
		
		helpButton = new JFXButton("?");
		StackPane.setAlignment(helpButton, Pos.TOP_RIGHT);
		helpButton.getStyleClass().add("button-help");
		
		backButton = new JFXButton("<");
		StackPane.setAlignment(backButton, Pos.TOP_LEFT);
		backButton.getStyleClass().add("button-back");
		backButton.setVisible(false);
		
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		topPane.getChildren().addAll(backButton, adam.getListeningImageView(), helpButton);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		topPane.getStyleClass().add("top-pane");
		
		BorderPane.setAlignment(topPane, Pos.TOP_CENTER);
		
		topPane.setStyle("-fx-background-color: rgb(230,230,230);");
		
		setCenter(sessionChooserPane);
		setTop(topPane);
	}
	
	public void transition(Pane toView) {
		if (toView instanceof ManualPane) {
			topPane.getChildren().add(0, toView);
			getChildren().remove(sessionChooserPane);
			setCenter(chartPane);
			
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
			getScene().getWindow().centerOnScreen();
			
			backButton.setVisible(true);
			helpButton.setVisible(true);
		}
		else if (toView instanceof QuizPane) {
			setCenter(toView);
			
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
			getScene().getWindow().centerOnScreen();
			
			helpButton.setVisible(false);
			backButton.setVisible(true);
		} else if (toView instanceof SessionChooserPane) {
			getScene().getWindow().setWidth(500);
			getScene().getWindow().setHeight(300);
			getScene().getWindow().centerOnScreen();
			
			if (topPane.getChildren().contains(manualSessionPane)) {
				topPane.getChildren().remove(manualSessionPane);
			}
			
			helpButton.setVisible(true);
			backButton.setVisible(false);
			setCenter(toView);
		}
		else
		{
			setCenter(toView);
		}
	}
	
	public void addLoadingScreen() {
		transition(loadingSpinner);
	}
	
	public void removeLoadingScreen() {
		getChildren().remove(loadingSpinner);
	}
	
	public Button getBackButton() {
		return backButton;
	}
	
	public Button getHelpButton() {
		return helpButton;
	}
	
	public Globals getGlobals() {
		return globals;
	}
	
	public SessionChooserPane getSessionChooserPane() {
		return sessionChooserPane;
	}
	
	public Avatar getAvatar() {
		return adam;
	}
	
	public ManualPane getManualSessionPane() {
		return manualSessionPane;
	}
	
	public QuizPane getQuizPane() {
		return quizPane;
	}
}