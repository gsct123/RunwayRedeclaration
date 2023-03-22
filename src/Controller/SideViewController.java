package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class SideViewController {

    public Rectangle clearwayL;
    public Rectangle stopwayL;
    public Rectangle phyRunway;
    public Rectangle clearwayR;
    public Label designatorL;
    public Line thresholdL;
    public Label designatorR;
    public Line toraEnd;
    public Line thresholdR;
    public Line ldaStart;
    public Line asdaStart;
    public Line ldaEnd;
    public Line asdaEnd;
    public Line toraStart;
    public Line todaStart;
    public Line todaEnd;
    public Line toraLength;
    public Line ldaLength;
    public Line asdaLength;
    public Line todaLength;
    public Line displacedThresholdL;
    public Label ldaLabel;
    public Label toraLabel;
    public Label asdaLabel;
    public Label todaLabel;
    public Polygon tocsSlope;
    public Rectangle stopwayR;
    public Polygon alsSlope;
    public Line displacedThresholdR;
    public Button button;


    LogicalRunway lR09L = new LogicalRunway("09L",3902,3902,3902,3595);
    //LogicalRunway lR09L = new LogicalRunway("09L",2400,2800,2600,2200);
    LogicalRunway lR27R = new LogicalRunway("27R",3884,3962,3884,3884);
    Obstacle obstacle = new Obstacle("obstacle1",12,10,0,-50);
    ObservableList<LogicalRunway> logicalRunways = FXCollections.observableArrayList(List.of(lR09L,lR27R));
    PhysicalRunway physicalRunway = new PhysicalRunway("Runway1",logicalRunways);

    private void setNewTora(Obstacle obstacle,LogicalRunway logicalRunway){
        double oriTora = logicalRunway.getTora();
        double newTora = logicalRunway.getNewTora();

        double newToraStartPx = getNumberOfPx(oriTora - newTora,logicalRunway);
        //double newToraEndPx = thresholdR.getLayoutX();
        double newLength = getNumberOfPx(newTora,logicalRunway);

        toraStart.setLayoutX(toraStart.getLayoutX() + newToraStartPx);
        toraLength.setLayoutX(toraLength.getLayoutX() + newToraStartPx);
        toraLength.setEndX(toraLength.getEndX() - newToraStartPx);


    }

    @FXML
    private void initialize() {
        setUpPhyRunway(physicalRunway);
        setUpStopwayAndClearway(physicalRunway);
        setUpLogicalRunway(lR09L);
        alsSlope.setVisible(false);
        tocsSlope.setVisible(false);
    }
    @FXML
    private void setUpLogicalRunway(LogicalRunway logicalRunway){
        setUpTora(logicalRunway);
        setUpLda(logicalRunway);
        setUpAsda(logicalRunway);
        setUpToda(logicalRunway);
    }

    @FXML
    private void handleExecuteButtonClick(ActionEvent event){
        Calculator.performCalc(obstacle,lR09L);
        setUpAlsTocs(obstacle,lR09L);
        setNewTora(obstacle,lR09L);
    }

    private void setUpTora(LogicalRunway logicalRunway){
        double originalStartX = thresholdL.getLayoutX();
        double originalEndX = thresholdR.getLayoutX();
        double tora = logicalRunway.getTora();
        //Reset the starting line
        toraStart.setLayoutX(originalStartX);
        toraEnd.setLayoutX(originalEndX);
        toraLength.setLayoutX(originalStartX);
        toraLength.setEndX(originalEndX - originalStartX);
        toraLabel.setText(" TORA = " + tora);
    }

    private void setUpLda(LogicalRunway logicalRunway){
        double originalStartX;
        double originalEndX = thresholdR.getLayoutX();
        double lda = logicalRunway.getLda();

        //Make displacedThreshold the starting point if it exists
        if (logicalRunway.getDisplacedThreshold() != 0){
            originalStartX = displacedThresholdL.getLayoutX();
        }else {
            originalStartX = thresholdL.getLayoutX();
        }
        //Reset the starting line
        ldaStart.setLayoutX(originalStartX);
        ldaEnd.setLayoutX(originalEndX);
        ldaLength.setLayoutX(originalStartX);
        ldaLength.setEndX(originalEndX - originalStartX);
        ldaLabel.setText(" LDA = " + lda);
    }

    private void setUpAsda(LogicalRunway logicalRunway){
        //Set Up Variables
        double originalStartX = thresholdL.getLayoutX();
        double originalEndX;
        double asda = logicalRunway.getAsda();
        if (logicalRunway.getStopway() != 0){
            originalEndX = stopwayR.getLayoutX()+stopwayR.getWidth();
        }else {
            originalEndX = thresholdR.getLayoutX();
        }

        asdaStart.setLayoutX(originalStartX);
        asdaEnd.setLayoutX(originalEndX);
        asdaLength.setLayoutX(originalStartX);
        asdaLength.setEndX(originalEndX - originalStartX);
        asdaLabel.setText(" ASDA = " + asda);
    }
    private void setUpToda(LogicalRunway logicalRunway){
        //Set Up Variables
        double originalStartX = thresholdL.getLayoutX();
        double originalEndX;
        double toda = logicalRunway.getToda();
        if (logicalRunway.getClearway() != 0){
            originalEndX = clearwayR.getLayoutX()+clearwayR.getWidth();
        }else {
            originalEndX = thresholdR.getLayoutX();
        }

        todaStart.setLayoutX(originalStartX);
        todaEnd.setLayoutX(originalEndX);
        todaLength.setLayoutX(originalStartX);
        todaLength.setEndX(originalEndX - originalStartX);
        todaLabel.setText(" TODA = " + toda);
    }

    private void setUpPhyRunway(PhysicalRunway physicalRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);

        //Set Up Threshold
        thresholdL.setLayoutX(phyRunway.getLayoutX());
        thresholdR.setLayoutX(phyRunway.getLayoutX()+phyRunway.getWidth());

        //Set Up Designator
        String lDesignator = lLogicalRunway.getDesignator();
        String rDesignator = rLogicalRunway.getDesignator();
        designatorL.setText(lDesignator);
        designatorR.setText(rDesignator);

        //Set Up DisplacedThreshold
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double oriLDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
        double oriRDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);
        displacedThresholdL.setLayoutX(oriLDisplacedThresholdX);
        displacedThresholdR.setLayoutX(oriRDisplacedThresholdX);
    }

    private void setUpStopwayAndClearway(PhysicalRunway physicalRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        setStopClearway(rLogicalRunway,"Left");
        setStopClearway(lLogicalRunway,"Right");
    }

    private void setStopClearway(LogicalRunway logicalRunway,String leftOrRightWay){
        //Set Up  Variable
        double stopwayLength = logicalRunway.getStopway();
        double stopwayWidthPx = getNumberOfPx(stopwayLength,logicalRunway);
        double clearwayLength = logicalRunway.getClearway();
        double clearwayWidthPx = getNumberOfPx(clearwayLength,logicalRunway);
        double oriStopwayX;
        double oriClearwayX;
        Rectangle stopway;
        Rectangle clearway;

        //Set Up Variable based on left or right
        if (leftOrRightWay.equals("Left")){
            oriStopwayX = thresholdL.getLayoutX() - stopwayWidthPx;
            oriClearwayX = thresholdL.getLayoutX() - clearwayWidthPx;
            stopway = stopwayL;
            clearway = clearwayL;
        } else {
            oriStopwayX = thresholdR.getLayoutX();
            oriClearwayX = thresholdR.getLayoutX();
            stopway = stopwayR;
            clearway = clearwayR;
        }

        // Set Values for both clearway and stopway
        setStopClearwayValue(stopway,stopwayLength,stopwayWidthPx,oriStopwayX);
        setStopClearwayValue(clearway,clearwayLength,clearwayWidthPx,oriClearwayX);
    }

    private void setStopClearwayValue(Rectangle way,double length, double widthPx, double oriWayX){
        if (length != 0 ){
            way.setWidth(widthPx);
            way.setLayoutX(oriWayX);
        }else {
            way.setWidth(0);
        }
    }

    private void setUpAlsTocs(Obstacle obstacle, LogicalRunway logicalRunway){
        double obsHeight = obstacle.getHeight();
        double slopeWidth = obstacle.getAlsTocs();
        double distanceFromThreshold = obstacle.getDistFThreshold();

        //Get Number of Pixel
        double slopeWidthPx = getNumberOfPx(slopeWidth,logicalRunway);
        double obsHeightPx = getNumberOfPx(obsHeight,logicalRunway);
        double distanceFromThresholdPx = getNumberOfPx(distanceFromThreshold,logicalRunway);

        Polygon slope;

        if (usingAls(obstacle,logicalRunway)){
            alsSlope.setVisible(true);
            slope = alsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)+slopeWidthPx);
        }else {
            tocsSlope.setVisible(true);
            slope = tocsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)+slopeWidthPx);
        }
    }

    private boolean usingAls(Obstacle obstacle, LogicalRunway logicalRunway){
        return obstacle.getDistFThreshold() <= (logicalRunway.getTora()/2);
    }

    private double getMeterPerPx(LogicalRunway logicalRunway){
        double ans = logicalRunway.getTora()/ phyRunway.getWidth();
        System.out.println("Number of meter in every pixel = " + ans + " meter");
        return ans;
    }

    private double getNumberOfPx(double length,LogicalRunway logicalRunway){
        double ans = length/getMeterPerPx(logicalRunway);
        //System.out.println(length + " meter = " + ans + " pixel");
        return ans;
    }

}
