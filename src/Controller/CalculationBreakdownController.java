package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;

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
    @FXML
    private TabPane breakdownPane;
    @FXML
    private Label ldaBreakdown;
    @FXML
    private Label toraBreakdown;
    @FXML
    private Label todaBreakdown;
    @FXML
    private Label asdaBreakdown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LogicalRunway runway = MainController.getLogRunwaySelected();
        Obstacle obstacle = MainController.getObstacleSelected();

        originalToda.setText("=  "+runway.getToda()+"  m");
        originalTora.setText("=  "+runway.getTora()+"  m");
        originalAsda.setText("=  "+runway.getAsda()+"  m");
        originalLda.setText("=  "+runway.getLda()+"  m");
        resa.setText("=  "+PhysicalRunway.getResa()+"  m");
        stripEnd.setText("=  "+ PhysicalRunway.getStripEnd()+"  m");
        blastProtection.setText("=  "+PhysicalRunway.getBlastProtection()+"  m");
        if(MainController.needRedeclare()){
            toraBreakdown.setText(Calculator.toraBreakdown(obstacle, runway));
            todaBreakdown.setText(Calculator.todaBreakdown(obstacle, runway));
            ldaBreakdown.setText(Calculator.ldaBreakdown(obstacle, runway));
            asdaBreakdown.setText(Calculator.asdaBreakdown(obstacle, runway));
        } else{
            String s = """
                    No redeclaration needed, no calculation performed.

                         Original runway parameters can be used.""";
            ldaBreakdown.setText(s);
            toraBreakdown.setText(s);
            todaBreakdown.setText(s);
            asdaBreakdown.setText(s);
        }
    }

}
