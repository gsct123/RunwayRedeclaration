public class Obstacle {
    private String name, nearestDesignator;
    private double height, width, distFCenterLine, distFThreshold;

    public Obstacle(String name, double height, double width, double distFCenterLine, String nearestDesignator, double distFThreshold) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.distFCenterLine = distFCenterLine;
        this.nearestDesignator = nearestDesignator;
        this.distFThreshold = distFThreshold;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNearestDesignator() {
        return nearestDesignator;
    }

    public void setNearestDesignator(String nearestDesignator) {
        this.nearestDesignator = nearestDesignator;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDistFCenterLine() {
        return distFCenterLine;
    }

    public void setDistFCenterLine(double distFCenterLine) {
        this.distFCenterLine = distFCenterLine;
    }

    public double getDistFThreshold() {
        return distFThreshold;
    }

    public void setDistFThreshold(double distFThreshold) {
        this.distFThreshold = distFThreshold;
    }
}
