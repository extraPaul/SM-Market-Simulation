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
       int overallDissatisfactionAvg;
       do{
    	   overallDissatisfactionAvg = 0;
    	   int[] halfHourDissatisfactionAvg = new int[18];
    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i]);
        	  smMarket.runSimulation();
              // See examples for hints on collecting output
              // and developping code for analysis
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
    	   for(int j = 0; j < 18; j++)
    		   halfHourDissatisfactionAvg[j] /= NUMRUNS;
    	   
    	   
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
    			  if(smMarket.getHalfHourPercentDissatisfied((int)(midle+index)) > smMarket.getHalfHourPercentDissatisfied((int)(midle-index))){
    				  if(smMarket.getHalfHourPercentDissatisfied((int)(midle+index)) > 0.1){
    					  empShiftLength += 30;
    					  midle += 0.5;
    					  index += 0.5;
    				  }
    				  else
    					  break;
    			  } else {
    				  if(smMarket.getHalfHourPercentDissatisfied((int)(midle-index)) > 0.1){
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
    			  if(smMarket.getHalfHourPercentDissatisfied((int)(midle+index)) > 0.1){
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
    	   
       }while(overallDissatisfactionAvg > 0.1);
       
   }
}
