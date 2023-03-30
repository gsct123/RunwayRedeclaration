package Controller;

import Model.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SideViewController {

    //tora
    @FXML
    private Line toraStart;
    @FXML
    private Line toraEnd;
    @FXML
    private Line toraLength;
    @FXML
    private Label toraLabel;
    @FXML
    private Polygon toraArrow;
    @FXML
    private Line rToraStart;
    @FXML
    private Line rToraEnd;
    @FXML
    private Line rToraLength;
    @FXML
    private Label rToraLabel;
    @FXML
    private Polygon rToraArrow;

    //lda
    @FXML
    private Line ldaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Line ldaLength;
    @FXML
    private Label ldaLabel;
    @FXML
    private Polygon ldaArrow;
    @FXML
    private Line rLdaStart;
    @FXML
    private Line rLdaEnd;
    @FXML
    private Line rLdaLength;
    @FXML
    private Label rLdaLabel;
    @FXML
    private Polygon rLdaArrow;

    //asda
    @FXML
    private Line asdaStart;
    @FXML
    private Line asdaEnd;
    @FXML
    private Line asdaLength;
    @FXML
    private Label asdaLabel;
    @FXML
    private Polygon asdaArrow;
    @FXML
    private Line rAsdaStart;
    @FXML
    private Line rAsdaEnd;
    @FXML
    private Line rAsdaLength;
    @FXML
    private Label rAsdaLabel;
    @FXML
    private Polygon rAsdaArrow;

    //toda
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Line todaLength;
    @FXML
    private Label todaLabel;
    @FXML
    private Polygon todaArrow;
    @FXML
    private Line rTodaStart;
    @FXML
    private Line rTodaEnd;
    @FXML
    private Line rTodaLength;
    @FXML
    private Label rTodaLabel;
    @FXML
    private Polygon rTodaArrow;

    //others
    @FXML
    private Line toraOtherLength;
    @FXML
    private Line toraOtherLength1;
    @FXML
    private Line ldaOtherLength;
    @FXML
    private Line ldaOtherLength1;
    @FXML
    private Label toraOtherLabel;
    @FXML
    private Label toraOtherLabel1;
    @FXML
    private Label ldaOtherLabel;
    @FXML
    private Label ldaOtherLabel1;
    @FXML
    private Line toraOtherStart;
    @FXML
    private Line ldaOtherStart;
    @FXML
    private Line ldaOtherEnd;
    @FXML
    private Line toraOtherEnd;
    @FXML
    private Polygon toraOtherArrow;
    @FXML
    private Polygon toraOtherArrow1;
    @FXML
    private Polygon ldaOtherArrow;
    @FXML
    private Polygon ldaOtherArrow1;

    //clearway and stopway
    @FXML
    private Rectangle clearwayL;
    @FXML
    private Rectangle clearwayR;
    @FXML
    private Rectangle stopwayL;
    @FXML
    private Rectangle stopwayR;

    //physical runway
    @FXML
    private Rectangle phyRunway;
    @FXML
    private Label designatorL;
    @FXML
    private Label designatorR;
    @FXML
    private Line thresholdL;
    @FXML
    private Line thresholdR;
    @FXML
    private Line displacedThresholdL;
    @FXML
    private Line displacedThresholdR;

    //obstacles
    @FXML
    private Polygon tocsSlope;
    @FXML
    private Polygon alsSlope;


    @FXML
    private void initialize() {
        initialiseVisibility();

        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
            PhysicalRunway runway = MainController.getPhysRunwaySelected();
            resetValues(runway);
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
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel(new ActionEvent()));
    }

    @FXML
    private void initialiseVisibility(){
        alsSlope.setVisible(false);
        tocsSlope.setVisible(false);
        toraOtherLength.setVisible(false);
        toraOtherLength1.setVisible(false);
        ldaOtherLength.setVisible(false);
        ldaOtherLength1.setVisible(false);
        toraOtherLabel.setVisible(false);
        toraOtherLabel1.setVisible(false);
        ldaOtherLabel.setVisible(false);
        ldaOtherLabel1.setVisible(false);
        toraOtherStart.setVisible(false);
        ldaOtherStart.setVisible(false);
        ldaOtherEnd.setVisible(false);
        toraOtherEnd.setVisible(false);
        toraOtherArrow.setVisible(false);
        toraOtherArrow1.setVisible(false);
        ldaOtherArrow.setVisible(false);
        ldaOtherArrow1.setVisible(false);
    }

    @FXML
    private void updateLabel(ActionEvent event){
        //Selected variables in MainController
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway lLogicalRunway = selectedPhyRunway.getLogicalRunways().get(0);
        Obstacle selectedObstacle = MainController.getObstacleSelected();

        if(Calculator.needRedeclare(selectedObstacle, lLogicalRunway)){
            Calculator.performCalc(selectedObstacle,selectedPhyRunway);
            resetValues(selectedPhyRunway);
            setUpAlsTocs(selectedObstacle,lLogicalRunway);
            //Set Up Arrows for the logical runway
            Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
            Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
            Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
            Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
            Arrow RToraArrow = new Arrow(rToraStart,rToraLength,rToraEnd,rToraLabel,rToraArrow);
            Arrow RLdaArrow = new Arrow(rLdaStart,rLdaLength,rLdaEnd,rLdaLabel,rLdaArrow);
            Arrow RAsdaArrow = new Arrow(rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel,rAsdaArrow);
            Arrow RTodaArrow = new Arrow(rTodaStart,rTodaLength,rTodaEnd,rTodaLabel,rTodaArrow);
            //Set Up the new Lines
            setNewLine("TORA","Left",selectedPhyRunway,selectedObstacle,ToraArrow);
            setNewLine("LDA","Left",selectedPhyRunway,selectedObstacle,LdaArrow);
            setNewLine("ASDA","Left",selectedPhyRunway,selectedObstacle,AsdaArrow);
            setNewLine("TODA","Left",selectedPhyRunway,selectedObstacle,TodaArrow);
            setNewLine("TORA","Right",selectedPhyRunway,selectedObstacle,RToraArrow);
            setNewLine("LDA","Right",selectedPhyRunway,selectedObstacle,RLdaArrow);
            setNewLine("ASDA","Right",selectedPhyRunway,selectedObstacle,RAsdaArrow);
            setNewLine("TODA","Right",selectedPhyRunway,selectedObstacle,RTodaArrow);
            //Set up OtherLines (Resa,BlastProtection...)
            boolean needRedeclare = Calculator.needRedeclare(selectedObstacle, lLogicalRunway);
            boolean isTalo = Calculator.getFlightMethod(selectedObstacle,lLogicalRunway).equals(Calculator.talo);
            setToraOtherLine(needRedeclare,isTalo, false, toraOtherLabel, toraOtherLength, toraOtherStart, toraOtherArrow);
            setLdaOtherLine(needRedeclare,isTalo, false, ldaOtherLabel, ldaOtherLength, ldaOtherStart, ldaOtherArrow);
            setToraOtherLine(needRedeclare,isTalo, true, toraOtherLabel1, toraOtherLength1, toraOtherEnd, toraOtherArrow1);
            setLdaOtherLine(needRedeclare,isTalo, true, ldaOtherLabel1, ldaOtherLength1, ldaOtherEnd, ldaOtherArrow1);
        } else{
            resetValues(selectedPhyRunway);
        }
    }

    //type is TORA, LDA ,TODA or ASDA
    private void setNewLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Obstacle obstacle,Arrow arrow){
        LogicalRunway logicalRunway = null;
        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
        }
        //variables for calculation later
        double originalValue = oldAndNewValue(type,logicalRunway)[0];
        double newValue = oldAndNewValue(type,logicalRunway)[1];
        double difference = originalValue - newValue;
        double differenceInPx = getNumberOfPx(difference,logicalRunway);
        double labelLayout;
        //Arrow variables
        Line start = arrow.getStart();
        Line length = arrow.getLength();
        Line end = arrow.getEnd();
        Label label = arrow.getLabel();
        Polygon arrowHead = arrow.getArrowHead();
        //variables for flightMethod
        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;
        //Start setting the lines based on it is either left or right side of the runway
        if (LeftorRight.equals("Left")){
            if (flightMethod.equals(talo)){
                start.setLayoutX(start.getLayoutX() + differenceInPx);
                length.setLayoutX(length.getLayoutX() + differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(ttlt)) {
                end.setLayoutX(end.getLayoutX() - differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            }
        }else {
            //when flightMethod for left runway is ttlt, the flightmethod for right runway will be talo, vice versa
            if (flightMethod.equals(ttlt)){
                //when flightMethod for right side is TALO
                start.setLayoutX(start.getLayoutX() - differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(talo)) {
                //when flightMethod for right side is TTLT
                end.setLayoutX(end.getLayoutX() + differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            }
        }
        label.setText(" " + type +" = " + newValue + "m ");
        labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
        label.setLayoutX(labelLayout);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    private void setLdaOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);
        arrow.setVisible(bool);

        if(bool){
            Obstacle obstacle = MainController.getObstacleSelected();

            double stripEnd = PhysicalRunway.getStripEnd();
            double resa = PhysicalRunway.getResa();
            double alsTocs = obstacle.getAlsTocs();
            double blastProtection = PhysicalRunway.getBlastProtection();
            double change;
            Line reference;
            //some tweak here
            if(isTALO){
                if(lower){
                    reference = rLdaEnd;
                } else{
                    reference = ldaStart;
                }
            } else {
                if(lower){
                    reference = rLdaStart;
                } else{
                    reference = ldaEnd;
                }
            }
            if(!isTALO && !lower || isTALO && lower){
                label.setText("  RESA + Strip End" +
                        "\n"+"= "+resa+"m + "+stripEnd+"m");
                change = resa+stripEnd;
            } else{
                //ttlt
                if(alsTocs < resa){
                    if(resa + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  RESA + Strip End" +
                                "\n"+"= "+resa+"m + "+stripEnd+"m");
                        change = resa + stripEnd;
                    }
                } else{
                    if(alsTocs + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  ALS + Strip End" +
                                "\n"+"= "+alsTocs+"m + "+stripEnd+"m");
                        change = alsTocs + stripEnd;
                    }
                }
            }
            //some tweak here
            double changePx = getNumberOfPx(change,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(changePx));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(changePx));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }
            boolean isTalo = Calculator.getFlightMethod(obstacle,MainController.getPhysRunwaySelected().getLogicalRunways().get(0)).equals(Calculator.talo);
            if(!lower){
                if(isTalo){
                    arrow.setLayoutX(reference.getLayoutX());
                }else {
                    arrow.setLayoutX(startEnd.getLayoutX());
                }
            } else{
                if(isTalo){
                    arrow.setLayoutX(startEnd.getLayoutX());
                }else {
                    arrow.setLayoutX(reference.getLayoutX());
                }
            }
        }
    }

    private void setToraOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);
        arrow.setVisible(bool);

        if(bool){
            Obstacle obstacle = MainController.getObstacleSelected();

            double stripEnd = PhysicalRunway.getStripEnd();
            double resa = PhysicalRunway.getResa();
            double alsTocs = obstacle.getAlsTocs();
            double blastProtection = PhysicalRunway.getBlastProtection();
            double change;
            Line reference;
            if(isTALO){
                if(lower){
                    reference = rToraEnd;
                } else{
                    reference = toraStart;
                }
            } else{
                if(lower) {
                    reference = rTodaStart;
                } else{
                    reference = toraEnd;
                }
            }

            if(isTALO && !lower || (!isTALO && lower)){
                label.setText("Blast protection" +
                        "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                change = PhysicalRunway.getBlastProtection();
            } else{
                //ttlt
                if(alsTocs < resa){
                    if(resa + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("   RESA + Strip End" +
                                "\n"+"= "+resa+"m + "+stripEnd+"m");
                        change = resa + stripEnd;
                    }
                } else{
                    if(alsTocs + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  TOCS + Strip End" +
                                "\n"+"= "+alsTocs+"m + "+stripEnd+"m");
                        change = alsTocs + stripEnd;
                    }
                }
            }
            double changePx = getNumberOfPx(change,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(changePx));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(changePx));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }
            boolean isTalo = Calculator.getFlightMethod(obstacle,MainController.getPhysRunwaySelected().getLogicalRunways().get(0)).equals(Calculator.talo);
            if(!lower){
                if(isTalo){
                    arrow.setLayoutX(reference.getLayoutX());
                }else {
                    arrow.setLayoutX(startEnd.getLayoutX());
                }
            } else{
                if(isTalo){
                    arrow.setLayoutX(startEnd.getLayoutX());
                }else {
                    arrow.setLayoutX(reference.getLayoutX());
                }
            }
        }
    }
    private void resetValues(PhysicalRunway physicalRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        setUpPhyRunway(physicalRunway,lLogicalRunway);
        setUpStopwayAndClearway(physicalRunway,lLogicalRunway);
        setUpLogicalRunway(physicalRunway);
        initialiseVisibility();
    }
    private void setUpLogicalRunway(PhysicalRunway physicalRunway){
        //left
        Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        setUpLine("TORA","Left",physicalRunway,ToraArrow);
        setUpLine("LDA","Left",physicalRunway,LdaArrow);
        setUpLine("ASDA","Left",physicalRunway,AsdaArrow);
        setUpLine("TODA","Left",physicalRunway,TodaArrow);
        //right
        Arrow RToraArrow = new Arrow(rToraStart,rToraLength,rToraEnd,rToraLabel,rToraArrow);
        Arrow RLdaArrow = new Arrow(rLdaStart,rLdaLength,rLdaEnd,rLdaLabel,rLdaArrow);
        Arrow RAsdaArrow = new Arrow(rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel,rAsdaArrow);
        Arrow RTodaArrow = new Arrow(rTodaStart,rTodaLength,rTodaEnd,rTodaLabel,rTodaArrow);
        setUpLine("TORA","Right",physicalRunway,RToraArrow);
        setUpLine("LDA","Right",physicalRunway,RLdaArrow);
        setUpLine("ASDA","Right",physicalRunway,RAsdaArrow);
        setUpLine("TODA","Right",physicalRunway,RTodaArrow);
    }

    private void setUpLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Arrow arrow){
        //initiate variables
        double originalStartX;
        double originalEndX;
        double leftActualThresholdX = thresholdL.getLayoutX();
        double rightActualThresholdX = thresholdR.getLayoutX();
        double displacedThresholdLayoutX;
        Rectangle stopway;
        Rectangle clearway;
        LogicalRunway logicalRunway = null;

        //Arrow variables
        Line start = arrow.getStart();
        Line length = arrow.getLength();
        Line end = arrow.getEnd();
        Label label = arrow.getLabel();
        Polygon arrowHead = arrow.getArrowHead();

        //default condition for all arrow
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

        // extra conditions for defining start
        if (type.equals("LDA")){
            originalStartX = displacedThresholdLayoutX;
        }

        //extra conditions for defining end
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

        //get which value we are getting (TORA/LDA/ASDA/TODA)
        double originalValue = oldAndNewValue(type,logicalRunway)[0];

        start.setLayoutX(originalStartX);
        end.setLayoutX(originalEndX);
        label.setText(" " + type +" = " + originalValue + "m ");
        if (LeftorRight.equals("Left")){
            length.setLayoutX(originalStartX);
        }else {
            length.setLayoutX(originalEndX);
        }
        double difference = Math.abs(originalEndX-originalStartX);
        length.setEndX(difference);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    private double[] oldAndNewValue(String type, LogicalRunway logicalRunway){
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
         designatorL.setText(lDesignator);
         designatorR.setText(rDesignator);
         lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
         rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
    }

    private void setUpStopwayAndClearway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
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
        }else {
            way.setWidth(0);
        }
        way.setLayoutX(oriWayX);
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

    private double getLabelLayout(Line start,Line length,Label label){
        return start.getLayoutX() + (length.getEndX()/2 - label.getWidth()/2);
    }

}
