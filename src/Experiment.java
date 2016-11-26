// File: Experiment.java
// Description:

import java.util.ArrayList;

import simModel.*;
import cern.jet.random.engine.*;

// Main Method: Experiments
// 
class Experiment
{
   public static void main(String[] args)
   {
       int i, NUMRUNS = 30; 
       double startTime=0.0, endTime=540.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMMarket smMarket = null;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       //TODO: make a legit schedule
       //schedule param
       ArrayList<ArrayList<Integer>> schedule = new ArrayList<ArrayList<Integer>>(2);
       
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println(" Case 1");

       int numExperiments = 0;
       double overallDissatisfactionAvg;
       do{
    	   overallDissatisfactionAvg = 0;
    	   double[] halfHourDissatisfactionAvg = new double[18];

    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i]);
        	  smMarket.runSimulation();
              // See examples for hints on collecting output
              // and developping code for analysis

        	  
        	  //TEST
        	  System.out.println("numDissatisfied: " + smMarket.getNumDissatisfied());
        	  System.out.println("numServed: " + smMarket.getNumServed());

        	  overallDissatisfactionAvg += smMarket.getOverallPercentDissatisfied();

        	  System.out.println("Overall dissatisfaction: " + smMarket.getOverallPercentDissatisfied());
        	  System.out.print("Halfhour dissatisfaction: ");
        	  for(int j = 0; j < 17; j++){
        		  System.out.print(smMarket.getHalfHourPercentDissatisfied(j) + "; ");
        		  halfHourDissatisfactionAvg[j] += smMarket.getHalfHourPercentDissatisfied(j);
        	  }
        	  System.out.println(smMarket.getHalfHourPercentDissatisfied(17));
        	  halfHourDissatisfactionAvg[17] += smMarket.getHalfHourPercentDissatisfied(17);
    	   }
    	   
    	   overallDissatisfactionAvg /= NUMRUNS;
    	   System.out.println("Average dissatisfation for " + NUMRUNS + " runs: " + overallDissatisfactionAvg);
    	   System.out.print("Average halfour dissatisfation for " + NUMRUNS + " runs: ");
    	   for(int j = 0; j < 18; j++){
    		   halfHourDissatisfactionAvg[j] /= NUMRUNS;
    		   System.out.print(halfHourDissatisfactionAvg[j] + "; ");
    	   }
    	   System.out.println();
    		   
    	   
    	   
    	  double max = 0;
    	  int maxIndex = 0;
    	  for(int j = 0; j < 18; j++){
    		  if(halfHourDissatisfactionAvg[j] > max){
    			  max = halfHourDissatisfactionAvg[j];
    			  maxIndex = j;
    		  }
    	  }
    	  
    	  int empStartTime = maxIndex*30;
    	  int empShiftLength = 30;
    	  double index = 1, midle = maxIndex;
    	  while(empShiftLength <= 6*60){
    		  if(midle - index > 0 && empStartTime >= 0){
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > halfHourDissatisfactionAvg[(int)(midle-index)]){
    				  if(halfHourDissatisfactionAvg[(int)(midle+index)] > 0.1 || empShiftLength < 3*60){
    					  empShiftLength += 30;
    					  midle += 0.5;
    					  index += 0.5;
    				  }
    				  else
    					  break;
    			  } else {
    				  if(halfHourDissatisfactionAvg[(int)(midle-index)] > 0.1 || empShiftLength < 3*60){
    					  empShiftLength += 30;
    					  empStartTime -= 30;
    					  if(midle - index - 1 > 0)
    						  midle -= 0.5;
    					  else
    						  midle += 0.5;
    					  index += 0.5;
    					  
    				  } else
    					  break;
    			  }
    		  } else {
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > 0.1 || empShiftLength < 3*60){
    				  empShiftLength += 30;
    				  midle += 0.5;
					  index += 0.5;
    			  }
				  else
					  break;
    		  }
    	  }
    	   
    	  ArrayList<Integer> newShift = new ArrayList<Integer>();
    	  newShift.add(empStartTime);
    	  newShift.add(empShiftLength);
    	  schedule.add(newShift);
    	  //TEST
    	  System.out.println("\nONE LOOP ENDED \n\n");
    	   
    	  numExperiments++;
       }while(overallDissatisfactionAvg > 0.8);
       
       System.out.println("Satisfaction threshold met after " + numExperiments + " experiments.");
       
       //print schedule
       for (int x = 0; x< schedule.size(); x++) {
    	   System.out.println("start time: " + schedule.get(x).get(0));
    	   System.out.println("duration: " + schedule.get(x).get(1));
       }
       
   }
}
