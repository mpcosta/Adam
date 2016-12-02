package adam.view;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {
	
	private SessionChooserPane sessionChooserPane;

	public MainView() {
		super();
		
		sessionChooserPane = new SessionChooserPane();
		setCenter(sessionChooserPane);
		
//        MenuButton menuButton = new MenuButton();
//        menuButton.getItems().addAll(
//                Stream.of("Sessions", "Lesson", "Manual", "Quiz")
//                    .map(MenuItem::new).collect(Collectors.toList()));
//        
//        setLeft(menuButton);
	}
	
}
