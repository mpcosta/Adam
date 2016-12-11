package adam.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXToggleButton;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * This is the Main View class which extends the BorderPane.
 * It is used to manipulate the view components, setting the objects,
 * taking care of the alignment, transitions and holds all the view components.
 * @author Team Blue
 */
public class MainView extends BorderPane {
	
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
	private Button helpButton;
	private ToggleButton advancedToggleButton;
	
	// Private fields for the loading component
	private JFXSpinner loadingSpinner;
	private StackPane loadingPane;
	private Label loadingLabel;
	
	// Private field for the globals object
	private Globals globals;
	
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
		loadingLabel = new Label("%");
		loadingLabel.setPadding(new Insets(60,0,0,0));
		loadingPane = new StackPane();
		loadingPane.getChildren().addAll(loadingSpinner, loadingLabel);
		
		// Creating an object for the global methods within the view.
		globals = new Globals();
		
		// Creating an instance of the avatar.
		adam = new Avatar(getScene());
		
		// Resize the avatar so it fits the screen and align it to the top centre
		adam.getListeningImageView().setFitHeight(90);
		StackPane.setAlignment(adam.getListeningImageView(), Pos.TOP_CENTER);
		
		helpButton = new JFXButton("?"); // TODO: delete the help button 
		
		advancedToggleButton = new JFXToggleButton();
		advancedToggleButton.setText("Simple");
		StackPane.setAlignment(advancedToggleButton, Pos.TOP_RIGHT);
		// TODO: move this in the controller
		advancedToggleButton.setOnAction(handler -> {
			if (advancedToggleButton.isSelected()) {
				advancedToggleButton.setText(manualSessionPane.switchToAdvancedMode());
			} else {
				advancedToggleButton.setText(manualSessionPane.switchToSimpleMode());
			}
		});
		
		advancedToggleButton.setVisible(false);
		
		// Creating the back button
		// Get the style class from the css resource
		// Align the help button to the top left
		backButton = new JFXButton("<");
		backButton.getStyleClass().add("button-back");
		
		// Setting the visibility to false
		// (there is no back screen at the start of the program)
		backButton.setVisible(false);
		StackPane.setAlignment(backButton, Pos.TOP_LEFT);
		
		// Adding the components to the topPane and align the pane to the top centre
		topPane.getChildren().addAll(backButton, adam.getListeningImageView(), advancedToggleButton);
		topPane.setPadding(new Insets(10, 10, 10, 10));
		topPane.getStyleClass().add("top-pane");
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
	public void setLoadingLabel(int value) {
		loadingLabel.setText(Integer.toString(value));
	}
	
	/**
	 * A getter for the back Button that is present when switching the different panes.
	 * @return An object that represents the back Button.
	 */
	public Button getBackButton() {
		return backButton;
	}
	
	/**
	 * A getter for the help Button present in the manual pane.
	 * @return An object that represents the help Button. 
	 */
	public Button getHelpButton() {
		return helpButton;
	}
	
	/**
	 * A getter for the globals object which holds global methods within the view.
	 * @return An object that represents the globals class of the view.
	 */
	public Globals getGlobals() {
		return globals;
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
}