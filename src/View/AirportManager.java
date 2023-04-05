package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirportManager extends Application {
    private static Stage classStage;
    private static String username;

    //default constructor
    public AirportManager(){}

    public AirportManager(String username) {
        AirportManager.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static Stage getStage(){
        return classStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        classStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AirportManager.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("SEG Runway Project");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
