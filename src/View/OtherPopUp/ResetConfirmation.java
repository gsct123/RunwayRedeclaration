package View.OtherPopUp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Optional;

public class ResetConfirmation {
    public ResetConfirmation() {}

    public boolean confirmReset(){
        boolean flag = false;

        Alert infoAlert = new Alert(Alert.AlertType.CONFIRMATION);
        infoAlert.setTitle("Confirmation");
        infoAlert.setHeaderText("Confirmation");
        infoAlert.setContentText("Warning: system will return to initial default state\nPrevious selections and inputs will be cleared\nAre you sure you want to reset?");
        infoAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");
        Optional<ButtonType> result = infoAlert.showAndWait();

        infoAlert.getDialogPane().sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        e.consume(); // Cancel the Enter key event
                    }
                });
            }
        });

        if(result.isPresent() && result.get() == ButtonType.OK){
            infoAlert.close();
            flag = true;
        } else if(result.isPresent() && result.get() == ButtonType.CANCEL){
            infoAlert.close();
        }

        return flag;
    }
}
