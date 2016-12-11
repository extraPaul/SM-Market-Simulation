// File: Experiment.java
// Description:

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smmSimModel.*;
import cern.jet.random.engine.*;
import wagu.Block;
import wagu.Board;
import wagu.Table;

// Main Method: Experiments
// 
class Experiment
{
	
   public static void main(String[] args)
   {	   
	   
       int i, NUMRUNS = 30;
       double DISATISFACTION_THRESHOLD = 0.2;
       double startTime=0.0, endTime=480.0;
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

       //For printing
       List<String> columnNames = Arrays.asList("Time", "Dissatisfaction %", "NumEmployees", "DeliNumEmp", "MnFNumEmp");
       int numExperiments = 0;
       double overallDissatisfactionAvg;
       double numMnFCustomersAvg;
       double numDeliCustomersAvg;
       double numBothCustomersAvg;
       double numBalkingAvg;
       double employeesIdleTimeRatioAvg;
       do{
    	   overallDissatisfactionAvg = 0;
    	   double[] halfHourDissatisfactionAvg = new double[Constants.NUM_HALF_HOUR];
    	   double[] halfHourDeliEmpAvg = new double[Constants.NUM_HALF_HOUR];
    	   double[] halfHourMnFEmpAvg = new double[Constants.NUM_HALF_HOUR];
    	   numMnFCustomersAvg = 0;
           numDeliCustomersAvg = 0;
           numBothCustomersAvg = 0;
           numBalkingAvg = 0;
           employeesIdleTimeRatioAvg = 0;

    	   for(i=0 ; i < NUMRUNS ; i++){
        	  smMarket = new SMMarket(startTime,endTime, schedule, sds[i], false);
        	  smMarket.runSimulation();
              // See examples for hints on collecting output
              // and developping code for analysis

        	  
        	  //TEST
        	  //System.out.println("numDissatisfied: " + smMarket.getNumDissatisfied());
        	  //System.out.println("numServed: " + smMarket.getNumServed());

        	  overallDissatisfactionAvg += smMarket.getOverallPercentDissatisfied();

        	  //System.out.println("Overall dissatisfaction: " + smMarket.getOverallPercentDissatisfied());
        	  //System.out.print("Halfhour dissatisfaction: ");
        	  for(int j = 0; j < Constants.NUM_HALF_HOUR; j++){
        		  halfHourDissatisfactionAvg[j] += smMarket.getHalfHourPercentDissatisfied(j);
        		  halfHourDeliEmpAvg[j] += smMarket.getCounter(Constants.DELI).dailyNumEmp[j];
        		  halfHourMnFEmpAvg[j] += smMarket.getCounter(Constants.MNF).dailyNumEmp[j];
        	  }
        	  
        	  numMnFCustomersAvg += smMarket.getOutputs().numMnFCustomers;
        	  numDeliCustomersAvg += smMarket.getOutputs().numDeliCustomers;
        	  numBothCustomersAvg += smMarket.getOutputs().numBothCustomers;
        	  numBalkingAvg += smMarket.getOutputs().numBalking;
        	  employeesIdleTimeRatioAvg += smMarket.getOutputs().getEmployeesIdleTimeRatio();
    	   }
    	   
    	   
    	   numExperiments++;
    	   System.out.println("LOOP " + numExperiments + " ENDED");
    	   System.out.println("----------------------------------------------------------------------------------------------------");
    	   overallDissatisfactionAvg /= NUMRUNS;
    	   System.out.println("Average dissatisfation for " + NUMRUNS + " runs: " + Math.round(overallDissatisfactionAvg*10000)/100.0 +"%");
    		   
    	   numMnFCustomersAvg /= NUMRUNS;
    	   numDeliCustomersAvg /= NUMRUNS;
    	   numBothCustomersAvg /= NUMRUNS;
    	   numBalkingAvg /= NUMRUNS;
    	   employeesIdleTimeRatioAvg /= NUMRUNS;
    	   // sort the schedule by start time
           // schedule.sort(c);
    	   
    	   //For printing
    	   List<List<String>> rowList = new ArrayList<List<String>>();
    	   
    	  double max = 0;
     	  int maxIndex = 0;
     	  for(int j = 0; j < Constants.NUM_HALF_HOUR; j++){
     		  halfHourDissatisfactionAvg[j] /= NUMRUNS;
     		  halfHourDeliEmpAvg[j] /= NUMRUNS;
     		  halfHourMnFEmpAvg[j] /= NUMRUNS;
     		  
     		  rowList.add(new ArrayList<String>());
     		  String time;
     		  if(j < 6)
     			 time = (j/2 + 9) + ":" + 3*(j%2) + "0 am";
     		  else if(j < 8)
     			 time = "12:" + 3*(j%2) + "0 pm";
     		  else
     			 time = (j/2 - 3) + ":" + 3*(j%2) + "0 pm";
     		  rowList.get(j).add(time);
     		  rowList.get(j).add(Double.toString(Math.round(halfHourDissatisfactionAvg[j]*10000)/100.0)+"%");
     		  rowList.get(j).add(String.valueOf(smMarket.rEmployeesInfo.uTotalEmployees[j]));
     		  rowList.get(j).add(Double.toString(Math.round(halfHourMnFEmpAvg[j]*100)/100.0));
     		  rowList.get(j).add(Double.toString(Math.round(halfHourDeliEmpAvg[j]*100)/100.0));
     		  
     		  if(halfHourDissatisfactionAvg[j] > max){
     			  max = halfHourDissatisfactionAvg[j];
     			  maxIndex = j;
     		  }
     	  }
     	  System.out.println("The average number of meat and fish customers was : " + numMnFCustomersAvg);
     	  System.out.println("The average number of deli customers was : " + numDeliCustomersAvg);
     	  System.out.println("The average number of customers who visited both counters was : " + numBothCustomersAvg);
     	  System.out.println("The average number of customers who walked into the store and left immediately was : " + numBalkingAvg);
     	  System.out.println("The average idle ratio of employees: " + employeesIdleTimeRatioAvg);
     	  System.out.println("Schedule: ");
     	  printSchedule(schedule);
     	  
     	  System.out.println("The values of the following tables are averages from the last " + NUMRUNS + " runs.");
     	  Board board = new Board(100);
     	  Table table = new Table(board, 100, columnNames, rowList);
     	  List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
     	  table.setColAlignsList(colAlignList);
     	  String tableString = board.setInitialBlock(table.tableToBlocks()).build().getPreview();
     	  System.out.println(tableString);
     	  
    	  if(overallDissatisfactionAvg > DISATISFACTION_THRESHOLD){
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
	    	  
	    	  String time;
	  		  int start = empStartTime/30;
	  		  if(start < 6)
	  			  time = (start/2 + 9) + ":" + 3*(start%2) + "0 am";
	  		  else if(start < 8)
	  			  time = "12:" + 3*(start%2) + "0 pm";
	  		  else
	  			  time = (start/2 - 3) + ":" + 3*(start%2) + "0 pm";
	  		  
	    	  System.out.println("Added shift [" + time + ", " + empShiftLength + "].");
    	  }
    	  
    	  System.out.println("----------------------------------------------------------------------------------------------------");
     	  System.out.println();
    	  
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
       System.out.println("Average dissatisfation for the last " + NUMRUNS + " runs: " + Math.round(overallDissatisfactionAvg*10000)/100.0 +"%");
       System.out.println("Schedule:");
       printSchedule(schedule);
   }
   
   public static void printSchedule(ArrayList<ArrayList<Integer>> schedule){
	  Board boardS = new Board(65);
  	  List<List<String>> stringSchedule = new ArrayList<List<String>>();
  	  for(int j = 0; j < schedule.size(); j++){
  		  stringSchedule.add(new ArrayList<String>());
  		  int start = schedule.get(j).get(0);
  		  stringSchedule.get(j).add(String.valueOf(start));
  		  String time;
  		  start /= 30;
  		  if(start < 6)
  			  time = (start/2 + 9) + ":" + 3*(start%2) + "0 am";
  		  else if(start < 8)
  			  time = "12:" + 3*(start%2) + "0 pm";
  		  else
  			  time = (start/2 - 3) + ":" + 3*(start%2) + "0 pm";
  		  stringSchedule.get(j).add(time);
  		  stringSchedule.get(j).add(String.valueOf(schedule.get(j).get(1)));
  	  }
 	  Table tableS = new Table(boardS, 65, Arrays.asList("Start Time (min)", "Start Time", "Duration"), stringSchedule);
 	  List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
 	  tableS.setColAlignsList(colAlignList);
 	  String tableString = boardS.setInitialBlock(tableS.tableToBlocks()).build().getPreview();
 	  System.out.println(tableString);
   }
   

}

