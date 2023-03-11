package Model;

public class Calculator {
    public static final double resa = 240;
    public static final double blastProtection = 300;
    public static final double stripEnd = 60;
    public static final double minCGArea = 75;
    //empty constructor
    public Calculator() {}
    public static final double maxCGArea = 150;
    public static final String talo = "Take-Off Away Landing Over";
    public static final String ttlt = "Take-Off Towards Landing Towards";

    //testing only
    public static void main(String[] args) {
        LogicalRunway runway = new LogicalRunway("09l", 3902, 3902, 3902, 3595);
        Obstacle obstacle = new Obstacle("obs1", 12, 0, 0, 50);
        System.out.println(getCalculationBreakdownT(obstacle, runway, Calculator.getFlightMethod(obstacle, runway)));
    }

    public static double calcTora(Obstacle obstacle,LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double tora;
        if(flightMethod.equals(talo)){
            tora = runway.getTora() - blastProtection - obstacle.getDistFThreshold() - runway.getDisplacedThreshold();
        }else {
            tora = obstacle.getDistFThreshold() + runway.getDisplacedThreshold() - getDisplacedLandingThreshold(obstacle.getAlsTocs(),ttlt);
        }
        runway.setNewTora(tora);
        return tora;
    }

    public static double calcLda(Obstacle obstacle,LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double lda;
        if (flightMethod.equals(talo)){
            lda = runway.getLda() - obstacle.getDistFThreshold() - getDisplacedLandingThreshold(obstacle.getAlsTocs(),talo);
        }else {
            lda = obstacle.getDistFThreshold() - stripEnd - resa;
        }
        runway.setNewLda(lda);
        return lda;
    }

    public static double calcAsda(Obstacle obstacle, LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double newAsda;
        double newTora = runway.getNewTora();
        double stopway = runway.getStopway();

        if (flightMethod.equals(talo)){
            newAsda =  newTora + stopway;
        }else {
            newAsda = newTora;
        }
        runway.setNewAsda(newAsda);
        return newAsda;
    }

    public static double calcToda(Obstacle obstacle, LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double newToda;
        double newTora = runway.getNewTora();
        double clearway = runway.getClearway();

        if (flightMethod.equals(talo)){
            newToda =  newTora + clearway;
        }else {
            newToda = newTora;
        }
        runway.setNewToda(newToda);
        return newToda;
    }

    public static double getDisplacedLandingThreshold(double alsTocs, String flightMethod){
        double displacedLandingThreshold;
        if (alsTocs < resa){
            displacedLandingThreshold = resa + stripEnd;
        }else {
            displacedLandingThreshold = alsTocs + stripEnd;
        }
        if (displacedLandingThreshold <= blastProtection && flightMethod.equals(talo)){
            displacedLandingThreshold += blastProtection;
        }
        return displacedLandingThreshold;
    }

    //check if the obstacle is within the strip end, or minimum clear graded area
    //the declaration needs to be redeclare if true.
    public static boolean needRedeclare(Obstacle obstacle, LogicalRunway logicalRunway){
        boolean withinStripEnd = obstacle.getDistFThreshold() <= logicalRunway.getTora() + stripEnd && obstacle.getDistFThreshold() >= -stripEnd;
        boolean withinCentreline = obstacle.getDistFCent() <= minCGArea && obstacle.getDistFCent() >= -minCGArea;
        return withinStripEnd && withinCentreline;
    }

    public static String getFlightMethod(Obstacle obstacle, LogicalRunway logicalRunway){
        String flightMethod;
        if (obstacle.getDistFThreshold() < logicalRunway.getTora()/2){
            flightMethod = talo;
        }else {
            flightMethod = ttlt;
        }
        return flightMethod;
    }

    private static int ldaBreakdownChoice(Obstacle obstacle, LogicalRunway runways){
        int choice;
        if (resa > obstacle.getAlsTocs()){
            choice = 1;
        } else if (blastProtection > obstacle.getAlsTocs()) {
            choice = 2;
        } else {
            choice = 3;
        }
        return choice;
    }

    private static int toraBreakdownChoice(Obstacle obstacle, LogicalRunway runways){
        int choice;
        if (resa > obstacle.getAlsTocs()){
            choice = 1;
        }else {
            choice = 2;
        }
        return choice;
    }

    public static String getCalculationBreakdownT(Obstacle obstacle, LogicalRunway runways, String flightPath){
        int ldaOrToraChoice;
        String result = "";
        result += "Calculation Breakdown:\n";
        if (flightPath.equals("Take-Off Away Landing Over")){
            ldaOrToraChoice = ldaBreakdownChoice(obstacle, runways);
            result += "TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold\n";
            result += "     = " + runways.getTora() + " - " + blastProtection + " - " + obstacle.getDistFThreshold() + " - " + runways.getDisplacedThreshold() + " = " + calcTora(obstacle, runways) + "\n";
            result += "ASDA = (R) TORA + STOPWAY\n";
            result += "     = " + runways.getNewTora() + " + " + runways.getStopway() + "\n     = " + calcAsda(obstacle,runways) + "\n";
            result += "TODA = (R) TORA + CLEARWAY\n";
            result += "     = " + runways.getNewTora() + " + " + runways.getClearway() + "\n     = " + calcToda(obstacle,runways) + "\n";
            if (ldaOrToraChoice == 1){
                result += "LDA  = Original LDA - Distance from threshold - Strip End - RESA - Obstacle Width\n";
                result += "     = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa+" - " + obstacle.getWidth() + "\n     = " + calcLda(obstacle, runways) + "\n";
            }
            else if (ldaOrToraChoice == 2){
                result += "LDA  = Original LDA - Distance from threshold - Blast Protection - Obstacle Width\n";
                result += "     = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + blastProtection + " - " +
                        obstacle.getWidth() + "\n     = " + calcLda(obstacle, runways) + "\n";
            }
            else {
                result += "LDA  = Original LDA - Distance from threshold - Strip End - Slope Calculation\n";
                result += "     = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + obstacle.getAlsTocs() + "\n     = " + calcLda(obstacle, runways) + "\n";
            }
        }
        else {
            ldaOrToraChoice = toraBreakdownChoice(obstacle, runways);
            if (ldaOrToraChoice == 1){
                result += "TORA = Distance from threshold + Displaced Threshold - RESA - Obstacle Width - Strip End\n";
                result += "     = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + resa + " - " + obstacle.getWidth() + " - " + stripEnd + "\n     = " + calcTora(obstacle, runways) + "\n";
            }
            else{
                result += "TORA =  Distance from threshold + Displaced Threshold - Slope Calculation - Strip End\n";
                result += "     = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + obstacle.getAlsTocs() + " - " + stripEnd + "\n     = " + calcTora(obstacle, runways) + "\n";
            }
            result += "ASDA = (R) TORA\n";
            result += "     = " + calcTora(obstacle, runways) + "\n     = " + calcAsda(obstacle,runways) + "\n";
            result += "TODA = (R) TORA\n";
            result += "     = " + calcTora(obstacle, runways) + "\n     = " + calcToda(obstacle,runways) + "\n";
            result += "LDA  = Distance from Threshold - Strip End - RESA\n";
            result += "     = " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa + "\n     = " + calcLda(obstacle, runways) + "\n";
        }
        result += "\n";
        return result;
    }


}