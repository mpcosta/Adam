package adam.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jfoenix.controls.JFXTextField;

import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;

public class AutoCompleteTextField extends JFXTextField {

	private LinkedList<String> displayText, overrideText;
	private ContextMenu entriesPopup;

	public AutoCompleteTextField() {
		super();
		
		displayText = new LinkedList<String>();
		overrideText = new LinkedList<String>();
		entriesPopup = new ContextMenu();
		/*
		textProperty().addListener((observableValue, oldString, newString) -> {
			if (getText().length() == 0) {
				entriesPopup.hide();
			} else {
				searchResults.clear();
				searchResults.addAll(entries);
				//searchResults.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
				
				if (entries.size() > 0) {
					populatePopup(searchResults);
					if (!entriesPopup.isShowing()) {
						entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
					}
				} else {
					entriesPopup.hide();
				}
			}
		});
		*/
		focusedProperty().addListener(listener -> {
			entriesPopup.hide();
		});
	}
	
	public void updateDisplay()
	{
		if (getText().length() == 0) {
			entriesPopup.hide();
		} else {
			//searchResults.clear();
			//searchResults.addAll(entries);
			//searchResults.addAll(possibleEntries.subSet(getText(), getText() + Character.MAX_VALUE));
			
			if (displayText.size() > 0) {
				populatePopup();
				if (!entriesPopup.isShowing()) {
					entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
				}
			} else {
				entriesPopup.hide();
			}
		}
	}

	private void populatePopup() {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		
		final int MAX_ENTRIES = 10;
		
		int count = Math.min(displayText.size(), MAX_ENTRIES);
		for (int i = 0; i < count; i++)
		{
			final String newText = overrideText.get(i);
			Label entryLabel = new Label(displayText.get(i));
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			
			item.setOnAction(handler -> {
				setText(newText);
				positionCaret(getText().length());
				entriesPopup.hide();
			});
			
			menuItems.add(item);
		}
		
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);
	}

	public void setEntries(LinkedList<String> display, LinkedList<String> override) {
		displayText = display;
		overrideText = override;
	}
}