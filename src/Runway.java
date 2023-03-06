public class Runway {
    private double length;
    private RunwayDetails leftRunway, rightRunway;

    public Runway(double length, RunwayDetails leftRunway, RunwayDetails rightRunway) {
        this.length = length;
        this.leftRunway = leftRunway;
        this.rightRunway = rightRunway;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public RunwayDetails getLeftRunway() {
        return leftRunway;
    }

    public void setLeftRunway(RunwayDetails leftRunway) {
        this.leftRunway = leftRunway;
    }

    public RunwayDetails getRightRunway() {
        return rightRunway;
    }

    public void setRightRunway(RunwayDetails rightRunway) {
        this.rightRunway = rightRunway;
    }
}
