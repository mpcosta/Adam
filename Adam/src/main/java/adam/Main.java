package adam;
	
import adam.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MainView root = new MainView();
			Scene scene = new Scene(root,800,560);
			scene.getStylesheets().add(getClass().getResource("view/application.css").toExternalForm());
			root.initComponents();
			
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
