public class RunwayDetails {
    private String designator;
    private double tora, toda, asda, lda, newTora, newToda, newAsda, newLda, displacedThreshold, clearway, stopway, resa, minCGArea, maxCGArea, als, tocs;

    public RunwayDetails(String designator, double tora, double toda, double asda, double lda, double newTora, double newToda, double newAsda, double newLda, double displacedThreshold, double clearway, double stopway, double resa, double minCGArea, double maxCGArea, double als, double tocs) {
        this.designator = designator;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
        this.newTora = newTora;
        this.newToda = newToda;
        this.newAsda = newAsda;
        this.newLda = newLda;
        this.displacedThreshold = displacedThreshold;
        this.clearway = clearway;
        this.stopway = stopway;
        this.resa = resa;
        this.minCGArea = minCGArea;
        this.maxCGArea = maxCGArea;
        this.als = als;
        this.tocs = tocs;
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public double getTora() {
        return tora;
    }

    public void setTora(double tora) {
        this.tora = tora;
    }

    public double getToda() {
        return toda;
    }

    public void setToda(double toda) {
        this.toda = toda;
    }

    public double getAsda() {
        return asda;
    }

    public void setAsda(double asda) {
        this.asda = asda;
    }

    public double getLda() {
        return lda;
    }

    public void setLda(double lda) {
        this.lda = lda;
    }

    public double getNewTora() {
        return newTora;
    }

    public void setNewTora(double newTora) {
        this.newTora = newTora;
    }

    public double getNewToda() {
        return newToda;
    }

    public void setNewToda(double newToda) {
        this.newToda = newToda;
    }

    public double getNewAsda() {
        return newAsda;
    }

    public void setNewAsda(double newAsda) {
        this.newAsda = newAsda;
    }

    public double getNewLda() {
        return newLda;
    }

    public void setNewLda(double newLda) {
        this.newLda = newLda;
    }

    public double getDisplacedThreshold() {
        return displacedThreshold;
    }

    public void setDisplacedThreshold(double displacedThreshold) {
        this.displacedThreshold = displacedThreshold;
    }

    public double getClearway() {
        return clearway;
    }

    public void setClearway(double clearway) {
        this.clearway = clearway;
    }

    public double getStopway() {
        return stopway;
    }

    public void setStopway(double stopway) {
        this.stopway = stopway;
    }

    public double getResa() {
        return resa;
    }

    public void setResa(double resa) {
        this.resa = resa;
    }

    public double getMinCGArea() {
        return minCGArea;
    }

    public void setMinCGArea(double minCGArea) {
        this.minCGArea = minCGArea;
    }

    public double getMaxCGArea() {
        return maxCGArea;
    }

    public void setMaxCGArea(double maxCGArea) {
        this.maxCGArea = maxCGArea;
    }

    public double getAls() {
        return als;
    }

    public void setAls(double als) {
        this.als = als;
    }

    public double getTocs() {
        return tocs;
    }

    public void setTocs(double tocs) {
        this.tocs = tocs;
    }
}
