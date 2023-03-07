package Model;

public class Calculator {
    private final static double resa = 240;
    private final static double blastProtection = 300;
    private final static double stripEnd = 60;

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
            lda = runways.getLda() - obstacle.getDistFThreshold() - stripEnd - resa - obstacle.getWidth();
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
}