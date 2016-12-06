package adam.view;

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
	
	private SortedSet<String> entries;
	private ContextMenu entriesPopup;
	private LinkedList<String> searchResults;

	public AutoCompleteTextField() {
		super();
		
		entries = new TreeSet<>();
		entriesPopup = new ContextMenu();
		searchResults = new LinkedList<>();
		
		textProperty().addListener((observableValue, oldString, newString) -> {
			if (getText().length() == 0) {
				entriesPopup.hide();
			} else {
				searchResults.clear();
				searchResults.addAll(entries.subSet(getText(), getText() + Character.MAX_VALUE));
				
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
		
		focusedProperty().addListener(listener -> {
			entriesPopup.hide();
		});
	}

	private void populatePopup(List<String> searchResults) {
		List<CustomMenuItem> menuItems = new LinkedList<>();
		
		int maxEntries = 10;
		int count = Math.min(searchResults.size(), maxEntries);
		for (int i = 0; i < count; i++) {
			final String result = searchResults.get(i);
			Label entryLabel = new Label(result);
			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			
			item.setOnAction(handler -> {
				setText(result);
				positionCaret(getText().length());
				entriesPopup.hide();
			});
			
			menuItems.add(item);
		}
		
		entriesPopup.getItems().clear();
		entriesPopup.getItems().addAll(menuItems);
	}

	public SortedSet<String> getEntries() {
		return entries;
	}	
}