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
        double originalTora = runway.getTora();
        double newTora;
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double displacedThreshold = runway.getDisplacedThreshold();
        double displacedLandingThreshold = getDisplacedLandingThreshold(obstacle.getAlsTocs(),ttlt);

        if(flightMethod.equals(talo)){
            newTora = originalTora - blastProtection - distanceFromThreshold - displacedThreshold;
        }else {
            //if flight method is TTLT,
            newTora = distanceFromThreshold + displacedThreshold - displacedLandingThreshold;
        }
        runway.setNewTora(newTora);
        return newTora;
    }

    public static double calcLda(Obstacle obstacle,LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double originalLda = runway.getLda();
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double newLda;
        double displacedLandingThreshold = getDisplacedLandingThreshold(obstacle.getAlsTocs(),talo);

        if (flightMethod.equals(talo)){
            newLda = originalLda - distanceFromThreshold - displacedLandingThreshold;
        }else {
            newLda = distanceFromThreshold - stripEnd - resa;
        }
        runway.setNewLda(newLda);
        return newLda;
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

    // For both flight method TTLT(Takeoff Towards Landing Towards) and TALO(Takeoff Away Landing Over),
    // If alsTocs(height of obstacle * 50) is less than RESA, displaced threshold will be (resa + strip) end otherwise it will be (alsTocs + strip end).
    // Only if the flight method is TALO(Takeoff Away Landing Over),
    // When either (resa + strip) or (alsTocs + strip end) is less than blast protection value, we need to add blast protection value into Displaced Landing Threshold.
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
        if (flightPath.equals("Take-Off Away Landing Over")){
            ldaOrToraChoice = ldaBreakdownChoice(obstacle, runways);
            result += "TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold\n";
            result += "         = " + runways.getTora() + " - " + blastProtection + " - " + obstacle.getDistFThreshold() + " - " + runways.getDisplacedThreshold() + "\n         = " + calcTora(obstacle, runways) + "\n\n";
            result += "ASDA = (R) TORA + STOPWAY\n";
            result += "         = " + runways.getNewTora() + " + " + runways.getStopway() + "\n         = " + calcAsda(obstacle,runways) + "\n\n";
            result += "TODA = (R) TORA + CLEARWAY\n";
            result += "         = " + runways.getNewTora() + " + " + runways.getClearway() + "\n         = " + calcToda(obstacle,runways) + "\n\n";
            if (ldaOrToraChoice == 1){
                result += "LDA  = Original LDA - Distance from threshold - Strip End - RESA - Obstacle Width\n";
                result += "        = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa+" - " + obstacle.getWidth() + "\n        = " + calcLda(obstacle, runways) + "\n\n";
            }
            else if (ldaOrToraChoice == 2){
                result += "LDA  = Original LDA - Distance from threshold - Blast Protection - Obstacle Width\n";
                result += "        = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + blastProtection + " - " +
                        obstacle.getWidth() + "\n        = " + calcLda(obstacle, runways) + "\n\n";
            }
            else {
                result += "LDA  = Original LDA - Distance from threshold - Strip End - Slope Calculation\n";
                result += "        = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + obstacle.getAlsTocs() + "\n        = " + calcLda(obstacle, runways) + "\n\n";
            }
        }
        else {
            ldaOrToraChoice = toraBreakdownChoice(obstacle, runways);
            if (ldaOrToraChoice == 1){
                result += "TORA = Distance from threshold + Displaced Threshold - RESA - Obstacle Width - Strip End\n";
                result += "         = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + resa + " - " + obstacle.getWidth() + " - " + stripEnd + "\n         = " + calcTora(obstacle, runways) + "\n";
            }
            else{
                result += "TORA =  Distance from threshold + Displaced Threshold - Slope Calculation - Strip End\n";
                result += "         = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + obstacle.getAlsTocs() + " - " + stripEnd + "\n         = " + calcTora(obstacle, runways) + "\n\n";
            }
            result += "ASDA = (R) TORA\n";
            result += "         = " + calcTora(obstacle, runways) + "\n         = " + calcAsda(obstacle,runways) + "\n\n";
            result += "TODA = (R) TORA\n";
            result += "         = " + calcTora(obstacle, runways) + "\n         = " + calcToda(obstacle,runways) + "\n\n";
            result += "LDA  = Distance from Threshold - Strip End - RESA\n";
            result += "        = " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa + "\n        = " + calcLda(obstacle, runways) + "\n\n";
        }
        result += "\n";
        return result;
    }


}