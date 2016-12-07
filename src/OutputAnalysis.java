// File: OutputAnalysis.java
// Description:

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import smmSimModel.*;
import outputAnalysis.ConfidenceInterval;
import cern.jet.random.engine.*;
import wagu.Block;
import wagu.Board;
import wagu.Table;

// Main Method: Experiments
// 
class OutputAnalysis
{
	// For output analysis
	static final double CONF_LEVEL = 0.95;
	
   public static void main(String[] args)
   {	   
	   
       int i, NUMRUNS = 30;
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
       
       //Store Run Results for multiple loops
       ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>(NUMRUNS);
       //Init List with ArrayLists
       for(int k = 0; k<NUMRUNS; k++){
    	   results.add(new ArrayList<Double>());
       }
       // Loop Costs
       ArrayList<Double> costs = new ArrayList<Double>();
       
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
       do{
    	   overallDissatisfactionAvg = 0;
    	   double[] halfHourDissatisfactionAvg = new double[18];
    	   double[] halfHourDeliEmpAvg = new double[18];
    	   double[] halfHourMnFEmpAvg = new double[18];
    	   numMnFCustomersAvg = 0;
           numDeliCustomersAvg = 0;
           numBothCustomersAvg = 0;
           numBalkingAvg = 0;

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
        	  for(int j = 0; j < 18; j++){
        		  halfHourDissatisfactionAvg[j] += smMarket.getHalfHourPercentDissatisfied(j);
        		  halfHourDeliEmpAvg[j] += smMarket.getCounter(Constants.DELI).dailyNumEmp[j];
        		  halfHourMnFEmpAvg[j] += smMarket.getCounter(Constants.MNF).dailyNumEmp[j];
        	  }
        	  
        	  numMnFCustomersAvg += smMarket.getOutputs().numMnFCustomers;
        	  numDeliCustomersAvg += smMarket.getOutputs().numDeliCustomers;
        	  numBothCustomersAvg += smMarket.getOutputs().numBothCustomers;
        	  numBalkingAvg += smMarket.getOutputs().numBalking;
        	  
        	  // Store run results
        	  results.get(i).add(smMarket.getOverallPercentDissatisfied());
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
    	   // sort the schedule by start time
           // schedule.sort(c);
    	   
    	   //For printing
    	   List<List<String>> rowList = new ArrayList<List<String>>();
    	   
    	  double max = 0;
     	  int maxIndex = 0;
     	  for(int j = 0; j < 18; j++){
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
     		  rowList.get(j).add(Double.toString(halfHourMnFEmpAvg[j]));
     		  rowList.get(j).add(Double.toString(halfHourDeliEmpAvg[j]));
     		  
     		  if(halfHourDissatisfactionAvg[j] > max){
     			  max = halfHourDissatisfactionAvg[j];
     			  maxIndex = j;
     		  }
     	  }
     	  System.out.println("The average number of meat and fish customers was : " + numMnFCustomersAvg);
     	  System.out.println("The average number of deli customers was : " + numDeliCustomersAvg);
     	  System.out.println("The average number of customers who visited both counters was : " + numBothCustomersAvg);
     	  System.out.println("The average number of customers who walked into the store and left immediately was : " + numBalkingAvg);
     	  System.out.println("Schedule: ");
     	  printSchedule(schedule);
     	  
     	  System.out.println("The values of the following tables are averages from the last " + NUMRUNS + " runs.");
     	  Board board = new Board(100);
     	  Table table = new Table(board, 100, columnNames, rowList);
     	  List<Integer> colAlignList = Arrays.asList(Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER, Block.DATA_CENTER);
     	  table.setColAlignsList(colAlignList);
     	  String tableString = board.setInitialBlock(table.tableToBlocks()).build().getPreview();
     	  System.out.println(tableString);
     	  
    	  
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
    	  System.out.println("----------------------------------------------------------------------------------------------------");
     	  System.out.println();
    	  
    	  // sort the schedule
    	  schedule.sort(c);
    	  costs.add(smMarket.getSechduleCost());
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
       analysis(results,costs);
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
   
   public static void analysis(ArrayList<ArrayList<Double>> results, ArrayList<Double> costs){
	   
	   // Flip Array
	   double[][] transformed = new double[results.get(0).size()][results.size()];
	   for(int i=0; i<transformed.length; i++){
		   for(int j=0; j<transformed[0].length; j++){
			   transformed[i][j] = results.get(j).get(i);
		   }
	   }
	   
	   // Build Confidence Intervals
	   ConfidenceInterval[] avgPercentDissatisfied = new ConfidenceInterval[transformed.length];
	   for(int i=0; i<transformed.length; i++){
		   avgPercentDissatisfied[i] = new ConfidenceInterval(transformed[i], CONF_LEVEL);
	   }
	   
	   System.out.println("\n******** Data Analysis ********\n");
	   
	   // Title Bar
	   System.out.print("   Run |");
	   for(int j=0; j<results.get(0).size(); j++){
		   System.out.printf("  Loop %d |",j+1);
	   }
	   
	   // Separating line
	   System.out.print("\n-------+");
	   for(int j=0; j<results.get(0).size(); j++){
		   System.out.print("---------+");
	   }
	   
	   // Print data rows
	   System.out.println();
	   for(int i=0; i<results.size(); i++){
		   System.out.printf("%6d |", i+1);
		   for(int j=0; j<results.get(i).size(); j++){
			   System.out.printf("   %.2f  |",results.get(i).get(j));
		   }
		   System.out.print("\n");
	   }
	   
	   // Separating line
	   System.out.print("-------+");
	   for(int j=0; j<results.get(0).size(); j++){
		   System.out.print("---------+");
	   }

	   // Print PE
	   System.out.print("\n    PE |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf("   %.2f  |", avgPercentDissatisfied[i].getPointEstimate());
	   }
	   
	   // Print S(n)
	   System.out.print("\n  S(n) |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf("   %.2f  |", avgPercentDissatisfied[i].getStdDev());
	   }
	   
	   // Print zeta
	   System.out.print("\n  zeta |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf("   %.2f  |", avgPercentDissatisfied[i].getZeta());
	   }
	   
	   // Print CI Min
	   System.out.print("\nCI Min |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf("   %.2f  |", avgPercentDissatisfied[i].getCfMin());
	   }
	   
	   // Print CI Max
	   System.out.print("\nCI Max |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf("   %.2f  |", avgPercentDissatisfied[i].getCfMax());
	   }
	   
	   // Separating line
	   System.out.print("\n-------+");
	   for(int j=0; j<results.get(0).size(); j++){
		   System.out.print("---------+");
	   }
	   
	   // Print Cost
	   System.out.print("\n  Cost |");
	   for(int i=0; i<costs.size(); i++){
		   System.out.printf(" $%.2f |", costs.get(i));
	   }
	   
	   System.out.println();
   }
}

