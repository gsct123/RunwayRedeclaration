package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SideViewController {
    public Rectangle pavement;
    public Label toraLabel;
    public Label obstacleLabel;
    public Label lRLabel;
    public Label mppLabel;
    @FXML
    private Line middle;
    @FXML
    private Line start;
    @FXML
    private Line end;
    @FXML
    private Polygon head;
    @FXML
    private TextField lineLength;
    @FXML
    private Button executeButton;

    private double length = 0;
    LogicalRunway logicalRunway = new LogicalRunway("09L",3902,3902,3902,3595);
    Obstacle obstacle = new Obstacle("obstacle1",12,10,0,-50);
    @FXML
    private void initialize() {
        lineLength.setOnKeyTyped( mouseEvent -> {
            try {
                if (!lineLength.getText().isEmpty()) {
                    length = Double.parseDouble(lineLength.getText());
                    System.out.println(lineLength.getText());
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        });
    }

    @FXML
    private void handleExecuteButtonClick(ActionEvent event){
        Calculator.performCalc(obstacle,logicalRunway);
        double changeInTora = logicalRunway.getNewTora() - logicalRunway.getTora();
        System.out.println(logicalRunway.getNewTora());
        System.out.println(logicalRunway.getTora());
        double MPP = getMeterPerPx(logicalRunway);
        double px = getPx(changeInTora);

        System.out.println(px);
        head.setLayoutX(head.getLayoutX()+px);
        end.setLayoutX(end.getLayoutX()+px);
        middle.setEndX(middle.getEndX()+px);
        System.out.println("Button clicked");

        obstacleLabel.setText(obstacleLabel.getText() + obstacle.toString());
        System.out.println(obstacle.toString());
        lRLabel.setText(lRLabel.getText() + logicalRunway.toString());
        System.out.println(logicalRunway.toString());
        mppLabel.setText(mppLabel.getText() + getMeterPerPx(logicalRunway));
        toraLabel.setText(toraLabel.getText() + logicalRunway.getNewTora());

    }

    @FXML
    private void getLength(ActionEvent event){
        lineLength.setOnKeyTyped( mouseEvent -> {
            try {
                if (!lineLength.getText().isEmpty()) {
                        length = Double.parseDouble(lineLength.getText());
                        System.out.println(lineLength.getText());
                    }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        });
    }

    public double getMeterPerPx(LogicalRunway logicalRunway){
        double ans = logicalRunway.getTora()/ pavement.getWidth();
        System.out.println(ans);
        return ans;
    }

    public double getPx(double length){
        double ans = length/getMeterPerPx(logicalRunway);
        System.out.println(length + "meter = " + ans + " pixel");
        return ans;
    }


}
