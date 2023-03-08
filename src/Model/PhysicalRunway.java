package Model;

import javafx.collections.ObservableList;

public class PhysicalRunway{
    private String name;
    private ObservableList<LogicalRunway> logicalRunways;
    //Obstacle is on a physical runway, one obstacle could affect all the logical runway in a physical runway.
    //There could only be ONE obstacle according to the requirement
    private Obstacle obstacle;

    @Override
    public String toString() {
        return "PhysicalRunway{" +
                "name='" + name + '\'' +
                "\nlogicalRunways=" + logicalRunways +
                "\nobstacle=" + obstacle +
                '}';
    }

    public PhysicalRunway(String name, ObservableList<LogicalRunway> logicalRunways, Obstacle obstacle){
        this.name = name;
        this.logicalRunways = logicalRunways;
        this.obstacle = obstacle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<LogicalRunway> getLogicalRunways() {
        return logicalRunways;
    }

    public void setLogicalRunways(ObservableList<LogicalRunway> logicalRunways) {
        this.logicalRunways = logicalRunways;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }
}