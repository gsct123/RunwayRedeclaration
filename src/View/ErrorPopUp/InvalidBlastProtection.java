package View.ErrorPopUp;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Optional;

public class InvalidBlastProtection {
    public InvalidBlastProtection() {}

    public void showError(TextField blastProtectionField){
        String errorSoundFile = "/Users/gavinteo/IdeaProjects/RunwayRedeclaration/src/View/Sound/ErrorSound.mp3";
        Media sound = new Media(new File(errorSoundFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error Message");
        errorAlert.setHeaderText("ERROR");
        errorAlert.setContentText("Invalid input for blast protection\nHint: please input a numerical value within this range: 300-500 (for safety purpose)");
        errorAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");
        Optional<ButtonType> result = errorAlert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            blastProtectionField.setText("300");
            errorAlert.close();
        }

        Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            blastProtectionField.setText("300");
            errorAlert.close();
        });
    }
}
