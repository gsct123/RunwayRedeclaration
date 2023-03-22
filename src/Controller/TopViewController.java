package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TopViewController {
    @FXML
    private Label leftDesignator;
    @FXML
    private Label rightDesignator;

    @FXML
    public void designatorSetter(ActionEvent event){
        MainController.physicalRunwayItem().addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText(newValue);
        });
    }
}
