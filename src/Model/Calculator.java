package Model;

public class Calculator {
    private static final double resa = 240;
    private static final double blastProtection = 300;
    private static final double stripEnd = 60;
    private static final double minCGArea = 75;
    //empty constructor
    public Calculator() {}
    public static final double maxCGArea = 150;
    public static final String talo = "Take-Off Away Landing Over";
    public static final String ttlt = "Take-Off Towards Landing Towards";

    //testing only
    public static void main(String[] args) {
        LogicalRunway runway = new LogicalRunway("09l", 3902, 3902, 3902, 3595);
        Obstacle obstacle = new Obstacle("obs1", 12, 0, 0, 50);
        getCalculationBreakdownT(obstacle, runway, Calculator.getFlightMethod(obstacle, runway));
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

    //TALO = Takeoff Away Landing Over
    //when taking off away / landing over, ASDA and TODA needs to include clearway and stopway
    public static double calcAsda_TALO(LogicalRunway runways){
        double newAsda =  runways.getNewTora() + runways.getStopway();
        runways.setNewAsda(newAsda);
        return newAsda;
    }
    public static double calcToda_TALO(LogicalRunway runways){
        double newToda = runways.getNewTora() + runways.getClearway();
        runways.setNewToda(newToda);
        return newToda;
    }

    //TTLT = Takeoff Towards Landing Towards
    //when taking off towards and landing towards, ASDA and TODA don't have to include stopway and clearway
    public static double calcAsda_TTLT(LogicalRunway runways){
        runways.setNewAsda(runways.getNewTora());
        return runways.getNewTora();
    }
    public static double calcToda_TTLT(LogicalRunway runways){
        runways.setNewToda(runways.getNewTora());
        return runways.getNewTora();
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

    public static int ldaBreakdownChoice(Obstacle obstacle, LogicalRunway runways){
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

    public static int toraBreakdownChoice(Obstacle obstacle, LogicalRunway runways){
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
            result += "     = " + runways.getNewTora() + " + " + runways.getStopway() + " = " + calcAsda_TALO(runways) + "\n";
            result += "TODA = (R) TORA + CLEARWAY\n";
            result += "     = " + runways.getNewTora() + " + " + runways.getClearway() + " = " + calcToda_TALO(runways) + "\n";
            if (ldaOrToraChoice == 1){
                result += "LDA  = Original LDA - Distance from threshold - Strip End - RESA - Obstacle Width\n";
                result += "LDA  = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa+" - " + obstacle.getWidth() + " = " + calcLda(obstacle, runways) + "\n";
            }
            else if (ldaOrToraChoice == 2){
                result += "LDA  = Original LDA - Distance from threshold - Blast Protection - Obstacle Width\n";
                result += "LDA  = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + blastProtection + " - " +
                        obstacle.getWidth() + " = " + calcLda(obstacle, runways) + "\n";
            }
            else {
                result += "LDA  = Original LDA - Distance from threshold - Strip End - Slope Calculation\n";
                result += "LDA: " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + obstacle.getAlsTocs() + " = " + calcLda(obstacle, runways) + "\n";
            }
        }
        else {
            ldaOrToraChoice = toraBreakdownChoice(obstacle, runways);
            if (ldaOrToraChoice == 1){
                result += "TORA = Distance from threshold + Displaced Threshold - RESA - Obstacle Width - Strip End\n";
                result += "TORA = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + resa + " - " + obstacle.getWidth() + " - " + stripEnd + " = " + calcTora(obstacle, runways) + "\n";
            }
            else{
                result += "TORA =  Distance from threshold + Displaced Threshold - Slope Calculation - Strip End\n";
                result += "TORA = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + obstacle.getAlsTocs() + " - " + stripEnd + " = " + calcTora(obstacle, runways) + "\n";
            }
            result += "ASDA = (R) TORA\n";
            result += "ASDA = " + calcTora(obstacle, runways) + " = " + calcAsda_TTLT(runways) + "\n";
            result += "TODA = (R) TORA\n";
            result += "TODA = " + calcTora(obstacle, runways) + " = " + calcToda_TTLT(runways) + "\n";
            result += "LDA  = Distance from Threshold - Strip End - RESA\n";
            result += "LDA  = " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa + " = " + calcLda(obstacle, runways) + "\n";
        }
        result += "\n";
        return result;
    }


}