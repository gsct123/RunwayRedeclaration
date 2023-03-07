package Model;

public class LogicalRunway {
    private String designator;
    private double tora;
    private double toda;
    private double asda;
    private double lda;
    private double newTora;
    private double newToda;
    private double newAsda;
    private double newLda;

    //not sure if will be use
    //displacedThreshold;
    //private double clearway;
    //private double stopway;
    //private double minCGArea;
    //private double maxCGArea;

    public LogicalRunway(String designator, double tora, double toda, double asda, double lda) {
        this.designator = designator;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
    }

    public void setNewParameter(double newTora,double newAsda, double newToda, double newLda){
        this.newTora = newTora;
        this.newAsda = newAsda;
        this.newToda = newToda;
        this.newLda  = newLda ;
    }

    @Override
    public String toString() {
        return "LogicalRunway{" +
                "designator='" + designator + '\'' +
                ", tora=" + tora +
                ", toda=" + toda +
                ", asda=" + asda +
                ", lda=" + lda +
                ", newTora=" + newTora +
                ", newToda=" + newToda +
                ", newAsda=" + newAsda +
                ", newLda=" + newLda +
                '}';
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

    public double getClearway() {
        return getToda() - getTora();
    }

    public double getStopway() {
        return getTora() - getAsda();
    }

    public double getDisplacedThreshold() {
        return getTora() - getLda();
    }

    public double getNewTora() {
        return newTora;
    }

    public double getNewToda() {
        return newToda;
    }

    public double getNewAsda() {
        return newAsda;
    }

    public double getNewLda() {
        return newLda;
    }
}