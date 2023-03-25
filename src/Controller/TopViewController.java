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
import javafx.scene.shape.Polyline;
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
    private Polyline toraLeftArrow;
    @FXML
    private Polyline toraRightArrow;
    @FXML
    private Label toraLabel;
    @FXML
    private Line toraLength;
    //toda
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Label todaLabel;
    @FXML
    private Line todaLength;
    //lda
    @FXML
    private Line ldaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Label ldaLabel;
    @FXML
    private Line ldaLength;
    //asda
    @FXML
    private Line asdaStart;
    @FXML
    private Line asdaEnd;
    @FXML
    private Label asdaLabel;
    @FXML
    private Line asdaLength;

    //other labels
    @FXML
    private Line blastProtectionLength;
    @FXML
    private Line blastProtectionStart;
    @FXML
    private Label blastProtectionLabel;

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
        MainController.logRunwayItem.addListener((observable, oldValue, newValue) -> {
            String[] split = (newValue.getDesignator()+"/"+MainController.physRunwayItem.get().getName()).split("/");
            leftDesignator.setText(split[0]);
            rightDesignator.setText(split[0].equals(split[1])? split[2]: split[1]);
            showOriginalParameters();
        });
        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
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
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel());
    }

    public void updateLabel(){
        LogicalRunway logRunway = MainController.logRunwayItem.get();
        Obstacle obstacle = MainController.obstacleProperty.get();
        showOriginalParameters();
        setNewLine("TORA",logRunway,obstacle,toraStart,toraLength,toraEnd,toraLabel);
        setUpBlastProtection(Calculator.needRedeclare(obstacle, logRunway) && Calculator.getFlightMethod(obstacle, logRunway).equals(Calculator.talo));
        setNewLine("LDA",logRunway,obstacle,ldaStart,ldaLength,ldaEnd,ldaLabel);
        setNewLine("ASDA",logRunway,obstacle,asdaStart,asdaLength,asdaEnd,asdaLabel);
        setNewLine("TODA",logRunway,obstacle,todaStart,todaLength,todaEnd,todaLabel);
    }

    protected void setUpBlastProtection(boolean bool){
        blastProtectionLabel.setVisible(bool);
        blastProtectionLength.setVisible(bool);
        blastProtectionStart.setVisible(bool);
        blastProtectionStart.setLayoutX(toraStart.getLayoutX()-(PhysicalRunway.getBlastProtection()*runway.getWidth()/MainController.getLogRunwaySelected().getTora()));
        blastProtectionLength.setLayoutX(blastProtectionStart.getLayoutX());
        blastProtectionLength.setEndX(toraStart.getLayoutX());
        blastProtectionLabel.setText("Blast protection" +
                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
        blastProtectionLabel.setLayoutX(blastProtectionStart.getLayoutX()+(toraStart.getLayoutX()-blastProtectionStart.getLayoutX())/2-blastProtectionLabel.getWidth()/2);
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
        double differenceInPx = (originalValue - newValue)*runway.getWidth()/logicalRunway.getTora();
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

        logRunway = MainController.logRunwayItem.get();
        tora = MainController.logRunwayItem.get().getTora();
        stripEnd = PhysicalRunway.getStripEnd();
        double displacedFromCentre = obstacle.getDirFromCentre().equals("L")? (-obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)-obsBlockWidth/2: (obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)-obsBlockWidth/2;
        if(displacedFromCentre < background.getHeight()/2 && displacedFromCentre > -background.getHeight()/2){
            if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora)-obsBlockWidth, centre+displacedFromCentre);
            } else{
                obstacleBlock.relocate(runwayStartX+(disFromThreshold*(runwayLength-logRunway.getClearway())/tora), centre+(displacedFromCentre));
            }
        }else{
            obstacleBlock.setVisible(false);
        }
    }

    public void showOriginalParameters(){
        LogicalRunway logRunway = MainController.logRunwayItem.get();
        LogicalRunway lLogicalRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(1);
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;
        if (logRunway.getDesignator().equals(lLogicalRunway.getDesignator())){
            lDisplacedThresholdX = thresholdL.getLayoutX() + lDisplacedThreshold*runway.getWidth()/lLogicalRunway.getTora();
            rDisplacedThresholdX = thresholdR.getLayoutX() - rDisplacedThreshold*runway.getWidth()/lLogicalRunway.getTora();
        }else {
            lDisplacedThresholdX = thresholdL.getLayoutX() + rDisplacedThreshold*runway.getWidth()/lLogicalRunway.getTora();
            rDisplacedThresholdX = thresholdR.getLayoutX() - lDisplacedThreshold*runway.getWidth()/lLogicalRunway.getTora();
        }

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);

        if (lLogicalRunway.getDesignator().equals(logRunway.getDesignator())){
            setStopClearway(rLogicalRunway,"Left");
            setStopClearway(lLogicalRunway,"Right");
        }else {
            setStopClearway(rLogicalRunway,"Right");
            setStopClearway(lLogicalRunway,"Left");
        }

        setUpTora(logRunway);
        setUpLda(logRunway);
        setUpToda(logRunway);
        setUpAsda(logRunway);
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
}
