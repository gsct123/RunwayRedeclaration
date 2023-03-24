package View.OtherPopUp;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import java.util.Optional;

public class ObstacleOutOfView {
    public ObstacleOutOfView() {}

    public void obstacleNotShow(Rectangle obstacleBlock) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Information");
        infoAlert.setHeaderText("INFO");
        infoAlert.setContentText("Obstacle is outside runway strip and thus not shown");
        infoAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-line-spacing: 5px");

        // Add key event filter to intercept Enter key press events
        infoAlert.getDialogPane().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Platform.runLater(() -> obstacleBlock.setVisible(false));
            }
        });

        Optional<ButtonType> result = infoAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            Platform.runLater(() -> obstacleBlock.setVisible(false));
        }
    }
}
