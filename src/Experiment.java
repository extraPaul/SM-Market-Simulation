// File: Experiment.java
// DescRIption:
// Testing!
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
       SMMarket smMarket;  // Simulation object

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
       
       // Loop for NUMRUN simulation runs for each case
       // Case 1
       System.out.println(" Case 1");
       for(i=0 ; i < NUMRUNS ; i++)
       {
    	  smMarket = new SMMarket(startTime,endTime,sds[i]);
    	  smMarket.runSimulation();
          // See examples for hints on collecting output
          // and developping code for analysis
       }
   }
}
