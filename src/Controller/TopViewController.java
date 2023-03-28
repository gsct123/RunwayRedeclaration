package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
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
    private Rectangle background;
    @FXML
    private Rectangle obstacleBlock;
    @FXML
    private Line centreLine;
    @FXML
    private Rectangle minCGArea;

    //reference
    @FXML
    private Line thresholdL;
    @FXML
    private Line thresholdR;

    //tora label
    @FXML
    private Line toraStart;
    @FXML
    private Line toraEnd;
    @FXML
    private Label toraLabel;
    @FXML
    private Line toraLength;
    @FXML
    private Line toraStart1;
    @FXML
    private Line toraEnd1;
    @FXML
    private Line toraLength1;
    @FXML
    private Label toraLabel1;
    //toda
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Label todaLabel;
    @FXML
    private Line todaLength;
    @FXML
    private Line todaStart1;
    @FXML
    private Line todaEnd1;
    @FXML
    private Label todaLabel1;
    @FXML
    private Line todaLength1;
    //lda
    @FXML
    private Line ldaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Label ldaLabel;
    @FXML
    private Line ldaLength;
    @FXML
    private Line ldaStart1;
    @FXML
    private Line ldaEnd1;
    @FXML
    private Label ldaLabel1;
    @FXML
    private Line ldaLength1;

    //asda
    @FXML
    private Line asdaStart;
    @FXML
    private Line asdaEnd;
    @FXML
    private Label asdaLabel;
    @FXML
    private Line asdaLength;
    @FXML
    private Line asdaStart1;
    @FXML
    private Line asdaEnd1;
    @FXML
    private Label asdaLabel1;
    @FXML
    private Line asdaLength1;

    //other labels
    @FXML
    private Line toraOtherLineLength;
    @FXML
    private Line toraOtherLineStart;
    @FXML
    private Label toraOtherLineLabel;
    @FXML
    private Line toraOtherLineLength1;
    @FXML
    private Line toraOtherLineEnd;
    @FXML
    private Label toraOtherLineLabel1;
    @FXML
    private Line ldaOtherLineLength;
    @FXML
    private Line ldaOtherLineStart;
    @FXML
    private Label ldaOtherLineLabel;
    @FXML
    private Line ldaOtherLineLength1;
    @FXML
    private Line ldaOtherLineEnd;
    @FXML
    private Label ldaOtherLineLabel1;

    @FXML
    private Rectangle clearwayL;
    @FXML
    private Rectangle clearwayR;
    @FXML
    private Rectangle stopwayL;
    @FXML
    private Rectangle stopwayR;
    @FXML
    private Line displacedThresholdL;
    @FXML
    private Line displacedThresholdR;

    //arrows
    @FXML
    private Polygon todaArrow;
    @FXML
    private Polygon asdaArrow;
    @FXML
    private Polygon ldaArrow;
    @FXML
    private Polygon toraArrow;
    @FXML
    private Polygon todaArrow1;
    @FXML
    private Polygon ldaArrow1;
    @FXML
    private Polygon asdaArrow1;
    @FXML
    private Polygon toraArrow1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MainController.airportItem.addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText(newValue.getLogicalRunways().get(0).getDesignator());
            rightDesignator.setText(newValue.getLogicalRunways().get(1).getDesignator());
            showOriginalParameters();
            if(MainController.getObstacleSelected() != null){
                relocateObstacle();
            }
        });
        MainController.obstacleProperty.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null){
                oldValue.setDistFThreshold(0);
                oldValue.setDistFCent(0);
            }
            newValue.setDistFThreshold(MainController.disFromThreshold.get());
            relocateObstacle();
        });
        MainController.dirFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> {
            relocateObstacle();
        });
        MainController.disFromCentre.addListener((observable, oldValue, newValue) -> {
            relocateObstacle();
        });
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel());
    }

    public void updateLabel(){
        relocateObstacle();
//        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
//        LogicalRunway llogRunway = selectedPhyRunway.getLogicalRunways().get(0);
//        LogicalRunway rlogRunway = selectedPhyRunway.getLogicalRunways().get(1);
//        Obstacle obstacle = MainController.obstacleProperty.get();
//        showOriginalParameters();
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway llogRunway = selectedPhyRunway.getLogicalRunways().get(0);

        Obstacle obstacle = MainController.getObstacleSelected();
        Calculator.performCalc(obstacle,selectedPhyRunway);
        resetValues(selectedPhyRunway);

        setNewLine("TORA","Left",selectedPhyRunway,obstacle,toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        setNewLine("LDA","Left",selectedPhyRunway,obstacle,ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        setNewLine("ASDA","Left",selectedPhyRunway,obstacle,asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        setNewLine("TODA","Left",selectedPhyRunway,obstacle,todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        setNewLine("TORA","Right",selectedPhyRunway,obstacle,toraEnd1,toraLength1,toraStart1,toraLabel1,toraArrow1);
        setNewLine("LDA","Right",selectedPhyRunway,obstacle,ldaEnd1,ldaLength1,ldaStart1,ldaLabel1,ldaArrow1);
        setNewLine("ASDA","Right",selectedPhyRunway,obstacle,asdaEnd1,asdaLength1,asdaStart1,asdaLabel1,asdaArrow1);
        setNewLine("TODA","Right",selectedPhyRunway,obstacle,todaEnd1,todaLength1,todaStart1,todaLabel1,todaArrow1);

        setToraOtherLine(Calculator.needRedeclare(obstacle, llogRunway), Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.talo), false, toraOtherLineLabel, toraOtherLineLength, toraOtherLineStart);
        setLdaOtherLine(Calculator.needRedeclare(obstacle, llogRunway), Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.talo), false, ldaOtherLineLabel, ldaOtherLineLength, ldaOtherLineStart);

        setToraOtherLine(Calculator.needRedeclare(obstacle, llogRunway), Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.talo), true, toraOtherLineLabel1, toraOtherLineLength1, toraOtherLineEnd);
        setLdaOtherLine(Calculator.needRedeclare(obstacle, llogRunway), Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.talo), true, ldaOtherLineLabel1, ldaOtherLineLength1, ldaOtherLineEnd);

    }

    protected void resetValues(PhysicalRunway physicalRunway){
        toraOtherLineEnd.setVisible(false);
        toraOtherLineStart.setVisible(false);
        toraOtherLineLabel.setVisible(false);
        toraOtherLineLabel1.setVisible(false);
        toraOtherLineLength1.setVisible(false);
        toraOtherLineLength.setVisible(false);

        ldaOtherLineEnd.setVisible(false);
        ldaOtherLineStart.setVisible(false);
        ldaOtherLineLabel.setVisible(false);
        ldaOtherLineLabel1.setVisible(false);
        ldaOtherLineLength.setVisible(false);
        ldaOtherLineLength1.setVisible(false);
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        setUpPhyRunway(physicalRunway,lLogicalRunway);
        setUpStopwayAndClearway(physicalRunway,lLogicalRunway);
        setUpLogicalRunway(physicalRunway);
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

    protected void setUpPhyRunway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);

        //Set Up Threshold
        thresholdL.setLayoutX(runway.getLayoutX());
        thresholdR.setLayoutX(runway.getLayoutX()+runway.getWidth());

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
            leftDesignator.setText(lDesignator);
            rightDesignator.setText(rDesignator);
            lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
            rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);
        }else {
            leftDesignator.setText(rDesignator);
            rightDesignator.setText(lDesignator);
            lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(rDisplacedThreshold,lLogicalRunway);
            rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(lDisplacedThreshold,rLogicalRunway);
        }

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
    }

    protected void setNewLine(String type, String LeftorRight, PhysicalRunway physicalRunway, Obstacle obstacle, Line start, Line length, Line end, Label label, Polygon arrowHead){
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
        }

        label.setText(" " + type +" = " + newValue + " ");
        labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
        label.setLayoutX(labelLayout);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    protected double getLabelLayout(Line start,Line length,Label label){
        return start.getLayoutX() + (length.getEndX()/2 - label.getWidth()/2);
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

    protected double getNumberOfPx(double length,LogicalRunway logicalRunway){
        return length/getMeterPerPx(logicalRunway);
    }

    protected double getMeterPerPx(LogicalRunway logicalRunway){
        return logicalRunway.getTora()/ runway.getWidth();
    }

    public void setLdaOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);

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
                    reference = ldaStart1;
                } else{
                    reference = ldaStart;
                }
            } else {
                if(lower){
                    reference = ldaEnd1;
                } else{
                    reference = ldaEnd;
                }
            }

            if(!isTALO && !lower || isTALO && lower){
                label.setText("  RESA + Strip End" +
                        "\n"+"= "+resa+"m + "+stripEnd+"m");
                change = PhysicalRunway.getBlastProtection();
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

            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(change*meterPerPx()));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(change*meterPerPx()));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }
        }
    }

    public void setToraOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);

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
                    reference = toraStart1;
                } else{
                    reference = toraStart;
                }
            } else{
                if(lower) {
                    reference = toraEnd1;
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

            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(change*meterPerPx()));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(change*meterPerPx()));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }
        }
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void relocateObstacle(){
        obstacleBlock.setVisible(true);
        Obstacle obstacle = MainController.getObstacleSelected();
        LogicalRunway logRunway;
        double runwayStartX = runway.getLayoutX();
        double runwayLength = runway.getWidth();
        double centre = centreLine.getLayoutY();
        double disFromThreshold = obstacle.getDistFThreshold();
        double tora;
        double stripEnd;
        double obsBlockWidth = obstacleBlock.getHeight();

        logRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(0);
        tora = logRunway.getTora();
        stripEnd = PhysicalRunway.getStripEnd();
        double displacedFromCentre = obstacle.getDirFromCentre().equals("L")? (-obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)-obsBlockWidth/2: (obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)-obsBlockWidth/2;
        if(Calculator.needRedeclare(obstacle, logRunway)){
            if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                obstacleBlock.relocate(runwayStartX+((disFromThreshold+logRunway.getDisplacedThreshold())*(runwayLength-logRunway.getClearway())/tora)-obsBlockWidth, centre+displacedFromCentre);
            } else{
                obstacleBlock.relocate(runwayStartX+((disFromThreshold+logRunway.getDisplacedThreshold())*(runwayLength-logRunway.getClearway())/tora), centre+(displacedFromCentre));
            }
        }else{
            obstacleBlock.setVisible(false);
        }
    }

    public void showOriginalParameters(){
        PhysicalRunway physRunway = MainController.getPhysRunwaySelected();
        LogicalRunway lLogicalRunway = physRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physRunway.getLogicalRunways().get(1);
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;
        lDisplacedThresholdX = thresholdL.getLayoutX() + lDisplacedThreshold*meterPerPx();
        rDisplacedThresholdX = thresholdR.getLayoutX() - rDisplacedThreshold*meterPerPx();

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);

        setStopClearway(rLogicalRunway,"Left");
        setStopClearway(lLogicalRunway,"Right");

        setUpLogicalRunway(physRunway);
    }

    protected void setUpLogicalRunway(PhysicalRunway physicalRunway){
        setUpLine("TORA","Left",physicalRunway,toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        setUpLine("LDA","Left",physicalRunway,ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        setUpLine("ASDA","Left",physicalRunway,asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        setUpLine("TODA","Left",physicalRunway,todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        setUpLine("TORA","Right",physicalRunway,toraEnd1,toraLength1,toraStart1,toraLabel1,toraArrow1);
        setUpLine("LDA","Right",physicalRunway,ldaEnd1,ldaLength1,ldaStart1,ldaLabel1,ldaArrow1);
        setUpLine("ASDA","Right",physicalRunway,asdaEnd1,asdaLength1,asdaStart1,asdaLabel1,asdaArrow1);
        setUpLine("TODA","Right",physicalRunway,todaEnd1,todaLength1,todaStart1,todaLabel1,todaArrow1);
    }

    protected void setUpLine(String type,String LeftorRight, PhysicalRunway physicalRunway, Line start, Line length, Line end, Label label, Polygon arrowHead){
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
        arrowHead.setLayoutX(end.getLayoutX());
    }

    private double meterPerPx(){
        return runway.getWidth()/MainController.getPhysRunwaySelected().getLogicalRunways().get(0).getTora();
    }

    private void setStopClearway(LogicalRunway logicalRunway,String leftOrRightWay){
        //Set Up  Variable
        double stopwayLength = logicalRunway.getStopway();
        double stopwayWidthPx = stopwayLength*runway.getWidth()/logicalRunway.getTora();
        double clearwayLength = logicalRunway.getClearway();
        double clearwayWidthPx = clearwayLength*runway.getWidth()/logicalRunway.getTora();
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
}
