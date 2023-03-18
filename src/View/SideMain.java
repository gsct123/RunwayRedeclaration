package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SideMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SideView.fxml"));
        //SideViewController sideViewController = new SideViewController();
        //loader.setController(sideViewController);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setTitle("SideViewTest");
        stage.setScene(scene);
        stage.show();
    }
}