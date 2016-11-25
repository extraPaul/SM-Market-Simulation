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
       double startTime=0.0, endTime=660.0;
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
       do{
    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i]);
        	  smMarket.runSimulation();
              // See examples for hints on collecting output
              // and developping code for analysis
        	  
        	  double min = 100;
        	  int minIndex = -1;
        	  try{
	        	  for(int j = 0; j < 18; j++){
	        		  if(smMarket.getHalfHourPercentDissatisfied(j) < min){
	        			  min = smMarket.getHalfHourPercentDissatisfied(j);
	        			  minIndex = j;
	        		  }
	        	  }
	        	  if(minIndex < 0)
	        		  throw new Exception();
        	  } catch(Exception e){
        		  System.out.println("Could not find minimum.");
        	  }
        	  
        	  int empStartTime = minIndex*30;
        	  int empShiftLength = 30;
        	  double index = 1, midle = minIndex;
        	  while(empShiftLength <= 6*60){
        		  if(midle - index > 0 && empStartTime >= 0){
        			  if(smMarket.getHalfHourPercentDissatisfied((int)(midle+index)) < smMarket.getHalfHourPercentDissatisfied((int)(midle-index))){
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
        			  if(smMarket.getHalfHourPercentDissatisfied((int)(midle+index)) > 0.1)
    					  empShiftLength += 30;
    				  else
    					  break;
        		  }
        	  }
           }
       }while(smMarket.getOverallPercentDissatisfied() > 0.1);
       
   }
}
