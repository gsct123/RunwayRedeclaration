package Model;

public class Calculator {
    //als and tocs
    private final static double resa = 240;
    private final static double blastProtection = 300;
    private final static double stripEnd = 60;

    //calculate Take-off Away and Landing Over
    public static void calcTALO(Obstacle obstacle, LogicalRunway runways){
        double tora, toda, asda, lda;
        tora = runways.getTora() - blastProtection - obstacle.getDistFThreshold() - runways.getDisplacedThreshold();

        asda = tora + runways.getStopway();

        toda = tora + runways.getClearway();

        if (resa > obstacle.getAlsTocs()){
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - resa - obstacle.getWidth();
        } else if (blastProtection > obstacle.getAlsTocs()) {
            //not sure
            lda = runways.getLda() - obstacle.getDistFThreshold() - blastProtection - obstacle.getWidth();
        } else {
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - obstacle.getAlsTocs();
        }
        runways.setNewParameter(tora,asda,toda,lda);
    }

    //calculate Take-off Towards AND Landing Towards
    public static void calcTTLT(Obstacle obstacle, LogicalRunway runways){
        double tora =0;
        double toda, asda, lda;
        if (resa > obstacle.getAlsTocs()){
            //not sure
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - resa - obstacle.getWidth() - stripEnd;
        }else {
            tora = obstacle.getDistFThreshold() + runways.getDisplacedThreshold() - obstacle.getAlsTocs() - stripEnd;
        }
        asda = tora;
        toda = tora;
        lda = obstacle.getDistFThreshold() - stripEnd - resa;
        runways.setNewParameter(tora,asda,toda,lda);
    }
}
