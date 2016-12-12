package adam.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * A class that represents the Manual Session Pane.
 * 
 * It is used to construct the display and the controls over the APIs. 
 * @author Team Blue
 */
public class ManualPane extends StackPane {
	
	private AutoCompleteTextField textField;
	private Button submitButton;
	private HBox searchBar;
	private HBox advancedBar;
	
	private ComboBox<String> countryComboBox;
	private ComboBox<String> indicatorComboBox;
	private ComboBox<String> graphTypeComboBox;
	private TextField fromYearField;
	private TextField toYearField;
	
	/**
	 * A constructor that initiates the simple/advanced bars.
	 * Adds the simple bar to this view.
	 */
	public ManualPane() {
		super();
		
		submitButton = new JFXButton("Search");
		submitButton.setStyle(MainView.STYLES.BUTTON_RAISED.toString());
		
		initSimpleBar();
		initAdvancedBar();
		
		advancedBar.getChildren().addAll(countryComboBox, indicatorComboBox, graphTypeComboBox, fromYearField, toYearField, submitButton);
		searchBar.getChildren().addAll(textField, submitButton);
		
		getChildren().add(searchBar);
	}

	/**
	 * A private method that initialises the simple bar components and
	 * adds them to the simple HBox called searchBar.
	 */
	private void initSimpleBar() {
		textField = new AutoCompleteTextField();
		textField.setPromptText("GDP for United States vs United Kingdom from 2010 to 2015 on a line graph");
		textField.setPrefSize(550, 20);
		
		searchBar = new HBox();
		searchBar.setPadding(new Insets(90, 10, 10, 10));
		searchBar.setSpacing(10);
		searchBar.setAlignment(Pos.TOP_CENTER);
	}

	/**
	 * A private method that initialises the advanced bar components and
	 * adds them to the advanced HBox called advancedBar.
	 */
	private void initAdvancedBar() {
		advancedBar = new HBox();
		advancedBar.setPadding(new Insets(90, 10, 10, 10));
		advancedBar.setSpacing(10);
		advancedBar.setAlignment(Pos.TOP_CENTER);
		
		countryComboBox = new JFXComboBox<String>();
		countryComboBox.setPromptText("Countries");
		countryComboBox.setPrefWidth(200);
		
		indicatorComboBox = new JFXComboBox<String>();
		indicatorComboBox.setPromptText("Indicators");
		indicatorComboBox.setPrefWidth(200);
		
		graphTypeComboBox = new JFXComboBox<String>();
		graphTypeComboBox.setPromptText("Chart");
		graphTypeComboBox.setPrefWidth(150);
		
		// Adding and removing dynamically the year field in case
		// a chart pane of type map is required which works
		// by year and not by range like the other cases.
		graphTypeComboBox.setOnAction(handler -> { 
			if (graphTypeComboBox.getSelectionModel().getSelectedIndex() == ChartPane.MAP) {
				fromYearField.setPromptText("Year");
				advancedBar.getChildren().remove(toYearField);
			} else {
				if (!advancedBar.getChildren().contains(toYearField)) {
					fromYearField.setPromptText("From");
					advancedBar.getChildren().add(4, toYearField);
				}
			}
		});
		
		fromYearField = new JFXTextField();
		fromYearField.setPromptText("From");
		fromYearField.setPrefWidth(100);
		
		toYearField = new JFXTextField();
		toYearField.setPromptText("To");
		toYearField.setPrefWidth(100);
	}
	
	/**
	 * A getter for the auto complete text field.
	 * @return An object that represents the auto complete textfield.
	 */
	public AutoCompleteTextField getAutoCompleteTextField() {
		return textField;
	}
	
	/**
	 * A getter for the on key released property.
	 * @return An Object Property that represents the Event Handler on the released key event.
	 */
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyReleasedProperty() {
		return textField.onKeyReleasedProperty();
	}

	/**
	 * A method that switches the top bar of this view to the advanced UI.
	 * @return A String that represents the name of the UI Mode.
	 */
	public String switchToAdvancedMode() {
		// Add the simple search bar
		getChildren().remove(searchBar);
		
		// Add advanced components
		advancedBar.getChildren().add(submitButton);
		getChildren().add(advancedBar);
		
		return "Advanced";
	}

	/**
	 * A method that switches the top bar of this view to the simple UI.
	 * @return A String that represents the name of the UI Mode.
	 */
	public String switchToSimpleMode() {
		// Remove advanced components
		getChildren().remove(advancedBar);
		advancedBar.getChildren().remove(submitButton);
		
		// Add the simple search bar
		searchBar.getChildren().add(submitButton);
		getChildren().add(searchBar);
		
		return "Simple";
	}
	
	/**
	 * A method to set the indicators list into the combobox.
	 * @param indicatorsList Requires an observable list of strings that contains the indicators.
	 */
	public void setIndicators(ObservableList<String> indicatorsList) {
		indicatorComboBox.setItems(indicatorsList);
	}
	
	/**
	 * A method to set the countries list into the combobox.
	 * @param countriesList Requires an observable list of strings that contains the countries.
	 */
	public void setCountries(ObservableList<String> countriesList) {
		countryComboBox.setItems(countriesList);
	}
	
	/**
	 * A method to set the items for the type of charts combo box. 
	 * @param graphList An ObservableList that contains Strings that represent the different type of charts.
	 */
	public void setGraphs(ObservableList<String> graphList) {
		graphTypeComboBox.setItems(graphList);
	}

	/**
	 * A getter for the on action property.
	 * @return An Object Property that represents the Event Handler on the action event for the submit button.
	 */
	public ObjectProperty<EventHandler<ActionEvent>> getSubmitButtonOnActionProperty() {
		return submitButton.onActionProperty();
	}
	
	public ComboBox<String> getCountryComboBox() {
		return countryComboBox;
	}

	public ComboBox<String> getIndicatorComboBox() {
		return indicatorComboBox;
	}
	
	public ComboBox<String> getGraphTypeComboBox() {
		return graphTypeComboBox;
	}

	public StringProperty getFromYearFieldTextInputProperty() {
		return fromYearField.textProperty();
	}

	public StringProperty getToYearFieldTextInputProperty() {
		return toYearField.textProperty();
	}
	
	public boolean isOnAdvancedMode()
	{
		return getChildren().contains(advancedBar);
	}
}
