package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.beans.value.ObservableValue;
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
    private Rectangle runway;
    @FXML
    private Rectangle obstacleBlock;
    @FXML
    private Line centreLine;
    @FXML
    private Line gradedAreaLine;
    @FXML
    private Rectangle minCGArea;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController.airportItem().addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        MainController.logRunwayItem().addListener((observable, oldValue, newValue) -> {
            String[] split = (newValue.getDesignator()+"/"+MainController.physRunwayItem().get().getName()).split("/");
            leftDesignator.setText(split[0]);
            rightDesignator.setText(split[0].equals(split[1])? split[2]: split[1]);
        });
        MainController.physRunwayItem().addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        MainController.obstacleProperty.addListener(((ObservableValue<? extends Obstacle> observable, Obstacle oldValue, Obstacle newValue) -> {
            relocateObstacle();
        }));
        MainController.dirFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> {
            relocateObstacle();
        });
        MainController.disFromCentre.addListener((observable, oldValue, newValue) -> {
            relocateObstacle();
        });
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void relocateObstacle(){
        obstacleBlock.setVisible(true);
        Obstacle obstacle = MainController.obstacleProperty.get();
        LogicalRunway logRunway;
        PhysicalRunway physRunway;
        double runwayStartX = runway.getLayoutX();
        double runwayLength = runway.getWidth();
        double centre = centreLine.getLayoutY();
        double disFromThreshold = obstacle.getDistFThreshold();
        double tora;
        double stripEnd;
        double obsBlockLength = obstacleBlock.getWidth();
        double obsBlockWidth = obstacleBlock.getHeight();

        if(MainController.logRunwayItem() != null){
            logRunway = MainController.logRunwayItem().get();
            tora = MainController.logRunwayItem().get().getTora();
            stripEnd = PhysicalRunway.getStripEnd();
            if(Calculator.needRedeclare(obstacle, logRunway)){
                double displacedFromCentre = obstacle.getDirFromCentre().equals("L")? (-obstacle.getDistFCent()*(minCGArea.getHeight()/2)/75)-obsBlockWidth/2: (obstacle.getDistFCent()*(minCGArea.getHeight()/2)/75)-obsBlockLength/2;
                if(disFromThreshold < 0 || disFromThreshold > tora){
                    if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*((minCGArea.getWidth()-runway.getWidth())/2)/stripEnd)-obsBlockWidth, centre+displacedFromCentre);
                    } else{
                        obstacleBlock.relocate(runwayStartX+runwayLength+((disFromThreshold-tora)*((minCGArea.getWidth()-runway.getWidth())/2)/stripEnd), centre+displacedFromCentre);
                    }
                } else{
                    //obstacle x depends on value of distFromThreshold
                    if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora)-obsBlockLength, centre+displacedFromCentre);
                    } else{
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora), centre+(displacedFromCentre));
                    }
                }
            } else{
                double displacedFromCentre = obstacle.getDirFromCentre().equals("L")? (-obstacle.getDistFCent()*(minCGArea.getHeight()/2)/75)-obsBlockWidth/2: (obstacle.getDistFCent()*(minCGArea.getHeight()/2)/75)-obsBlockLength/2;
                if(disFromThreshold < 0 || disFromThreshold > tora){
                    if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*((minCGArea.getWidth()-runway.getWidth())/2)/stripEnd)-obsBlockWidth, centre+displacedFromCentre);
                    } else{
                        obstacleBlock.relocate(runwayStartX+runwayLength+((disFromThreshold-tora)*((minCGArea.getWidth()-runway.getWidth())/2)/stripEnd), centre+displacedFromCentre);
                    }
                } else{
                    //obstacle x depends on value of distFromThreshold
                    if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora)-obsBlockLength, centre+displacedFromCentre);
                    } else{
                        obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora), centre+(displacedFromCentre));
                    }
                }
            }
        }
    }
}
