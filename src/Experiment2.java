// File: Experiment.java
// Description:

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import smmSimModel.*;
import cern.jet.random.engine.*;

// Main Method: Experiments
// 
class Experiment2
{
	
   public static void main(String[] args)
   {
       int i, NUMRUNS = 20; 
       double DISATISFACTION_THRESHOLD = 0.20;
       double startTime=0.0, endTime=540.0;
       Seeds[] sds = new Seeds[NUMRUNS];
       SMMarket smMarket = null;  // Simulation object

       // Custom Comparator used to sort the schedule by start time
       ScheduleComparator c = new ScheduleComparator();
       
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
       
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println("Case 1");

       int numExperiments = 0;
       double overallDissatisfactionAvg;
       double numMnFCustomersAvg;
       double numDeliCustomersAvg;
       double numBothCustomersAvg;
       double numBalkingAvg;
       
       //TEST
       ArrayList<ArrayList<Double>> deliWaitTimes = new ArrayList<ArrayList<Double>>();
       ArrayList<ArrayList<Double>> mnfWaitTimes = new ArrayList<ArrayList<Double>>();
       ArrayList<ArrayList<Double>> bothWaitTimes = new ArrayList<ArrayList<Double>>();
       
       do{
    	   overallDissatisfactionAvg = 0;
    	   double[] halfHourDissatisfactionAvg = new double[18];
    	   numMnFCustomersAvg = 0;
           numDeliCustomersAvg = 0;
           numBothCustomersAvg = 0;
           numBalkingAvg = 0;

    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i], true);
        	  smMarket.runSimulation();
        
        	  //TEST
        	  //System.out.println("numDissatisfied: " + smMarket.getNumDissatisfied());
        	  //System.out.println("numServed: " + smMarket.getNumServed());

        	  overallDissatisfactionAvg += smMarket.getOverallPercentDissatisfied();

        	  //System.out.println("Overall dissatisfaction: " + smMarket.getOverallPercentDissatisfied());
        	  //System.out.print("Halfhour dissatisfaction: ");
        	  for(int j = 0; j < 18; j++){
        		  halfHourDissatisfactionAvg[j] += smMarket.getHalfHourPercentDissatisfied(j);
        	  }
        	  
        	  numMnFCustomersAvg += smMarket.getOutputs().numMnFCustomers;
        	  numDeliCustomersAvg += smMarket.getOutputs().numDeliCustomers;
        	  numBothCustomersAvg += smMarket.getOutputs().numBothCustomers;
        	  numBalkingAvg += smMarket.getOutputs().numBalking;
        	  
        	  // TEST
        	  deliWaitTimes.add(smMarket.getOutputs().deliWaitTimes);
        	  mnfWaitTimes.add(smMarket.getOutputs().mnfWaitTimes);
        	  bothWaitTimes.add(smMarket.getOutputs().bothWaitTimes);
    	   }
    	   
    	   
    	   numExperiments++;
    	   System.out.println("LOOP " + numExperiments + " ENDED");
    	   System.out.println("----------------------------------------------------------------------------------------------------");
    	   overallDissatisfactionAvg /= NUMRUNS;
    	   System.out.println("Average dissatisfation for " + NUMRUNS + " runs: " + overallDissatisfactionAvg);
    		   
    	   numMnFCustomersAvg /= NUMRUNS;
    	   numDeliCustomersAvg /= NUMRUNS;
    	   numBothCustomersAvg /= NUMRUNS;
    	   numBalkingAvg /= NUMRUNS;
    	   // sort the schedule by start time
           // schedule.sort(c);
    	   
    	  double max = 0;
     	  int maxIndex = 0;
     	  for(int j = 0; j < 18; j++){
     		  halfHourDissatisfactionAvg[j] /= NUMRUNS;
     		  if(halfHourDissatisfactionAvg[j] > max){
     			  max = halfHourDissatisfactionAvg[j];
     			  maxIndex = j;
     		  }
     	  }
     	  System.out.println("Average half-hour dissatisfation for " + NUMRUNS + " runs: " + Arrays.toString(halfHourDissatisfactionAvg));
     	  System.out.println("The average number of meat and fish customers was : " + numMnFCustomersAvg);
			System.out.println("The average number of deli customers was : " + numDeliCustomersAvg);
			System.out.println("The average number of customers who visited both counters was : " + numBothCustomersAvg);
			System.out.println("The average number of customers who walked into the store and left immediately was : " + numBalkingAvg);
			System.out.println("uTotalEmp: " + Arrays.toString(smMarket.rEmployeesInfo.uTotalEmployees));
			System.out.println("Schedule: " + Arrays.deepToString(schedule.toArray()));
			System.out.println("----------------------------------------------------------------------------------------------------");
			System.out.println();
    	  
    	  int empStartTime = maxIndex*30;
    	  int empShiftLength = 30;
    	  double index = 1, midle = maxIndex;
    	  while(empShiftLength < 6*60){
    		  if(midle - index > 0 && empStartTime > 0){
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > halfHourDissatisfactionAvg[(int)(midle-index)]){
    				  if(halfHourDissatisfactionAvg[(int)(midle+index)] > DISATISFACTION_THRESHOLD || empShiftLength < 3*60){
    					  empShiftLength += 30;
    					  midle += 0.5;
    					  index += 0.5;
    				  }
    				  else
    					  break;
    			  } else {
    				  if(halfHourDissatisfactionAvg[(int)(midle-index)] > DISATISFACTION_THRESHOLD || empShiftLength < 3*60){
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
    			  if(halfHourDissatisfactionAvg[(int)(midle+index)] > DISATISFACTION_THRESHOLD || empShiftLength < 3*60){
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
    	  
    	  // sort the schedule
    	  schedule.sort(c);
       }while(overallDissatisfactionAvg > DISATISFACTION_THRESHOLD);
       
       System.out.println();
       System.out.println("Satisfaction threshold met after " + numExperiments + " experiments.");
       
       
       //print schedule
       /*for (int x = 0; x< schedule.size(); x++) {
    	   System.out.println("start time: " + schedule.get(x).get(0));
    	   System.out.println("duration: " + schedule.get(x).get(1));
       }*/
       
       System.out.println();
       System.out.println("Daily labour cost for schedule: " + new DecimalFormat("$ #0.00").format(smMarket.getSechduleCost()));       
       
       System.out.println("Schedule [Start time, duration]: " + Arrays.deepToString(schedule.toArray()));
       
       System.out.println("Deli Customer wait times:\n");
       
       
       // summary of customer wait times
       // each index holds the number of customers waiting this many minutes
       
       int[] deliTime = new int[35];
       int[] mnfTime = new int[35];
       int[] bothTime = new int[35];
       
       for (int x = 0; x< deliWaitTimes.size(); x++) {
    	   for (int y = 0; y < deliWaitTimes.get(x).size(); y++ ) {
    		   int temp = deliWaitTimes.get(x).get(y).intValue();
    		   if(temp < 34) {
    			   deliTime[temp]++;
    		   } else {
    			   deliTime[34]++;
    		   }
    	   }
       }
       for (int x = 0; x< mnfWaitTimes.size(); x++) {
    	   for (int y = 0; y < mnfWaitTimes.get(x).size(); y++ ) {
    		   int temp = mnfWaitTimes.get(x).get(y).intValue();
    		   if(temp < 34) {
    			   mnfTime[temp]++;
    		   } else {
    			   mnfTime[34]++;
    		   }
    	   }
       }
       for (int x = 0; x< bothWaitTimes.size(); x++) {
    	   for (int y = 0; y < bothWaitTimes.get(x).size(); y++ ) {
    		   int temp = bothWaitTimes.get(x).get(y).intValue();
    		   if(temp < 34) {
    			   bothTime[temp]++;
    		   } else {
    			   bothTime[34]++;
    		   }
    	   }
       }
       
       
       // print out total number of customers that waited [index] number of minutes
       System.out.println("\n\nNUMBERIC WAIT TIMES:");
       System.out.println("DELI WAIT TIMES:");
       for (int x = 0; x < deliTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
       }
       System.out.println();
       for (int x = 0; x < deliTime.length; x++) {
    	   System.out.print(String.format("%-6s", deliTime[x]));
       }
       System.out.println();
       
       System.out.println("MNF WAIT TIMES:");
       for (int x = 0; x < mnfTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
       }
       System.out.println();
       for (int x = 0; x < mnfTime.length; x++) {
    	   System.out.print(String.format("%-6s", mnfTime[x]));
       }
       System.out.println();
       
       System.out.println("BOTH WAIT TIMES:");
       for (int x = 0; x < bothTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
       }
       System.out.println();
       for (int x = 0; x < bothTime.length; x++) {
    	   System.out.print(String.format("%-6s", bothTime[x]));
       }
       System.out.println();
       
       // print out total percentage of customers that waited [index] number of minutes
       int deliSum = 0;
       int mnfSum = 0;
       int bothSum = 0;
       
       System.out.println("\n\nPERCENTAGE WAIT TIMES:");
       System.out.println("DELI WAIT TIMES:");
       for (int x = 0; x < deliTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
    	   deliSum += deliTime[x];
       }
       System.out.println();
       for (int x = 0; x < deliTime.length; x++) {
    	   System.out.print(String.format("%-6s", String.format("%.2f", ((double) deliTime[x]/ ((double) deliSum)))) );
       }
       System.out.println();
       
       System.out.println("MNF WAIT TIMES:");
       for (int x = 0; x < mnfTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
    	   mnfSum += mnfTime[x];
       }
       System.out.println();
       for (int x = 0; x < mnfTime.length; x++) {
    	   System.out.print(String.format("%-6s", String.format("%.2f", ((double) mnfTime[x]/ ((double) mnfSum)))) );
       }
       System.out.println();
       
       System.out.println("BOTH WAIT TIMES:");
       for (int x = 0; x < bothTime.length; x++) {
    	   System.out.print(String.format("%-6s", x));
    	   bothSum += bothTime[x];
       }
       System.out.println();
       for (int x = 0; x < bothTime.length; x++) {
    	   System.out.print(String.format("%-6s", String.format("%.2f", ((double) bothTime[x]/ ((double) bothSum)))) );
       }
       System.out.println();
       

   }
   

}

