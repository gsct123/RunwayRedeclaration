package Model;

public class Calculator {
    //als and tocs
    private final static double resa = 240;
    private final static double blastProtection = 300;
    private final static double stripEnd = 60;

    //calculate Take-off Away and Landing Over
    public static void calcTALO(Obstacle obstacle, LogicalRunway runways){
        int ldaChoice;
        double tora, toda, asda, lda;
        tora = runways.getTora() - blastProtection - obstacle.getDistFThreshold() - runways.getDisplacedThreshold();

        asda = tora + runways.getStopway();

        toda = tora + runways.getClearway();

        if (resa > obstacle.getAlsTocs()){
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - resa - obstacle.getWidth();
            ldaChoice = 1;
        } else if (blastProtection > obstacle.getAlsTocs()) {
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - blastProtection - obstacle.getWidth();
            ldaChoice = 2;
        } else {
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - obstacle.getAlsTocs();
            ldaChoice = 3;
        }
        runways.setNewParameter(tora,asda,toda,lda);
        printCalculationBreakdown(obstacle, runways, tora, toda, asda, lda, 1, ldaChoice);
    }

    //calculate Take-off Towards AND Landing Towards
    public static void calcTTLT(Obstacle obstacle, LogicalRunway runways){
        int toraChoice;
        double tora =0;
        double toda, asda, lda;
        if (resa > obstacle.getAlsTocs()){
            //not sure
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - resa - obstacle.getWidth() - stripEnd;
            toraChoice = 1;
        }else {
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - obstacle.getAlsTocs() - stripEnd;
            toraChoice = 2;
        }
        asda = tora;
        toda = tora;
        lda = obstacle.getDistFThreshold() - stripEnd - resa;
        runways.setNewParameter(tora,asda,toda,lda);
        printCalculationBreakdown(obstacle, runways, tora, toda, asda, lda, 1, toraChoice);
    }
    public static void printCalculationBreakdown(Obstacle obstacle, LogicalRunway runways, double tora, double toda, double asda, double lda, int breakdownChoice, int ldaOrToraChoice){
        System.out.println("Calculation Breakdown:");
        if (breakdownChoice == 1){
            System.out.println("TORA: " + runways.getTora() + " - " + blastProtection + " - " + obstacle.getDistFThreshold() + " - " + runways.getDisplacedThreshold() + " = " + tora);
            System.out.println("ASDA: " + tora + " + " + runways.getStopway() + " = " + asda);
            System.out.println("TODA: " + tora + " + " + runways.getClearway() + " = " + toda);
            if (ldaOrToraChoice == 1){
                System.out.println("LDA: " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa+" - " + obstacle.getWidth() + " = " + lda);
            }
            else if (ldaOrToraChoice == 2){
                System.out.println("LDA: " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + blastProtection + " - " + obstacle.getWidth() + " = " + lda);
            }
            else{
                System.out.println("LDA: " + runways.getLda() + " - " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + obstacle.getAlsTocs() + " = " + lda);
            }
        }
        else {
            if (ldaOrToraChoice == 1){
                System.out.println("TORA: " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + resa + " - " + obstacle.getWidth() + " - " + stripEnd + " = " + tora);
            }
            else {
                System.out.println("TORA: " + obstacle.getDistFThreshold() + " + " + runways.getDisplacedThreshold() + " - " + obstacle.getAlsTocs() + " - " + stripEnd + " = " + tora);
            }
            System.out.println("ASDA: " + tora + " = " + asda);
            System.out.println("TODA: " + tora + " = " + toda);
            System.out.println("LDA: " + obstacle.getDistFThreshold() + " - " + stripEnd + " - " + resa + " = " + lda);
        }

    }
}
