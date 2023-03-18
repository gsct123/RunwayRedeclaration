package View.Handlers;

import Controller.MainController;
import View.OtherPopUp.ResetConfirmation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ResetHandler {
    public ResetHandler() {}

    public void reset(Stage stage) throws IOException {
        boolean flag = new ResetConfirmation().confirmReset();
        if(flag) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Main.fxml"));
            MainController controller = new MainController();
            loader.setController(controller);
            Parent root = loader.load();

            Scene scene = new Scene(root);

            stage.setTitle("SEG Runway Project");
            stage.setScene(scene);
            stage.show();
        }
    }
}
