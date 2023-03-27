package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.beans.value.ObservableValue;
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
    private Line rToraEnd;
    @FXML
    private Line rLdaStart;
    @FXML
    private Line rAsdaStart;
    @FXML
    private Line rLdaEnd;
    @FXML
    private Line rAsdaEnd;
    @FXML
    private Line rToraStart;
    @FXML
    private Line rTodaStart;
    @FXML
    private Line rTodaEnd;
    @FXML
    private Line rToraLength;
    @FXML
    private Line rLdaLength;
    @FXML
    private Line rAsdaLength;
    @FXML
    private Line rTodaLength;
    @FXML
    private Label rLdaLabel;
    @FXML
    private Label rToraLabel;
    @FXML
    private Label rAsdaLabel;
    @FXML
    private Label rTodaLabel;
    @FXML
     Line toraStripEndStart;
    @FXML
    private Line toraStripEndEnd;
    @FXML
    private Line toraStripEndLength;
    @FXML
    private Label toraStripEndLabel;
    @FXML
    private Line ldaBlastProtectionStart;
    @FXML
    private Line ldaBlastProtectionEnd;
    @FXML
    private Line ldBlastProtectionLength;
    @FXML
    private Label ldaBlastProtectionLabel;
    @FXML
    private Line displacedTStart;
    @FXML
    private Line displacedTEnd;
    @FXML
    private Line displacedTLength;
    @FXML
    private Label displacedTLabel;
    @FXML
    private Line blastProtectionStart;
    @FXML
    private Line blastProtectionLength;
    @FXML
    private Line blastProtectionEnd;
    @FXML
    private Label blastProtectionLabel;
    @FXML
    private Rectangle clearwayL;
    @FXML
    private Rectangle stopwayL;
    @FXML
    private Rectangle phyRunway;
    @FXML
    private Rectangle clearwayR;
    @FXML
    private Label designatorL;
    @FXML
    private Line thresholdL;
    @FXML
    private Label designatorR;
    @FXML
    private Line toraEnd;
    @FXML
    private Line thresholdR;
    @FXML
    private Line ldaStart;
    @FXML
    private Line asdaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Line asdaEnd;
    @FXML
    private Line toraStart;
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Line toraLength;
    @FXML
    private Line ldaLength;
    @FXML
    private Line asdaLength;
    @FXML
    private Line todaLength;
    @FXML
    private Line displacedThresholdL;
    @FXML
    private Label ldaLabel;
    @FXML
    private Label toraLabel;
    @FXML
    private Label asdaLabel;
    @FXML
    private Label todaLabel;
    @FXML
    private Polygon tocsSlope;
    @FXML
    private Rectangle stopwayR;
    @FXML
    private Polygon alsSlope;
    @FXML
    private Line displacedThresholdR;
    @FXML
    private Button button;

    @FXML
    private void initialize() {
        initialiseVisibility();

//        MainController.airportItem.addListener((observable, oldValue, newValue) -> {
//            leftDesignator.setText("___");
//            rightDesignator.setText("___");
//        });
        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
            PhysicalRunway runway = MainController.getPhysRunwaySelected();
            resetValues(runway);
            setUpLogicalRunway(runway);

        });
        MainController.obstacleProperty.addListener(((ObservableValue<? extends Obstacle> observable, Obstacle oldValue, Obstacle newValue) -> {
            if(oldValue != null){
                oldValue.setDistFThreshold(0);
                oldValue.setDistFCent(0);
            }
            newValue.setDistFThreshold(MainController.disFromThreshold.get());
            setUpAlsTocs(newValue,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
        }));
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> {
            setUpAlsTocs(MainController.getObstacleSelected(),MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
        });
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> handleExecuteButtonClick(new ActionEvent()));
    }

    @FXML
    protected void initialiseVisibility(){
        alsSlope.setVisible(false);
        tocsSlope.setVisible(false);
        blastProtectionStart.setVisible(false);
        blastProtectionEnd.setVisible(false);
        blastProtectionLength.setVisible(false);
        blastProtectionLabel.setVisible(false);
        ldaBlastProtectionStart.setVisible(false);
        ldaBlastProtectionEnd.setVisible(false);
        ldaBlastProtectionLabel.setVisible(false);
        ldBlastProtectionLength.setVisible(false);
        toraStripEndStart.setVisible(false);
        toraStripEndLength.setVisible(false);
        toraStripEndEnd.setVisible(false);
        toraStripEndLabel.setVisible(false);
    }

    @FXML
    protected void handleExecuteButtonClick(ActionEvent event){
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway lLogicalRunway = selectedPhyRunway.getLogicalRunways().get(0);

        Obstacle selectedObstacle = MainController.getObstacleSelected();
        Calculator.performCalc(selectedObstacle,selectedPhyRunway);
        resetValues(selectedPhyRunway);

        setUpAlsTocs(selectedObstacle,lLogicalRunway);
        setNewLine("TORA","Left",selectedPhyRunway,selectedObstacle,toraStart,toraLength,toraEnd,toraLabel);
        setNewLine("LDA","Left",selectedPhyRunway,selectedObstacle,ldaStart,ldaLength,ldaEnd,ldaLabel);
        setNewLine("ASDA","Left",selectedPhyRunway,selectedObstacle,asdaStart,asdaLength,asdaEnd,asdaLabel);
        setNewLine("TODA","Left",selectedPhyRunway,selectedObstacle,todaStart,todaLength,todaEnd,todaLabel);
        setNewLine("TORA","Right",selectedPhyRunway,selectedObstacle,rToraStart,rToraLength,rToraEnd,rToraLabel);
        setNewLine("LDA","Right",selectedPhyRunway,selectedObstacle,rLdaStart,rLdaLength,rLdaEnd,rLdaLabel);
        setNewLine("ASDA","Right",selectedPhyRunway,selectedObstacle,rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel);
        setNewLine("TODA","Right",selectedPhyRunway,selectedObstacle,rTodaStart,rTodaLength,rTodaEnd,rTodaLabel);

        //setOtherLines(selectedLogRunway,selectedObstacle,blastProtectionStart,blastProtectionLength,blastProtectionEnd,blastProtectionLabel,PhysicalRunway.getBlastProtection(),"TORA","blastProtection");
        //setOtherLines(selectedLogRunway,selectedObstacle,toraStripEndStart,toraStripEndLength,toraStripEndEnd,toraStripEndLabel,PhysicalRunway.getStripEnd(),"TORA","stripEnd");
    }

    //type is TORA, LDA ,TODA or ASDA
    protected void setNewLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Obstacle obstacle,Line start,Line length,Line end,Label label){
        LogicalRunway logicalRunway = null;
        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
        }

        double originalValue = oldAndNewValue(type,logicalRunway)[0];
        double newValue = oldAndNewValue(type,logicalRunway)[1];
        double difference = originalValue - newValue;
        double differenceInPx = getNumberOfPx(difference,logicalRunway);
        double labelLayout;

        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;

        if (LeftorRight.equals("Left")){
            if (flightMethod.equals(talo)){
                start.setLayoutX(start.getLayoutX() + differenceInPx);
                length.setLayoutX(length.getLayoutX() + differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(ttlt)) {
                end.setLayoutX(end.getLayoutX() - differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            }
            labelLayout = getLabelLayout(start,length,label);
        }else {
            //when flightMethod for left runway is ttlt, the flightmethod for right runway will be talo, vice versa
            if (flightMethod.equals(ttlt)){
                //for flightmethod talo
                start.setLayoutX(start.getLayoutX() - differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(talo)) {
                //for flightmethod ttlt
                end.setLayoutX(end.getLayoutX() + differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
                System.out.println(type + " " + LeftorRight + " = \n" + end.getLayoutX()+ "\n" +length.getStartX() + "\n" + length.getEndX() + "\n");
            }
            labelLayout = getLabelLayout(end,length,label);
        }

        label.setText(" " + type +" = " + newValue + " ");
        label.setLayoutX(labelLayout);
    }

    //protected void setBlastProtection(String LandingOrTakeoff, LogicalRunway logicalRunway){
    //    double bPLengthPx = getNumberOfPx(PhysicalRunway.getBlastProtection(),logicalRunway);
//
    //    setLineVisibility(true,blastProtectionStart,blastProtectionLength,blastProtectionEnd,blastProtectionLabel);
    //    if (LandingOrTakeoff.equals("Takeoff")){
    //        //
    //        blastProtectionLabel.setText(String.valueOf(PhysicalRunway.getBlastProtection()));
    //        // Set X
    //        blastProtectionLength.setEndX(bPLengthPx);
    //        blastProtectionLength.setLayoutX(toraStart.getLayoutX()-bPLengthPx);
    //        blastProtectionEnd.setLayoutX(toraStart.getLayoutX());
    //        blastProtectionStart.setLayoutX(toraStart.getLayoutX()-bPLengthPx);
    //        blastProtectionLabel.setLayoutX(getLabelLayout(blastProtectionStart,blastProtectionLength,blastProtectionLabel));
    //        // Set Y
    //        setAllLayoutY(toraLength.getLayoutY(),blastProtectionStart,blastProtectionLength,blastProtectionEnd,blastProtectionLabel);
    //    }else {
//
    //    }
    //}

    protected void setOtherLines(LogicalRunway logicalRunway, Obstacle obstacle, Line  start,Line length,Line end,Label label,double value,String type,String result){
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double resa = PhysicalRunway.getResa();
        double stripEnd = PhysicalRunway.getStripEnd();
        double alsTocs = obstacle.getAlsTocs();
        double lengthInPx = getNumberOfPx(value,logicalRunway);


        boolean flightMethodIsTALO = Calculator.getFlightMethod(obstacle,logicalRunway).equals(Calculator.talo);
        boolean resaGThanAlsTocs = resa > alsTocs;

        if (value != 0){
            //setLineVisibility(true,start,length,end,label);
            //set value
            label.setText(String.valueOf(value));
            //set the length of the line
            length.setEndX(lengthInPx);

            //new variables
            double lengthLayoutX=0;
            double startLayoutX=0;
            double endLayoutX=0;

            if (type.equals("TORA")){
                switch (result) {
                    case "blastProtection" -> {
                        if (flightMethodIsTALO){
                            setLineVisibility(true,start,length,end,label);
                            lengthLayoutX = toraStart.getLayoutX()-lengthInPx;
                            startLayoutX = toraStart.getLayoutX()-lengthInPx;
                            endLayoutX = toraStart.getLayoutX();
                        }
                    }
                    case "stripEnd" -> {
                        if (!flightMethodIsTALO){
                            System.out.println(toraLabel.getLayoutX());
                            setLineVisibility(true,start,length,end,label);
                            lengthLayoutX = toraEnd.getLayoutX();
                            startLayoutX = toraEnd.getLayoutX();
                            endLayoutX = toraEnd.getLayoutX()+lengthInPx;
                            label.setVisible(false);
                        }
                    }
                    case "resa" -> {
                        if (resaGThanAlsTocs){

                        }
                    }
                    case "slope" -> {
                        if (flightMethodIsTALO){

                        }else {

                        }
                    }
                }
            } else if (type.equals("LDA")) {
                switch (result) {
                    case "blastProtection" -> {


                    }
                    case "stripEnd" -> {

                    }
                    case "resa" -> {

                    }
                    case "slope" -> {
                        if (flightMethodIsTALO){

                        }else {

                        }
                    }
                }
            }

            length.setLayoutX(lengthLayoutX);
            start.setLayoutX (startLayoutX);
            end.setLayoutX   (endLayoutX);
            label.setLayoutX(getLabelLayout(start,length,label));

            //setAllLayoutY    (layoutY,start,length,end,label);

        }
    }

    protected void resetValues(PhysicalRunway physicalRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        setUpPhyRunway(physicalRunway,lLogicalRunway);
        setUpStopwayAndClearway(physicalRunway,lLogicalRunway);
        setUpLogicalRunway(physicalRunway);
    }
    protected void setUpLogicalRunway(PhysicalRunway physicalRunway){
        setUpLine("TORA","Left",physicalRunway,toraStart,toraLength,toraEnd,toraLabel);
        setUpLine("LDA","Left",physicalRunway,ldaStart,ldaLength,ldaEnd,ldaLabel);
        setUpLine("ASDA","Left",physicalRunway,asdaStart,asdaLength,asdaEnd,asdaLabel);
        setUpLine("TODA","Left",physicalRunway,todaStart,todaLength,todaEnd,todaLabel);
        setUpLine("TORA","Right",physicalRunway,rToraStart,rToraLength,rToraEnd,rToraLabel);
        setUpLine("LDA","Right",physicalRunway,rLdaStart,rLdaLength,rLdaEnd,rLdaLabel);
        setUpLine("ASDA","Right",physicalRunway,rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel);
        setUpLine("TODA","Right",physicalRunway,rTodaStart,rTodaLength,rTodaEnd,rTodaLabel);
    }

    //protected void setUpTora(PhysicalRunway physicalRunway){
    //    LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
    //    LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
//
    //    double originalStartX = thresholdL.getLayoutX();
    //    double originalEndX = thresholdR.getLayoutX();
    //    double tora = logicalRunway.getTora();
    //    //Reset the starting line
    //    toraStart.setLayoutX(originalStartX);
    //    toraEnd.setLayoutX(originalEndX);
    //    toraLength.setLayoutX(originalStartX);
    //    toraLength.setEndX(originalEndX - originalStartX);
    //    toraLabel.setText(" TORA = " + tora);
    //}
//
    //protected void setUpLda(PhysicalRunway physicalRunway){
    //    double originalStartX;
    //    double originalEndX = thresholdR.getLayoutX();
    //    double lda = logicalRunway.getLda();
//
    //    //Make displacedThreshold the starting point if it exists
    //    if (logicalRunway.getDisplacedThreshold() != 0){
    //        originalStartX = displacedThresholdL.getLayoutX();
    //    }else {
    //        originalStartX = thresholdL.getLayoutX();
    //    }
    //    //Reset the starting line
    //    ldaStart.setLayoutX(originalStartX);
    //    ldaEnd.setLayoutX(originalEndX);
    //    ldaLength.setLayoutX(originalStartX);
    //    ldaLength.setEndX(originalEndX - originalStartX);
    //    ldaLabel.setText(" LDA = " + lda);
    //}

    protected void setUpLine(String type,String LeftorRight, PhysicalRunway physicalRunway, Line start, Line length, Line end, Label label){
        LogicalRunway logicalRunway = null;
        double originalStartX;
        double originalEndX;
        double leftActualThresholdX = thresholdL.getLayoutX();
        double rightActualThresholdX = thresholdR.getLayoutX();
        double displacedThresholdLayoutX;
        Rectangle stopway;
        Rectangle clearway;

        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
            displacedThresholdLayoutX = displacedThresholdL.getLayoutX();
            stopway = stopwayR;
            clearway = clearwayR;
            originalStartX = leftActualThresholdX;
            originalEndX = rightActualThresholdX;
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
            displacedThresholdLayoutX = displacedThresholdR.getLayoutX();
            stopway = stopwayL;
            clearway = clearwayL;
            originalStartX = rightActualThresholdX;
            originalEndX = leftActualThresholdX;
        }

        //condition for defining start
        if (type.equals("LDA")){
            originalStartX = displacedThresholdLayoutX;
        }

        //condition for defining end
        if (LeftorRight.equals("Left")){
            if (type.equals("ASDA")){
                originalEndX = stopway.getLayoutX() + stopway.getWidth();
            }else if (type.equals("TODA")){
                originalEndX = clearway.getLayoutX() + clearway.getWidth();
            }
        }else {
            if (type.equals("ASDA")){
                originalEndX = stopway.getLayoutX();
            }else if (type.equals("TODA")){
                originalEndX = clearway.getLayoutX() ;
            }
        }

        double originalValue = oldAndNewValue(type,logicalRunway)[0];

        start.setLayoutX(originalStartX);
        end.setLayoutX(originalEndX);
        label.setText(" " + type + " = " + originalValue + " " );
        if (LeftorRight.equals("Left")){
            length.setLayoutX(originalStartX);
        }else {
            length.setLayoutX(originalEndX);
        }
        double difference = Math.abs(originalEndX-originalStartX);
        length.setEndX(difference);
        System.out.println(type + " " + LeftorRight + " = \n" + end.getLayoutX()+ "\n" +length.getStartX() + "\n" + length.getEndX() + "\n");
    }

    protected double[] oldAndNewValue(String type, LogicalRunway logicalRunway){
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
        return new double[]{originalValue,newValue};
    }




    //protected void setUpAsda(PhysicalRunway physicalRunway){
    //    //Set Up Variables
    //    double originalStartX = thresholdL.getLayoutX();
    //    double originalEndX;
    //    double asda = logicalRunway.getAsda();
    //    if (logicalRunway.getStopway() != 0){
    //        originalEndX = stopwayR.getLayoutX()+stopwayR.getWidth();
    //    }else {
    //        originalEndX = thresholdR.getLayoutX();
    //    }
//
    //    asdaStart.setLayoutX(originalStartX);
    //    asdaEnd.setLayoutX(originalEndX);
    //    asdaLength.setLayoutX(originalStartX);
    //    asdaLength.setEndX(originalEndX - originalStartX);
    //    asdaLabel.setText(" ASDA = " + asda);
    //}
//
    //protected void setUpToda(PhysicalRunway physicalRunway){
    //    //Set Up Variables
    //    double originalStartX = thresholdL.getLayoutX();
    //    double originalEndX;
    //    double toda = logicalRunway.getToda();
    //    if (logicalRunway.getClearway() != 0){
    //        originalEndX = clearwayR.getLayoutX()+clearwayR.getWidth();
    //    }else {
    //        originalEndX = thresholdR.getLayoutX();
    //    }
//
    //    todaStart.setLayoutX(originalStartX);
    //    todaEnd.setLayoutX(originalEndX);
    //    todaLength.setLayoutX(originalStartX);
    //    todaLength.setEndX(originalEndX - originalStartX);
    //    todaLabel.setText(" TODA = " + toda);
    //}

    protected void setUpPhyRunway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
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

    protected void setUpStopwayAndClearway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        //If the left Logical runway is the selected Logical runway
        if (lLogicalRunway.getDesignator().equals(selectedLogRunway.getDesignator())){
            //The stop/clearway for left runway will be on the right side, vice versa
            setStopClearway(lLogicalRunway,"Right");
            setStopClearway(rLogicalRunway,"Left");
        }else {
            setStopClearway(lLogicalRunway,"Left");
            setStopClearway(rLogicalRunway,"Right");
        }
    }

    protected void setStopClearway(LogicalRunway logicalRunway,String leftOrRightWay){
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

    protected void setStopClearwayValue(Rectangle way,double length, double widthPx, double oriWayX){
        if (length != 0 ){
            way.setWidth(widthPx);
            way.setLayoutX(oriWayX);
        }else {
            way.setWidth(0);
            way.setLayoutX(oriWayX);
        }
    }

    protected void setUpAlsTocs(Obstacle obstacle, LogicalRunway logicalRunway){
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

    protected double getMeterPerPx(LogicalRunway logicalRunway){
        return logicalRunway.getTora()/ phyRunway.getWidth();
    }

    protected double getNumberOfPx(double length,LogicalRunway logicalRunway){
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
