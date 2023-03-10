package Model;

public class Calculator {
    private static final double resa = 240;
    private static final double blastProtection = 300;
    private static final double stripEnd = 60;
    private static final double minCGArea = 75;

    //empty constructor
    public Calculator() {}
    private static final double maxCGArea = 150;
    private static final String talo = "Take-Off Away Landing Over";
    private static final String ttlt = "Take-Off Towards Landing Towards";

    //testing only
    public static void main(String[] args) {
        LogicalRunway runway = new LogicalRunway("09l", 3902, 3902, 3902, 3595);
        Obstacle obstacle = new Obstacle("obs1", 12, 0, 0, 50);
        //improvement: use Calculator.getFlightMethod inside the function getCalculationBreakdownT to get the flight method
        getCalculationBreakdownT(obstacle, runway, Calculator.getFlightMethod(obstacle, runway));
    }
    //check if the obstacle is within the strip end, or minimum clear graded area
    //the declaration needs to be redeclare if true.
    public static boolean needRedeclare(Obstacle obstacle, LogicalRunway logicalRunway){
        boolean withinStripEnd = obstacle.getDistFThreshold() <= logicalRunway.getTora() + stripEnd && obstacle.getDistFThreshold() >= -stripEnd;
        boolean withinCentreline = obstacle.getDistFCent() <= minCGArea && obstacle.getDistFCent() >= -minCGArea;
        return withinStripEnd && withinCentreline;
    }

    //TA = TakeOff Away
    public static double calcTora_TA(Obstacle obstacle, LogicalRunway runways){
        double tora = runways.getTora() - blastProtection - obstacle.getDistFThreshold() - runways.getDisplacedThreshold();
        runways.setNewTora(tora);
        return tora;
    }
    //LO = Landing Over
    public static double calcLda_LO(Obstacle obstacle, LogicalRunway runways){
        double lda;
        if (resa > obstacle.getAlsTocs()){
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - resa ;
        } else if (blastProtection > obstacle.getAlsTocs()) {
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - blastProtection - obstacle.getWidth();
        } else {
            //LDA = Original LDA - dist from threshold - stripEnd (60) - Als (height * 50)
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - obstacle.getAlsTocs();
        }
        runways.setNewLda(lda);
        return lda;
    }

    public static double getDisplacedLandingThreshold(Double alsTocs){
        double displacedLandingThreshold;
        if (alsTocs < resa){
            displacedLandingThreshold = resa + stripEnd;
        }else {
            displacedLandingThreshold = alsTocs + stripEnd;
        }
        //This condition is given in the definition based on different plane can have different blastProtection value/runway can have different resa
        //But in here we just assume they're all the same, thus this will be always true, if alsTocs lesser than resa, (if the condition (obstacle.getAlsTocs() < resa) is true, (displacedLandingThreshold <= blastProtection) will also always be true)
        //since blastprotection value must be greater than resa + stripEnd/AlsTocs + stripEnd
        if (displacedLandingThreshold <= blastProtection){
            displacedLandingThreshold += blastProtection;
        }
        return displacedLandingThreshold;
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

    //TT = TakeOff Towards
    public static double calcTora_TT(Obstacle obstacle, LogicalRunway runways){
        double tora;
        if (resa > obstacle.getAlsTocs()){
            //not sure
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - resa - obstacle.getWidth() - stripEnd;
        }else {
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - obstacle.getAlsTocs() - stripEnd;
        }
        runways.setNewTora(tora);
        return tora;
    }

    //LT = Landing Towards
    public static double calcLda_LT(Obstacle obstacle, LogicalRunway runways){
        double lda = obstacle.getDistFThreshold() - stripEnd - resa;
        runways.setNewLda(lda);
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

    public static String getCalculationBreakdownT(Obstacle obstacle, LogicalRunway runways, String flightPath){
        int ldaOrToraChoice;
        String result = "";
        result += "Calculation Breakdown:\n";
        if (flightPath.equals("Take-Off Away Landing Over")){
            ldaOrToraChoice = ldaBreakdownChoice(obstacle, runways);
            result += "TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold\n";
            result += "     = " + runways.getTora() + " - " + blastProtection + " - " + obstacle.getDistFThreshold() + " - " + runways.getDisplacedThreshold() + " = " + calcTora_TA(obstacle, runways) + "\n";
            result += "ASDA = (R) TORA + STOPWAY\n";
            result += "     = " + runways.getNewTora() + " + " + runways.getStopway() + " = " + calcAsda_TALO(runways) + "\n";
            result += "TODA = (R) TORA + CLEARWAY\n";
            result += "     = " + runways.getNewTora() + " + " + runways.getClearway() + " = " + calcToda_TALO(runways) + "\n";
            if (ldaOrToraChoice == 1){
                result += "LDA  = Original LDA - Distance from threshold - Strip End - RESA - Obstacle Width\n";
                result += "LDA  = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa+" - " + obstacle.getWidth() + " = " + calcLda_LO(obstacle, runways) + "\n";
            }
            else if (ldaOrToraChoice == 2){
                result += "LDA  = Original LDA - Distance from threshold - Blast Protection - Obstacle Width\n";
                result += "LDA  = " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + blastProtection + " - " +
                        obstacle.getWidth() + " = " + calcLda_LO(obstacle, runways) + "\n";
            }
            else {
                result += "LDA  = Original LDA - Distance from threshold - Strip End - Slope Calculation\n";
                result += "LDA: " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + obstacle.getAlsTocs() + " = " + calcLda_LO(obstacle, runways) + "\n";
            }
        }
        else {
            ldaOrToraChoice = toraBreakdownChoice(obstacle, runways);
            if (ldaOrToraChoice == 1){
                result += "TORA = Distance from threshold + Displaced Threshold - RESA - Obstacle Width - Strip End\n";
                result += "TORA = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + resa + " - " + obstacle.getWidth() + " - " + stripEnd + " = " + calcTora_TT(obstacle, runways) + "\n";
            }
            else{
                result += "TORA =  Distance from threshold + Displaced Threshold - Slope Calculation - Strip End\n";
                result += "TORA = " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + obstacle.getAlsTocs() + " - " + stripEnd + " = " + calcTora_TT(obstacle, runways) + "\n";
            }
            result += "ASDA = (R) TORA\n";
            result += "ASDA = " + calcTora_TT(obstacle, runways) + " = " + calcAsda_TTLT(runways) + "\n";
            result += "TODA = (R) TORA\n";
            result += "TODA = " + calcTora_TT(obstacle, runways) + " = " + calcToda_TTLT(runways) + "\n";
            result += "LDA  = Distance from Threshold - Strip End - RESA\n";
            result += "LDA  = " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa + " = " + calcLda_LT(obstacle, runways) + "\n";
        }
        result += "\n";
        return result;
    }


}