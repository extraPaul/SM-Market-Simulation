import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomSeedGenerator;
import wagu.Block;
import wagu.Board;
import wagu.Table;

public class InputDataMod {


	public final static int ENDTIME = 480;
	public final static double[] MEAN_N = {6, 2.4, 2, 2, 1.7, 1.3, 0.9, 1, 1.1, 1.5, 1.7, 1.7, 1.5, 1.3, 1.2, 1};
	public final static double[] MEAN_D = {0, 0, 0, 0, 4, 1, 1.1, 2, 4, 0, 0, 0, 0, 0, 0, 0};
	
	public static void main(String[] args)
	{
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		
		 getArrivalStats(rsg);
		
	}

	public static void getArrivalStats(RandomSeedGenerator r)
	{
		//Generate 10 seeds to be used for each run of a time slot for both the normal and deli customers
		ArrayList<Exponential> interArrival_Ns = new ArrayList<Exponential>();
		ArrayList<Exponential> interArrival_Ds = new ArrayList<Exponential>();
		
		for (int idx = 0; idx<10; idx++)
		{
			Exponential iA_N = new Exponential(0, new MersenneTwister(r.nextSeed()));
			interArrival_Ns.add(iA_N);
			Exponential iA_D = new Exponential(0, new MersenneTwister(r.nextSeed()));
			interArrival_Ds.add(iA_D);
		}
		
		// declare a the arrival averages for each time slot
		double[] slotPercAverages_N = new double[16];
		double[] slotPercAverages_D = new double[16];
		double[] total = new double[16];
		double[] meanInterArrvl = new double[16];
		
		//Loop through the time slots :0 -15
		String[] timeOfDay = {"9-9:30", "9:30-10", "10-10:30","10:30-11","11-11:30","11:30-12","12-12:30","12:30-1","1-1:30","1:30-2","2-2:30","2:30-3","3-3:30","3:30-4", "4-4:30","4:30-5"};
		for (int slotIdx = 0 ; slotIdx < 16; slotIdx++)
		{
			System.out.println();
			System.out.println(timeOfDay[slotIdx] + " time slot");
			double AveProp_N = 0.0;
			double AveProp_D = 0.0;
			double AveTotal = 0.0;	
			double AveInterArrT = 0.0;
			
			//Get the appropriate arrival rate for the time slot
			
			double rateN = 1 / MEAN_N[slotIdx];
			double  rateD =  1 / MEAN_D[slotIdx];
			
			final int num_Runs = 10;
			
			//Run each slot 10 times and find the average of the arrival percentage for that time slot
			for (int runIdx = 0; runIdx < num_Runs; runIdx++)
			{
				System.out.println();
				System.out.println("Run " + (runIdx + 1));
				/**get the inter-arrival mean for both arrival types for the time slot
				using the appropriate Random seed generator and time slot value
				*/
				 final int TIMEINTERVAL =30;
				 
				Exponential Arr_N = interArrival_Ns.get(runIdx);
				Exponential Arr_D = interArrival_Ds.get(runIdx);
				
				ArrayList<Double> arrivalTimes = new ArrayList<Double>(); // to hold arrival times 
				 
				int normalCount = 0, deliCount = 0; // for the number of normal and deli customers that arrive during the run
				
				double nArrival, dArrival; 
				nArrival = Arr_N.nextDouble(rateN);
				dArrival =  Arr_D.nextDouble(rateD);
	
				while(nArrival <= TIMEINTERVAL && dArrival <= TIMEINTERVAL)
				{
					// if a normal customer arrives before or at the same time as a deli customer
					if(dArrival == 0)
					{
						arrivalTimes.add(nArrival);
						nArrival = nArrival + Arr_N.nextDouble(rateN);
						normalCount++;	
					}
					else{// if it's the lunch period
						if(nArrival <= dArrival)
						{
							if (nArrival == dArrival) // if both customer arrive at the same time, make a note of it
							{System.out.printf("Normal and Deli customer arrival at %f\n", nArrival);}
						//Save deli arrival as the next arrival and get the next arrival
						arrivalTimes.add(nArrival);
						nArrival = nArrival + Arr_N.nextDouble(rateN);
						normalCount++;
						}
						else// if a Deli customer arrives first
						{//Save deli arrival as the next arrival and get the next arrival
						arrivalTimes.add(dArrival);
						dArrival = dArrival + Arr_D.nextDouble(rateD);
						deliCount++;
						}
					}
				}
				// get the proportion of normal and deli customers in the time slot for the runIdx run 
				double propN =  (double) (normalCount)/(normalCount + deliCount);
				double propD =  (double) (deliCount)/(normalCount+deliCount);
				
				//add the values to the to the sum of run percentages for each arrival type
				AveProp_N += propN;
				AveProp_D += propD;	
				AveTotal += deliCount + normalCount;
				System.out.println("Total number of customers " +(deliCount + normalCount));
				System.out.printf("Proportion of normal customer = %f%% (Normal customers= %d)\n",(propN * 100), normalCount);
				System.out.printf("Proportion of deli customer = %f%% (Deli customers= %d)\n",(propD * 100), deliCount);
				
				//Get the mean interarrival time for the runIdx run for that timeslot
				double interArr= arrivalTimes.get(0);
				for (int j = 1; j< arrivalTimes.size(); j++)
				{
					 interArr += arrivalTimes.get(j) - arrivalTimes.get(j-1);
				}
				interArr = interArr/ arrivalTimes.size();
				AveInterArrT += interArr;
				System.out.printf("Mean interarrival time = %f \n", interArr );
		}
		//Get the average of the proportions for the time slot, Total number of customers and Interarrival mean then store their values,
			AveProp_N = AveProp_N / num_Runs;
			AveProp_D = AveProp_D/ num_Runs;
			AveTotal = AveTotal/num_Runs;
			AveInterArrT = AveInterArrT / num_Runs;
			
			slotPercAverages_N[slotIdx] = AveProp_N * 100;
			slotPercAverages_D[slotIdx] = AveProp_D * 100;
			total[slotIdx] = AveTotal;
			meanInterArrvl[slotIdx] = AveInterArrT;
		}
		
		
		List<List<String>> sTats = new ArrayList<List<String>>();
		for (int i = 0; i< 16; i++)
		{
			ArrayList<String> s = new ArrayList<String>();
			s.add(timeOfDay[i]);
			s.add(String.valueOf(slotPercAverages_N[i]));
			s.add(String.valueOf(slotPercAverages_D[i]));
			s.add(String.valueOf(total[i]));
			s.add(String.valueOf(meanInterArrvl[i]));
			sTats.add(s);
		}
		
		Board boards = new Board(100);
		List<String> columnHeaders = Arrays.asList("Time frame", "Normal Customers(%)", "Deli Customers (%)", "Total", "Mean Inter-Arrival (min)");
		List<Integer> alignment = Arrays.asList(Block.DATA_BOTTOM_LEFT,Block.DATA_BOTTOM_LEFT,Block.DATA_BOTTOM_LEFT,Block.DATA_BOTTOM_LEFT, Block.DATA_BOTTOM_MIDDLE);
		Table tbl = new Table(boards, 100, columnHeaders, sTats);
		tbl.setColAlignsList(alignment);
		String tblString = boards.setInitialBlock(tbl.tableToBlocks()).build().getPreview();
		System.out.println("The average percentage of normal customers is given below ");
		System.out.println(tblString);
	}
}
