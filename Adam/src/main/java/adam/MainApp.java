package adam;
	
import adam.controller.MainController;
import adam.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView root = new MainView();
			Scene scene = new Scene(root,500,300);
			root.initComponents();
			
			MainController controller = new MainController(root);
			
			primaryStage.setTitle("Adam - Your Macro-Economics Friend");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
