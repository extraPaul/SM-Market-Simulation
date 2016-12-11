/* CSI4124/SYS5110 – Foundations of Modeling and Simulation
 * SM Market - Simulation Project
 * Fall 2016
 * 
 * Team Members: 
 * Paul Laplante
 * Saman Daneshvar
 * Matthew Gordon Yaraskavitch
 * Toluwalase Olufowobi
 * Ekomabasi Ukpong
 * Qufei Chen
 */

import java.util.ArrayList;
import java.util.Arrays;

import smmSimModel.*;
import cern.jet.random.engine.*;

class ValidationExperiment
{
   public static void main(String[] args)
   {
       int i = 0, NUMRUNS = 20; 
       double startTime=0.0, endTime=480.0;
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
