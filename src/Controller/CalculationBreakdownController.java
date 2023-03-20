package Controller;

import Model.LogicalRunway;
import Model.PhysicalRunway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculationBreakdownController implements Initializable {
    @FXML
    private Label originalToda;
    @FXML
    private Label originalTora;
    @FXML
    private Label originalLda;
    @FXML
    private Label originalAsda;
    @FXML
    private Label resa;
    @FXML
    private Label stripEnd;
    @FXML
    private Label blastProtection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LogicalRunway runway = MainController.getLogRunwaySelected();
        originalToda.setText("= "+runway.getToda()+" m");
        originalTora.setText("= "+runway.getTora()+" m");
        originalAsda.setText("= "+runway.getAsda()+" m");
        originalLda.setText("= "+runway.getLda()+" m");
        resa.setText("= "+PhysicalRunway.getResa()+" m");
        stripEnd.setText("= "+ PhysicalRunway.getStripEnd()+" m");
        blastProtection.setText("= "+PhysicalRunway.getBlastProtection()+" m");
    }
}
