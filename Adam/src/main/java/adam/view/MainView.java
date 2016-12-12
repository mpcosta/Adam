package adam.view;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This is the Main View class which extends the BorderPane.
 * It is used to manipulate the view components, setting the objects,
 * taking care of the alignment, transitions and holds all the view components.
 * @author Team Blue
 */
public class MainView extends BorderPane {
	
	/**
	 * An enum that defines the styles for different type
	 * of elements used throughout the view.
	 */
	protected static enum STYLES {
	    BUTTON_RAISED("-fx-font-size: 14px; -fx-button-type: RAISED; -fx-background-color: rgb(77,102,204); -fx-min-width: 80; -fx-text-fill: WHITE;"),
	    BUTTON_BACK("-fx-background-radius: 5em; -fx-font-size: 16px; -fx-button-type: RAISED; -fx-background-color: rgb(222,129,111); -fx-text-fill: WHITE; -fx-font-weight: bold;"),
	    QUESTION_LABEL("-fx-font-size: 16px; -fx-font-weight: bold;")
	    ;

	    private final String text;

	    /**
	     * @param text
	     */
	    private STYLES(final String text) {
	        this.text = text;
	    }

	    @Override
	    public String toString() {
	        return text;
	    }
    }
	
	// Private pane fields used in the view
	private SessionChooserPane sessionChooserPane;
	private ManualPane manualSessionPane;
	private ChartPane chartPane;
	private StackPane topPane;
	private QuizPane quizPane;
	
	// Private field of the avatar
	private Avatar adam;
	
	// Private fields for the buttons
	private Button backButton;
	private ToggleButton advancedToggleButton;
	
	// Private fields for the loading component
	private JFXSpinner loadingSpinner;
	private StackPane loadingPane;
	private Label loadingLabel;
	
	// The primary stage of this application.
	protected static Stage primaryStage;
	
	/**
	 * A constructor which takes a reference to the primary stage of this Application.
	 * @param primaryStage A Stage that represents the primary stage of this Application.
	 */
	public MainView(Stage primaryStage) {
		MainView.primaryStage = primaryStage;
	}
	
	/**
	 * A method used to initialise all the components within this view.
	 */
	public void initComponents() {
		// Creating the panes.
		sessionChooserPane = new SessionChooserPane();
		manualSessionPane = new ManualPane();
		topPane = new StackPane();
		quizPane = new QuizPane();
		chartPane = new ChartPane(ChartPane.LINE, "Chart");
		
		// Creating the loading component
		loadingSpinner = new JFXSpinner();
		loadingLabel = new Label("Loading...");
		loadingLabel.setPadding(new Insets(60,0,0,0));
		loadingPane = new StackPane();
		loadingPane.getChildren().addAll(loadingSpinner, loadingLabel);
		
		// Creating an instance of the avatar.
		adam = new Avatar(getScene());
		
		// Resize the avatar so it fits the screen and align it to the top centre
		adam.getListeningImageView().setFitHeight(90);
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		advancedToggleButton = new JFXToggleButton();
		advancedToggleButton.setText("Simple");
		StackPane.setAlignment(advancedToggleButton, Pos.TOP_RIGHT);
		
		advancedToggleButton.setVisible(false);
		
		// Creating the back button
		// Get the style class from the css resource
		// Align the help button to the top left
		backButton = new JFXButton("<");
		backButton.setStyle(STYLES.BUTTON_BACK.toString());
		
		// Setting the visibility to false
		// (there is no back screen at the start of the program)
		backButton.setVisible(false);
		StackPane.setAlignment(backButton, Pos.TOP_LEFT);
		
		// Adding the components to the topPane and align the pane to the top centre
		topPane.getChildren().addAll(backButton, adam.getListeningImageView(), advancedToggleButton);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		topPane.setStyle("-fx-background-color: rgb(230,230,230);");
		BorderPane.setAlignment(topPane, Pos.TOP_CENTER);
		
		// Setting the centre and the top parts of the border pane.
		setCenter(sessionChooserPane);
		setTop(topPane);
	}
	
	/**
	 * A method used to change the views (mostly the center node 
	 * of what is displayed to the user).
	 * @param toView The Pane used to switch the center node of the application.
	 */
	public void transition(Pane toView) {
		
		// If "toView" is an instance of ManualPane
		// we have to remove the session chooser pane from the center
		// and change it to the new view.
		if (toView instanceof ManualPane) {
			topPane.getChildren().add(0, toView);
			getChildren().remove(sessionChooserPane);
			setCenter(chartPane);
			
			// Resizing the window to a reasonable size.
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
			getScene().getWindow().centerOnScreen();
			
			// Showing helper buttons
			advancedToggleButton.setVisible(true);
			backButton.setVisible(true);
			requestFocus();
		}
		else if (toView instanceof QuizPane) {
			setCenter(toView);
			
			// Resizing the window to a reasonable size.
			getScene().getWindow().setWidth(800);
			getScene().getWindow().setHeight(560);
			getScene().getWindow().centerOnScreen();
			
			// Showing helper buttons
			backButton.setVisible(true);
		} else if (toView instanceof SessionChooserPane) {
			
			// Resizing the window to a reasonable size.
			getScene().getWindow().setWidth(500);
			getScene().getWindow().setHeight(300);
			getScene().getWindow().centerOnScreen();
			
			if (topPane.getChildren().contains(manualSessionPane)) {
				topPane.getChildren().remove(manualSessionPane);
			}
			
			// Showing helper buttons
			advancedToggleButton.setVisible(false);
			backButton.setVisible(false);
			setCenter(toView);
		} else {
			setCenter(toView);
		}
	}
	
	/**
	 * A method that adds the loading screen in the center of the Pane.
	 * It is used to show feedback to the user when loading.
	 */
	public void addLoadingScreen() {
		transition(loadingPane);
	}

	/**
	 * A method that removes the loading screen from the view.
	 */
	public void removeLoadingScreen() {
		getChildren().remove(loadingPane);
	}
	
	/**
	 * A method to set the loading label to appear a text 
	 */
	public void setLoadingLabel(String value) {
		loadingLabel.setText(value);
	}
	
	public ToggleButton getAdvancedToggleButton()
	{
		return advancedToggleButton;
	}
	
	/**
	 * A getter for the back Button that is present when switching the different panes.
	 * @return An object that represents the back Button.
	 */
	public Button getBackButton() {
		return backButton;
	}
	
	/**
	 * This is a getter for the Session Chooser Pane
	 * @return An object that represents an instance of the Session Chooser Pane
	 */
	public SessionChooserPane getSessionChooserPane() {
		return sessionChooserPane;
	}
	
	/**
	 * This is a getter for the avatar (the image from the top part of the view)
	 * @return An object that represents the Avatar (the top image)
	 */
	public Avatar getAvatar() {
		return adam;
	}
	
	/**
	 * This is a getter for the Manual Session Pane.
	 * @return An object that represents an instance of the Manual Pane.
	 */
	public ManualPane getManualSessionPane() {
		return manualSessionPane;
	}
	
	/**
	 * This is a getter for the quiz pane.
	 * @return An object that represents an instance of the quiz pane.
	 */
	public QuizPane getQuizPane() {
		return quizPane;
	}
	
	/**
	 * A method to show a dialog.
	 * @param title The title of the dialog.
	 * @param header The header of the dialog
	 * @param content The content of the dialog.
	 * @param details The details of the dialog.
	 */
	public void showDialog(String title, String header, String content, String details, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		if (details != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.print(details);
			String exceptionText = sw.toString();

			Label label = new Label("Details:");

			TextArea textArea = new TextArea(exceptionText);
			textArea.setEditable(false);
			textArea.setWrapText(true);

			textArea.setMaxWidth(Double.MAX_VALUE);
			textArea.setMaxHeight(Double.MAX_VALUE);
			GridPane.setVgrow(textArea, Priority.ALWAYS);
			GridPane.setHgrow(textArea, Priority.ALWAYS);

			GridPane expContent = new GridPane();
			expContent.setMaxWidth(Double.MAX_VALUE);
			expContent.add(label, 0, 0);
			expContent.add(textArea, 0, 1);

			// Set expandable Exception into the dialog pane.
			alert.getDialogPane().setExpandableContent(expContent);
		}
		
		alert.showAndWait();
	}
}