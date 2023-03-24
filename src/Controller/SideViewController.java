package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SideViewController {

    @FXML
    protected Line blastProtectionStart;
    @FXML
    protected Line blastProtectionLength;
    @FXML
    protected Line blastProtectionEnd;
    @FXML
    protected Label blastProtectionLabel;
    @FXML
    protected Rectangle clearwayL;
    @FXML
    protected Rectangle stopwayL;
    @FXML
    protected Rectangle phyRunway;
    @FXML
    protected Rectangle clearwayR;
    @FXML
    protected Label designatorL;
    @FXML
    protected Line thresholdL;
    @FXML
    protected Label designatorR;
    @FXML
    protected Line toraEnd;
    @FXML
    protected Line thresholdR;
    @FXML
    protected Line ldaStart;
    @FXML
    protected Line asdaStart;
    @FXML
    protected Line ldaEnd;
    @FXML
    protected Line asdaEnd;
    @FXML
    protected Line toraStart;
    @FXML
    protected Line todaStart;
    @FXML
    protected Line todaEnd;
    @FXML
    protected Line toraLength;
    @FXML
    protected Line ldaLength;
    @FXML
    protected Line asdaLength;
    @FXML
    protected Line todaLength;
    @FXML
    protected Line displacedThresholdL;
    @FXML
    protected Label ldaLabel;
    @FXML
    protected Label toraLabel;
    @FXML
    protected Label asdaLabel;
    @FXML
    protected Label todaLabel;
    @FXML
    protected Polygon tocsSlope;
    @FXML
    protected Rectangle stopwayR;
    @FXML
    protected Polygon alsSlope;
    @FXML
    protected Line displacedThresholdR;
    @FXML
    protected Button button;

    @FXML
    private void initialize() {
        alsSlope.setVisible(false);
        tocsSlope.setVisible(false);
        blastProtectionStart.setVisible(false);
        blastProtectionEnd.setVisible(false);
        blastProtectionLength.setVisible(false);
        blastProtectionLabel.setVisible(false);

    }

    @FXML
    private void handleExecuteButtonClick(ActionEvent event){
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway selectedLogRunway = MainController.getLogRunwaySelected();
        Obstacle selectedObstacle = MainController.getObstacleSelected();

        resetValues(selectedPhyRunway,selectedLogRunway);
        Calculator.performCalc(selectedObstacle,selectedLogRunway);
        setUpAlsTocs(selectedObstacle,selectedLogRunway);
        setNewLine("TORA",selectedLogRunway,selectedObstacle,toraStart,toraLength,toraEnd,toraLabel);
        setNewLine("LDA",selectedLogRunway,selectedObstacle,ldaStart,ldaLength,ldaEnd,ldaLabel);
        setNewLine("ASDA",selectedLogRunway,selectedObstacle,asdaStart,asdaLength,asdaEnd,asdaLabel);
        setNewLine("TODA",selectedLogRunway,selectedObstacle,todaStart,todaLength,todaEnd,todaLabel);
    }

    protected void setNewLine(String type, LogicalRunway logicalRunway,Obstacle obstacle,Line start,Line length,Line end,Label label){
        double originalValue = 0;
        double newValue = 0;
        switch (type) {
            case "TORA" -> {
                originalValue = logicalRunway.getTora();
                newValue = logicalRunway.getNewTora();
            }
            case "LDA" -> {
                originalValue = logicalRunway.getLda();
                newValue = logicalRunway.getNewLda();
            }
            case "ASDA" -> {
                originalValue = logicalRunway.getAsda();
                newValue = logicalRunway.getNewAsda();
            }
            case "TODA" -> {
                originalValue = logicalRunway.getToda();
                newValue = logicalRunway.getNewToda();
            }
        }
        double differenceInPx = getNumberOfPx(originalValue - newValue,logicalRunway);
        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;
        if (flightMethod.equals(talo)){
            start.setLayoutX(start.getLayoutX() + differenceInPx);
            length.setLayoutX(length.getLayoutX() + differenceInPx);
            length.setEndX(length.getEndX() - differenceInPx);
        } else if (flightMethod.equals(ttlt)) {
            end.setLayoutX(end.getLayoutX() - differenceInPx);
            length.setEndX(length.getEndX() - differenceInPx);
        }
        double labelLayout = getLabelLayout(start,length,label);
        label.setText(" " + type +" = " + newValue + " ");
        label.setLayoutX(labelLayout);
    }

    protected void setBlastProtection(String LandingOrTakeoff, LogicalRunway logicalRunway){
        double bPLengthPx = getNumberOfPx(PhysicalRunway.getBlastProtection(),logicalRunway);
        setLineVisibility(true,blastProtectionStart,blastProtectionLength,blastProtectionEnd,blastProtectionLabel);
        if (LandingOrTakeoff.equals("Takeoff")){
            //
            blastProtectionLabel.setText(String.valueOf(PhysicalRunway.getBlastProtection()));
            // Set X
            blastProtectionLength.setEndX(bPLengthPx);
            blastProtectionLength.setLayoutX(toraStart.getLayoutX()-bPLengthPx);
            blastProtectionEnd.setLayoutX(toraStart.getLayoutX());
            blastProtectionStart.setLayoutX(toraStart.getLayoutX()-bPLengthPx);
            blastProtectionLabel.setLayoutX(getLabelLayout(blastProtectionStart,blastProtectionLength,blastProtectionLabel));
            // Set Y
            setAllLayoutY(toraLength.getLayoutY(),blastProtectionStart,blastProtectionLength,blastProtectionEnd,blastProtectionLabel);
        }else {

        }
    }

    private void resetValues(PhysicalRunway physicalRunway, LogicalRunway logicalRunway){
        setUpPhyRunway(physicalRunway,logicalRunway);
        setUpStopwayAndClearway(physicalRunway,logicalRunway);
        setUpLogicalRunway(logicalRunway);
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

    private void setUpLogicalRunway(LogicalRunway logicalRunway){
        setUpTora(logicalRunway);
        setUpLda(logicalRunway);
        setUpAsda(logicalRunway);
        setUpToda(logicalRunway);
    }

    private void setUpPhyRunway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);

        //Set Up Threshold
        thresholdL.setLayoutX(phyRunway.getLayoutX());
        thresholdR.setLayoutX(phyRunway.getLayoutX()+phyRunway.getWidth());

        //Set Up Designator
        String lDesignator = lLogicalRunway.getDesignator();
        String rDesignator = rLogicalRunway.getDesignator();
        //Set Up DisplacedThreshold
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;

        //change in designator and displacedThreshold as logical runway changes
        if (selectedLogRunway.getDesignator().equals(lDesignator)){
            designatorL.setText(lDesignator);
            designatorR.setText(rDesignator);
            lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
            rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);
        }else {
            designatorL.setText(rDesignator);
            designatorR.setText(lDesignator);
            lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(rDisplacedThreshold,lLogicalRunway);
            rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(lDisplacedThreshold,rLogicalRunway);
        }

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
    }

    private void setUpStopwayAndClearway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        if (lLogicalRunway.getDesignator().equals(selectedLogRunway.getDesignator())){
            setStopClearway(rLogicalRunway,"Left");
            setStopClearway(lLogicalRunway,"Right");
        }else {
            setStopClearway(rLogicalRunway,"Right");
            setStopClearway(lLogicalRunway,"Left");
        }
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
        boolean usingAls = Calculator.getFlightMethod(obstacle,logicalRunway).equals(Calculator.talo);
        if (usingAls){
            tocsSlope.setVisible(false);
            alsSlope.setVisible(true);
            slope = alsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)+slopeWidthPx);
        }else {
            tocsSlope.setVisible(true);
            alsSlope.setVisible(false);
            slope = tocsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)-slopeWidthPx);
        }
    }

    private double getMeterPerPx(LogicalRunway logicalRunway){
        return logicalRunway.getTora()/ phyRunway.getWidth();
    }

    private double getNumberOfPx(double length,LogicalRunway logicalRunway){
        return length/getMeterPerPx(logicalRunway);
    }

    protected double getLabelLayout(Line start,Line length,Label label){
        return start.getLayoutX() + (length.getEndX()/2 - label.getWidth()/2);
    }

    protected void setAllLayoutY(double layoutY,Line start, Line length, Line end, Label label){
        start.setLayoutY(layoutY-start.getEndY()/2);
        end.setLayoutY(layoutY - end.getEndY()/2);
        length.setLayoutY(layoutY);
        label.setLayoutY(layoutY - label.getHeight()/2);
    }

    protected void setLineVisibility(boolean b, Line start, Line length, Line end, Label label){
        start.setVisible(b);
        length.setVisible(b);
        end.setVisible(b);
        label.setVisible(b);
    }

}
