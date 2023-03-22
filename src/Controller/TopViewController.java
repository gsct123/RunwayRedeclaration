package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TopViewController implements Initializable {
    @FXML
    private Label leftDesignator;
    @FXML
    private Label rightDesignator;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController.logRunwayItem().addListener((observable, oldValue, newValue) -> {
            String[] split = newValue.split("/");
            leftDesignator.setText(split[0]);
            rightDesignator.setText(split[0].equals(split[1])? split[2]: split[0]);
        });
    }
}
