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
    private Line blastProtectionLength;
    @FXML
    private Line blastProtectionStart;
    @FXML
    private Label blastProtectionLabel;
    @FXML
    private Line blastProtectionLength1;
    @FXML
    private Line blastProtectionEnd;
    @FXML
    private Label blastProtectionLabel1;

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
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel());
    }

    public void updateLabel(){
        LogicalRunway llogRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(0);
        LogicalRunway rlogRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(1);
        Obstacle obstacle = MainController.obstacleProperty.get();
        showOriginalParameters();
        setNewLine("TORA",llogRunway,obstacle,toraStart,toraLength,toraEnd,toraLabel);
        setUpBlastProtection(Calculator.needRedeclare(obstacle, llogRunway) && Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.talo), false, blastProtectionLabel, blastProtectionLength, blastProtectionStart, toraStart);
        setNewLine("LDA",llogRunway,obstacle,ldaStart,ldaLength,ldaEnd,ldaLabel);
        setNewLine("ASDA",llogRunway,obstacle,asdaStart,asdaLength,asdaEnd,asdaLabel);
        setNewLine("TODA",llogRunway,obstacle,todaStart,todaLength,todaEnd,todaLabel);

        double distThreshold = llogRunway.getLda()-obstacle.getDistFThreshold();
        Obstacle tempObs = new Obstacle(obstacle.getName(), obstacle.getHeight(), obstacle.getWidth(), obstacle.getDistFCent(), distThreshold);
        setInvertedNewLine("TORA",rlogRunway,tempObs,toraStart1,toraLength1,toraEnd1,toraLabel1);
        setInvertedNewLine("LDA",rlogRunway,tempObs,ldaStart1,ldaLength1,ldaEnd1,ldaLabel1);
        setInvertedNewLine("ASDA",rlogRunway,tempObs,asdaStart1,asdaLength1,asdaEnd1,asdaLabel1);
        setInvertedNewLine("TODA",rlogRunway,tempObs,todaStart1,todaLength1,todaEnd1,todaLabel1);
        setUpBlastProtection(Calculator.needRedeclare(obstacle, llogRunway) && Calculator.getFlightMethod(obstacle, llogRunway).equals(Calculator.ttlt), true, blastProtectionLabel1, blastProtectionLength1, blastProtectionEnd, toraEnd1);
    }

    protected void setUpBlastProtection(boolean bool, boolean lower, Label label, Line length, Line startEnd, Line toraRef){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);
        label.setText("Blast protection" +
                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
        if(lower){
            startEnd.setLayoutX(toraRef.getLayoutX()+(PhysicalRunway.getBlastProtection()*meterPerPx()));
            length.setLayoutX(toraRef.getLayoutX());
            length.setEndX(startEnd.getLayoutX()-toraRef.getLayoutX());
            label.setLayoutX(toraRef.getLayoutX()-(toraRef.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
        } else{
            startEnd.setLayoutX(toraRef.getLayoutX()-(PhysicalRunway.getBlastProtection()*meterPerPx()));
            length.setLayoutX(startEnd.getLayoutX());
            length.setEndX(toraRef.getLayoutX()-startEnd.getLayoutX());
            label.setLayoutX(startEnd.getLayoutX()+(toraRef.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
        }
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
        double differenceInPx = (originalValue - newValue)*meterPerPx();
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
        double labelLayout = start.getLayoutX() + (end.getLayoutX()-start.getLayoutX())/2-label.getWidth()/2;
        label.setText(" " + type +" = " + newValue + "m ");
        label.setLayoutX(labelLayout);
    }

    protected void setInvertedNewLine(String type, LogicalRunway logicalRunway,Obstacle obstacle,Line start,Line length,Line end,Label label){
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
        double differenceInPx = (originalValue - newValue)*runway.getWidth()/logicalRunway.getTora();
        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;
        if (flightMethod.equals(ttlt)){
            start.setLayoutX(start.getLayoutX() + differenceInPx);
            length.setLayoutX(length.getLayoutX() + differenceInPx);
            length.setEndX(length.getEndX() - differenceInPx);
        } else if (flightMethod.equals(talo)) {
            end.setLayoutX(end.getLayoutX() - differenceInPx);
            length.setEndX(length.getEndX() - differenceInPx);
        }
        double labelLayout = start.getLayoutX() + (end.getLayoutX()-start.getLayoutX())/2-label.getWidth()/2;
        label.setText(" " + type +" = " + newValue + "m ");
        label.setLayoutX(labelLayout);
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void relocateObstacle(){
        obstacleBlock.setVisible(true);
        Obstacle obstacle = MainController.obstacleProperty.get();
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

        setUpTora(physRunway);
        setUpLda(physRunway);
        setUpToda(physRunway);
        setUpAsda(physRunway);
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

    private void setUpTora(PhysicalRunway physRunway){
        LogicalRunway llogRunway = physRunway.getLogicalRunways().get(0);
        LogicalRunway rlogRunway = physRunway.getLogicalRunways().get(1);
        double loriginalStartX = thresholdL.getLayoutX();
        double loriginalEndX = thresholdR.getLayoutX();
        double ltora = llogRunway.getTora();
        double rtora = rlogRunway.getTora();
        //Reset the starting line
        toraStart.setLayoutX(loriginalStartX);
        toraEnd.setLayoutX(loriginalEndX);
        toraLength.setLayoutX(loriginalStartX);
        toraLength.setEndX(loriginalEndX - loriginalStartX);
        toraLabel.setText(" TORA = " + ltora);


        toraEnd1.setLayoutX(loriginalEndX);
        toraStart1.setLayoutX(loriginalStartX - (rtora-ltora)*runway.getWidth()/ltora);
        toraLength1.setLayoutX(loriginalStartX - (rtora-ltora)*runway.getWidth()/ltora);
        toraLength1.setEndX(toraLength1.getStartX() + toraEnd1.getLayoutX()-toraStart1.getLayoutX());
        toraLabel1.setText(" TORA = " + rtora);
    }

    private void setUpLda(PhysicalRunway physRunway){
        LogicalRunway llogRunway = physRunway.getLogicalRunways().get(0);
        LogicalRunway rlogRunway = physRunway.getLogicalRunways().get(1);
        double loriginalStartX;
        double loriginalEndX = thresholdR.getLayoutX();
        double llda = llogRunway.getLda();
        double rlda = rlogRunway.getLda();

        //Make displacedThreshold the starting point if it exists
        if (llogRunway.getDisplacedThreshold() != 0){
            loriginalStartX = displacedThresholdL.getLayoutX();
        }else {
            loriginalStartX = thresholdL.getLayoutX();
        }
        //Reset the starting line
        ldaStart.setLayoutX(loriginalStartX);
        ldaEnd.setLayoutX(loriginalEndX);
        ldaLength.setLayoutX(ldaStart.getLayoutX());
        ldaLength.setEndX(loriginalEndX-loriginalStartX);
        ldaLabel.setText(" LDA = " + llda);

        double roriginalStartX = thresholdL.getLayoutX();
        double roriginalEndX;
        if(rlogRunway.getDisplacedThreshold() != 0){
            roriginalEndX = displacedThresholdR.getLayoutX();
        } else{
            roriginalEndX = thresholdR.getLayoutX();
        }

        ldaEnd1.setLayoutX(roriginalEndX);
        ldaStart1.setLayoutX(roriginalStartX - (rlogRunway.getTora()- llogRunway.getTora())*meterPerPx());
        ldaLength1.setLayoutX(ldaStart1.getLayoutX());
        ldaLength1.setEndX(ldaEnd1.getLayoutX()-ldaStart1.getLayoutX());
        ldaLabel1.setText(" LDA = " + rlda);
    }

    private void setUpAsda(PhysicalRunway physRunway){
        LogicalRunway llogRunway = physRunway.getLogicalRunways().get(0);
        LogicalRunway rlogRunway = physRunway.getLogicalRunways().get(1);
        //Set Up Variables
        double loriginalStartX = thresholdL.getLayoutX();
        double loriginalEndX;
        double lasda = llogRunway.getAsda();
        if (llogRunway.getStopway() != 0){
            loriginalEndX = stopwayR.getLayoutX()+stopwayR.getWidth();
        }else {
            loriginalEndX = thresholdR.getLayoutX();
        }
        asdaStart.setLayoutX(loriginalStartX);
        asdaEnd.setLayoutX(loriginalEndX);
        asdaLength.setLayoutX(loriginalStartX);
        asdaLength.setEndX(loriginalEndX - loriginalStartX);
        asdaLabel.setText(" ASDA = " + lasda);

        double roriginalStartX;
        double roriginalEndX = thresholdR.getLayoutX();
        double rasda = rlogRunway.getAsda();
        if(rlogRunway.getStopway() != 0){
            roriginalStartX = stopwayL.getLayoutX();
        } else{
            roriginalStartX = thresholdL.getLayoutX();
        }
        asdaStart1.setLayoutX(roriginalStartX - (rlogRunway.getTora()- llogRunway.getTora())*meterPerPx());
        asdaEnd1.setLayoutX(roriginalEndX);
        asdaLength1.setLayoutX(asdaStart1.getLayoutX());
        asdaLength1.setEndX(asdaEnd1.getLayoutX()-asdaStart1.getLayoutX());
        asdaLabel1.setText(" ASDA = " + rasda);
    }

    private void setUpToda(PhysicalRunway physRunway){
        LogicalRunway llogRunway = physRunway.getLogicalRunways().get(0);
        LogicalRunway rlogRunway = physRunway.getLogicalRunways().get(1);

        //Set Up Variables
        double loriginalStartX = thresholdL.getLayoutX();
        double loriginalEndX;
        double ltoda = llogRunway.getToda();
        if (llogRunway.getClearway() != 0){
            loriginalEndX = clearwayR.getLayoutX()+clearwayR.getWidth();
        }else {
            loriginalEndX = thresholdR.getLayoutX();
        }
        todaStart.setLayoutX(loriginalStartX);
        todaEnd.setLayoutX(loriginalEndX);
        todaLength.setLayoutX(loriginalStartX);
        todaLength.setEndX(loriginalEndX - loriginalStartX);
        todaLabel.setText(" TODA = " + ltoda);

        double roriginalStartX;
        double roriginalEndX = thresholdR.getLayoutX();
        double rtoda = rlogRunway.getToda();
        if(rlogRunway.getClearway() != 0){
            roriginalStartX = clearwayL.getLayoutX();
        } else{
            roriginalStartX = thresholdL.getLayoutX();
        }
        todaStart1.setLayoutX(roriginalStartX - (rlogRunway.getTora()- llogRunway.getTora())*meterPerPx());
        todaEnd1.setLayoutX(roriginalEndX);
        todaLength1.setLayoutX(todaStart1.getLayoutX());
        todaLength1.setEndX(todaEnd1.getLayoutX()-todaStart1.getLayoutX());
        todaLabel1.setText(" TODA = " + rtoda);
    }
}
