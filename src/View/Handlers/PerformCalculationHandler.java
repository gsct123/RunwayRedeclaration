package View.Handlers;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import View.ErrorPopUp.InvalidDistanceThreshold;
import View.OtherPopUp.NoRedeclarationNeeded;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.util.Optional;

public class PerformCalculationHandler {
    public PerformCalculationHandler() {}
    //havent completed
    public void handlingCalcPerformation(){
        if(Calculator.needRedeclare(obstacleSelected, logRunwaySelected)){
            Calculator.calcTora(obstacleSelected, logRunwaySelected);
            Calculator.calcAsda(obstacleSelected, logRunwaySelected);
            Calculator.calcToda(obstacleSelected, logRunwaySelected);
            Calculator.calcLda(obstacleSelected, logRunwaySelected);
            newToraLabel.setText("TORA  =  "+logRunwaySelected.getNewTora());
            newTodaLabel.setText("TODA  =  "+logRunwaySelected.getNewToda());
            newAsdaLabel.setText("ASDA  =  "+logRunwaySelected.getNewAsda());
            newLdaLabel.setText("LDA     =  "+logRunwaySelected.getNewLda());
            editToBeginLabel.setVisible(false);
            noCalcPerformedLabel.setVisible(false);
            breakdownLabel.setText(Calculator.getCalculationBreakdownT(obstacleSelected, logRunwaySelected));
            breakdownLabel.setVisible(true);
        } else{
            breakdownLabel.setVisible(false);
            editToBeginLabel.setVisible(true);
            noCalcPerformedLabel.setVisible(true);
            editToBeginLabel.setText("No runway redeclation needed");
            noCalcPerformedLabel.setText("Original runway parameters can be used");

            newToraLabel.setText("TORA  =  "+logRunwaySelected.getTora());
            newTodaLabel.setText("TODA  =  "+logRunwaySelected.getToda());
            newAsdaLabel.setText("ASDA  =  "+logRunwaySelected.getAsda());
            newLdaLabel.setText("LDA     =  "+logRunwaySelected.getLda());

            new NoRedeclarationNeeded().showNoRedeclarationNeeded();
        }

        String disThreshold = distanceThresholdTextField.getText().trim();
        try {
            distFromThreshold = Double.parseDouble(disThreshold);
            obstacleSelected.setDistFThreshold(distFromThreshold);
        } catch (NumberFormatException exception) {
            //display error message
            new InvalidDistanceThreshold().showDisThresholdError(distanceThresholdTextField);
        }

        String clDistance = clDistTextField.getText();
        try{
            distFromCentreLine = Double.parseDouble(clDistance);
            if(distFromCentreLine < 0){
                throw new NumberFormatException();
            }
            obstacleSelected.setDistFCent(distFromCentreLine);
            performCalculationButton.setDisable(true);
        } catch (NumberFormatException exception){
            //display error message
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Message");
            errorAlert.setHeaderText("ERROR");
            errorAlert.setContentText("Invalid input for distance from centre line\nHint: please input a numerical value greater or equal to 0");
            errorAlert.getDialogPane().lookup(".content.label").setStyle("-fx-font-family: Verdana; -fx-font-size: 14px; -fx-text-fill: red; -fx-line-spacing: 5px");
            Optional<ButtonType> result = errorAlert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK){
                clDistTextField.setText("0");
                errorAlert.close();
            }

            Button okButton = (Button) errorAlert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setOnAction(event -> {
                clDistTextField.setText("0");
                errorAlert.close();
            });
        }


    }
}
