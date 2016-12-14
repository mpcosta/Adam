package adam;
	
import adam.controller.MainController;
import adam.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A class that represents the Main of this application.
 * 
 * Sets up the model, view and controller and gives the appropriate references where necessary.
 * @author Team Blue
 */
public class MainApp extends Application {
	
	// The title of the application.
	private final String title = "Adam - Your Macro-Economics Friend";
	/**
	 * A method to set the main view 
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView root = new MainView(primaryStage);
			Scene scene = new Scene(root,500,300);
			root.initComponents();
			
			@SuppressWarnings("unused") // TODO: Usable here?
			MainController controller = new MainController(root);
			
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(e -> System.exit(0)); // TODO: Fix why app does not exit without this
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
