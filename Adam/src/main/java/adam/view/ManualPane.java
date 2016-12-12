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
	
	public ManualPane() {
		super();
		
		submitButton = new JFXButton("Search");
		submitButton.getStyleClass().add("button-raised");
		
		initSimpleBar();
		initAdvancedBar();
		
		advancedBar.getChildren().addAll(countryComboBox, indicatorComboBox, fromYearField, toYearField, graphTypeComboBox, submitButton);
		searchBar.getChildren().addAll(textField, submitButton);
		
		getChildren().add(searchBar);
	}

	private void initSimpleBar() {
		textField = new AutoCompleteTextField();
		textField.setPromptText("GDP for United States vs United Kingdom from 2010 to 2015 on a line graph");
		textField.setPrefSize(500, 20);
		
		searchBar = new HBox();
		searchBar.setPadding(new Insets(90, 10, 10, 10));
		searchBar.setSpacing(10);
		searchBar.setAlignment(Pos.TOP_CENTER);
	}

	private void initAdvancedBar() {
		advancedBar = new HBox();
		advancedBar.setPadding(new Insets(90, 10, 10, 10));
		advancedBar.setSpacing(10);
		advancedBar.setAlignment(Pos.TOP_CENTER);
		
		countryComboBox = new JFXComboBox<String>();
		countryComboBox.setPromptText("Countries");
		indicatorComboBox = new JFXComboBox<String>();
		indicatorComboBox.setPromptText("Indicators");	
		graphTypeComboBox = new JFXComboBox<String>();
		graphTypeComboBox.setPromptText("Graph type");
		fromYearField = new JFXTextField();
		fromYearField.setPromptText("From");
		fromYearField.setPrefWidth(50);
		toYearField = new JFXTextField();
		toYearField.setPromptText("To");
		toYearField.setPrefWidth(50);
	}
	
	public AutoCompleteTextField getAutoCompleteTextField() {
		return textField;
	}
	
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyPressedProperty() {
		return textField.onKeyPressedProperty();
	}
	
	public ObjectProperty<EventHandler<? super KeyEvent>> getTextInputOnKeyReleasedProperty() {
		return textField.onKeyReleasedProperty();
	}
	
	public ObjectProperty<EventHandler<ActionEvent>> getOnActionProperty() {
		return submitButton.onActionProperty();
	}

	public String switchToAdvancedMode() {
		// Add the simple search bar
		getChildren().remove(searchBar);
		
		// Add advanced components
		advancedBar.getChildren().add(submitButton);
		getChildren().add(advancedBar);
		
		return "Advanced";
	}

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

	public ObjectProperty<EventHandler<ActionEvent>> getSubmitButtonOnActionProperty() {
		return submitButton.onActionProperty();
	}

	public ComboBox<String> getCountryComboBox() {
		return countryComboBox;
	}

	public ComboBox<String> getIndicatorComboBox() {
		return indicatorComboBox;
	}

	public StringProperty getFromYearFieldTextInputProperty() {
		return fromYearField.textProperty();
	}

	public StringProperty getToYearField() {
		return toYearField.textProperty();
	}
}
