// File: ValidationExperiment.java
// Description:

import java.util.ArrayList;
import java.util.Arrays;

import smmSimModel.*;
import cern.jet.random.engine.*;

// Main Method: Experiments
// 
class ValidationExperiment2
{
   public static void main(String[] args)
   {
       int i = 0, NUMRUNS = 20; 
       double startTime=0.0, endTime=540.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMMarket smMarket = null;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       
       //schedule param
       ArrayList<ArrayList<Integer>> schedule = new ArrayList<ArrayList<Integer>>();
       
       //Add initial part time employees, for prep work.
       for(int j = 0; j < 3; j++){
    	   ArrayList<Integer> shift = new ArrayList<Integer>();
           shift.add(0);
           shift.add(180);
           schedule.add(shift);
           shift = new ArrayList<Integer>();
           shift.add(180);
           shift.add(330);
           schedule.add(shift);
       }
       for(int j = 0; j < 4; j++){
    	   ArrayList<Integer> shift = new ArrayList<Integer>();
           shift.add(150);
           shift.add(360);
           schedule.add(shift);
       }
       ArrayList<Integer> shift = new ArrayList<Integer>();
       shift.add(270);
       shift.add(180);
       schedule.add(shift);
       
       
       smMarket = new SMMarket(startTime,endTime, schedule, sds[0], true);
       smMarket.runSimulation();
       System.out.println("numDissatisfied: " + smMarket.getNumDissatisfied());
       System.out.println("numServed: " + smMarket.getNumServed());
       System.out.println("Overall dissatisfaction: " + smMarket.getOverallPercentDissatisfied());
       System.out.print("Halfhour dissatisfaction: " + Arrays.toString(smMarket.getHalfHourPercentDissatisfied()));
   }
}