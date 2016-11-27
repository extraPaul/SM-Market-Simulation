// File: Experiment.java
// Description:

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import simModel.*;
import cern.jet.random.engine.*;

// Main Method: Experiments
// 
class Experiment
{
   public static void main(String[] args)
   {
       int i, NUMRUNS = 20; 
       double startTime=0.0, endTime=540.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMMarket smMarket = null;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       //TODO: make a legit schedule
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
       
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println(" Case 1");

       int numExperiments = 0;
       double overallDissatisfactionAvg;
       double numMnFCustomersAvg;
       double numDeliCustomersAvg;
       double numBothCustomersAvg;
       double numBalkingAvg;
       do{
    	   overallDissatisfactionAvg = 0;
    	   double[] halfHourDissatisfactionAvg = new double[18];
    	   numMnFCustomersAvg = 0;
           numDeliCustomersAvg = 0;
           numBothCustomersAvg = 0;
           numBalkingAvg = 0;

    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i]);
        	  smMarket.runSimulation();
              // See examples for hints on collecting output
              // and developping code for analysis

        	  
        	  //TEST
        	  //System.out.println("numDissatisfied: " + smMarket.getNumDissatisfied());
        	  //System.out.println("numServed: " + smMarket.getNumServed());

        	  overallDissatisfactionAvg += smMarket.getOverallPercentDissatisfied();

        	  //System.out.println("Overall dissatisfaction: " + smMarket.getOverallPercentDissatisfied());
        	  //System.out.print("Halfhour dissatisfaction: ");
        	  for(int j = 0; j < 17; j++){
        		  //System.out.print(smMarket.getHalfHourPercentDissatisfied(j) + "; ");
        		  halfHourDissatisfactionAvg[j] += smMarket.getHalfHourPercentDissatisfied(j);
        	  }
        	  //System.out.println(smMarket.getHalfHourPercentDissatisfied(17));
        	  halfHourDissatisfactionAvg[17] += smMarket.getHalfHourPercentDissatisfied(17);
        	  
        	  numMnFCustomersAvg += smMarket.getOutputs().numMnFCustomers;
        	  numDeliCustomersAvg += smMarket.getOutputs().numDeliCustomers;
        	  numBothCustomersAvg += smMarket.getOutputs().numBothCustomers;
        	  numBalkingAvg += smMarket.getOutputs().numBalking;
    	   }
    	   
    	   System.out.println();
    	   System.out.println("----------------------------------------------------------------------------------------------------");
    	   overallDissatisfactionAvg /= NUMRUNS;
    	   System.out.println("Average dissatisfation for " + NUMRUNS + " runs: " + overallDissatisfactionAvg);

    	   System.out.println("Average halfour dissatisfation for " + NUMRUNS + " runs: " + Arrays.toString(halfHourDissatisfactionAvg));
    		   
    	   numMnFCustomersAvg /= NUMRUNS;
    	   numDeliCustomersAvg /= NUMRUNS;
    	   numBothCustomersAvg /= NUMRUNS;
    	   numBalkingAvg /= NUMRUNS;
    	   
    	   System.out.println("The average number of meat and fish customers was : " + numMnFCustomersAvg);
    	   System.out.println("The average number of deli customers was : " + numDeliCustomersAvg);
    	   System.out.println("The average number of customers who visited both counters was : " + numBothCustomersAvg);
    	   System.out.println("The average number of customers who walked into the store and left immediately was : " + numBalkingAvg);
    	   System.out.println("uTotalEmp: " + Arrays.toString(smMarket.rEmployeesInfo.uTotalEmployees));
    	   System.out.println("Schedule: " + Arrays.deepToString(schedule.toArray()));
    	   System.out.println("----------------------------------------------------------------------------------------------------");
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
    	  while(empShiftLength < 6*60){
    		  if(midle - index > 0 && empStartTime >= 0){
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > halfHourDissatisfactionAvg[(int)(midle-index)]){
    				  if(halfHourDissatisfactionAvg[(int)(midle+index)] > 0.15 || empShiftLength < 3*60){
    					  empShiftLength += 30;
    					  midle += 0.5;
    					  index += 0.5;
    				  }
    				  else
    					  break;
    			  } else {
    				  if(halfHourDissatisfactionAvg[(int)(midle-index)] > 0.2 || empShiftLength < 3*60){
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
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > 0.2 || empShiftLength < 3*60){
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
    	  System.out.println("LOOP " + numExperiments + " ENDED");
    	   
    	  numExperiments++;
       }while(overallDissatisfactionAvg > 0.2);
       
       System.out.println();
       System.out.println("Satisfaction threshold met after " + numExperiments + " experiments.");
       
       
       //print schedule
       for (int x = 0; x< schedule.size(); x++) {
    	   System.out.println("start time: " + schedule.get(x).get(0));
    	   System.out.println("duration: " + schedule.get(x).get(1));
       }
       
       System.out.println();
       System.out.println("Daily labour cost for schedule: " + new DecimalFormat("$ #0.00").format(smMarket.getSechduleCost()));

       
       System.out.println(Arrays.deepToString(schedule.toArray()));

   }
}
