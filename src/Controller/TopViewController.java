package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class TopViewController implements Initializable {
    private MainController mainController;

    @FXML
    private Label leftDesignator;
    @FXML
    private Label rightDesignator;
    @FXML
    private Rectangle runwayStrip;
    @FXML
    private Rectangle obstacleBlock;
    @FXML
    private Line centreLine;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController.airportItem().addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        MainController.logRunwayItem().addListener((observable, oldValue, newValue) -> {
            String[] split = (newValue.getDesignator()+"/"+MainController.physRunwayItem().getName()).split("/");
            leftDesignator.setText(split[0]);
            rightDesignator.setText(split[0].equals(split[1])? split[2]: split[1]);
        });
        MainController.physRunwayItem().addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        MainController.obstacleProperty().addListener((observable, oldValue, newValue) -> {
            Obstacle obstacle = MainController.obstacleProperty().get();
            LogicalRunway logRunway;
            double runwayStartX = runwayStrip.getX();
            double runwayStartY = runwayStrip.getY();
            double runwayLength = runwayStrip.getWidth();
            double centre = centreLine.getEndY();
            double disFromThreshold = newValue.getDistFThreshold();
            double tora;
            double stripEnd;

            if(MainController.logRunwayItem() != null){
                logRunway = MainController.logRunwayItem().get();
                tora = MainController.logRunwayItem().get().getTora();
                stripEnd = PhysicalRunway.getStripEnd();
                if(Calculator.needRedeclare(obstacle, logRunway)){
                    if(disFromThreshold < 0 || disFromThreshold > tora + stripEnd){
                        //show label instead of obstacle
                    } else{
                        //obstacle x depends on value of distFromThreshold
                        if(Calculator.getFlightMethod(obstacle, logRunway).equals("talo")){

                        } else{
                            obstacleBlock.relocate();
                        }
                    }
                }
            }
        });
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }
}
