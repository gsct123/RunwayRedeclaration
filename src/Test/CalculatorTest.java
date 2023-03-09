package Test;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    LogicalRunway lR09L = new LogicalRunway("09L",3902,3902,3902,3595);
    LogicalRunway lR27R = new LogicalRunway("27R",3884,3962,3884,3884);
    Obstacle obstacle1 = new Obstacle("obstacle1",12,0,0,-50);
    Obstacle obstacle2 = new Obstacle("obstacle2",12,0,0,3646);
    @Test
    @DisplayName("Test calculation of Tora for Takeoff Away")
    void calcTora_TA() {
        assertEquals(3345, Calculator.calcTora_TA(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Lda for Landing Over")
    void calcLda_LO() {
        assertEquals(2985,Calculator.calcLda_LO(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Tora for Takeoff Towards")
    void calcTora_TT() {
        assertEquals(2986,Calculator.calcTora_TT(obstacle2,lR27R));
    }

    @Test
    @DisplayName("Test calculation of Lda for Landing Towards")
    void calcLda_LT() {
        assertEquals(2986,Calculator.calcTora_TT(obstacle2,lR27R));
    }

    @Test
    @DisplayName("Test calculation of Asda for Takeoff Away Landing Over")
    void calcAsda_TALO() {
        Calculator.calcTora_TA(obstacle1,lR09L);
        assertEquals(3345,Calculator.calcAsda_TALO(lR09L));
    }

    @Test
    @DisplayName("Test calculation of Toda for Takeoff Away Landing Over")
    void calcToda_TALO() {
        Calculator.calcTora_TA(obstacle1,lR09L);
        assertEquals(3345,Calculator.calcToda_TALO(lR09L));
    }

    @Test
    @DisplayName("Test calculation of Asda for Takeoff Towards Landing Towards")
    void calcAsda_TTLT() {
        Calculator.calcTora_TT(obstacle2,lR27R);
        assertEquals(2986,Calculator.calcAsda_TTLT(lR27R));
    }

    @Test
    @DisplayName("Test calculation of Toda for Takeoff Towards Landing Towards")
    void calcToda_TTLT() {
        Calculator.calcTora_TT(obstacle2,lR27R);
        assertEquals(2986,Calculator.calcToda_TTLT(lR27R));
    }

    //GT = greater than
    //LT = less than
    Obstacle obstacleGTCentreline = new Obstacle("obstacle1",10,10,200,50);
    Obstacle obstacleLTCentreline = new Obstacle("obstacle2",10,10,-200,50);
    Obstacle obstacleGTStripEnd = new Obstacle("obstacle3",10,10,50,10000);
    Obstacle obstacleLTStripEnd = new Obstacle("obstacle4",10,10,50,-70);
    @Test
    @DisplayName("Test if we needs to redeclare given obstacle and logicalRunway")
    void needRedeclare(){
        assertFalse(Calculator.needRedeclare(obstacleGTStripEnd, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleLTStripEnd, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleGTCentreline, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleLTCentreline, lR09L));
        assertTrue(Calculator.needRedeclare(obstacle1, lR09L));
    }

    Obstacle obstacleResa = new Obstacle("Fallen Tree",2,50,0,50);
    Obstacle obstacleBlast = new Obstacle("Fallen Tree",5,50,0,50);
    Obstacle obstacleAlsTocs = new Obstacle("ObstacleAlsTocs",10,50,0,50);

    @Test
    @DisplayName("Test if a right choice was given between TALO and TTLT")
    void getCalculationBreakdown(){
        assertEquals("Take-Off Away Landing Over",Calculator.getFlightMethod(obstacle1,lR09L));
        assertEquals("Take-Off Towards Landing Towards",Calculator.getFlightMethod(obstacle2,lR27R));
    }

    @Test
    void ldaBreakdownChoice(){
    }

    @Test
    void toraBreakdownChoice(){
    }

    @Test
    void printCalculationBreakdownT(){
    }
}
