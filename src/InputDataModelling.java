import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wagu.Block;
import wagu.Board;
import wagu.Table;
import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomSeedGenerator;


public class InputDataModelling 
{
	public final static int NUM_OBSERVATIONS = 30;
	public final static int OBSERVATION_INTERVAL = NUM_OBSERVATIONS * 30;  // observation interval, for each half hour block
	// Exponential means for interarrival times for components A and B
	public final static double[] MEAN_INTER_ARRVL_NORM = {6, 2.4, 2, 2, 1.71, 1.33, 0.92, 1, 1.09, 1.5, 1.71, 1.71, 1.5, 1.33, 1.2, 1};  // minutes
	public final static double[] MEAN_INTER_ARRVL_DELI = {4, 1, 1.09, 2, 4};  // minutes NOTE: Starts at 11:00 and goes till 1:30

	public static void main(String[] args) 
	{
		RandomSeedGenerator rsg = new RandomSeedGenerator();		
		for(int i=0; i<5; i++)
		{
			arrivalRun(rsg);
		}
	}
	
	public static void arrivalRun(RandomSeedGenerator rsg)
	{
		Exponential[] interArrival_Normal = new Exponential[16];
		Exponential[] interArrival_Deli = new Exponential[5];
		
		// Generate data for part A components over 5 weeks (5*7*26*60) = 50,400 minutes
		for(int i = 0; i < 16; i++)
			interArrival_Normal[i] = new Exponential(1/MEAN_INTER_ARRVL_NORM[i], new MersenneTwister(rsg.nextSeed()));
		for(int i = 0; i < 5; i++)
			interArrival_Deli[i] = new Exponential(1/MEAN_INTER_ARRVL_DELI[i], new MersenneTwister(rsg.nextSeed()));
		// Use ArrayLists to record the data:
		
		ArrayList<Double> arrivals = new ArrayList<Double>();
		int normalCount, bothCount, deliCount;
		double normalArrival, deliArrival;  // For arrivals of components
		MersenneTwister randGen = new MersenneTwister();
		
		double[] prcNorm = new double[16];
		double[] prcNormBoth = new double[16];
		double[] prcNormSingle = new double[16];
		double[] prcDeli = new double[16];
		for(int i = 0; i < 16; i++){
			normalCount = 0;
			bothCount = 0;
			deliCount = 0;
			// First arrival is from t = 0
			normalArrival = interArrival_Normal[i].nextDouble();
			if(4 <= i && i <= 8)
				deliArrival = interArrival_Deli[i - 4].nextDouble();
			else
				deliArrival = Double.POSITIVE_INFINITY;
			while(normalArrival < OBSERVATION_INTERVAL)
			{
				if(normalArrival <= deliArrival)  // should be rare that arrivals are equal
				{
					if(normalArrival == deliArrival) System.out.printf(" aArrival = bArrival at %f\n",normalArrival);  // Flag equality
					
					if(randGen.nextDouble() < 0.5){
						bothCount++;
					}
					// save aArrival as next arrival and get next value for aArrival
					arrivals.add(normalArrival);  // Example of autoboxing - double value in aArrival is automatically converted to a Double object
					normalArrival = normalArrival + interArrival_Normal[i].nextDouble();
					normalCount++;
				}
				else  // bArrival < aArrival
				{
					// save bArrival as next arrival and get next value for bArrival
					arrivals.add(deliArrival);  // Example of autoboxing - double value in aArrival is automatically converted to a Double object
					deliArrival = deliArrival + interArrival_Deli[i-4].nextDouble();	
					deliCount++;
				}
			}
			prcNorm[i] = (double)normalCount/(normalCount+deliCount);
			System.out.printf("The percentage of normal arrivals = %f %% (normalCount = %d)\n", prcNorm[i], normalCount);
			prcNormSingle[i] = (double)(normalCount-bothCount)/(normalCount+deliCount);
			System.out.printf("The percentage of normal arrivals visiting one counter = %f %% (Count = %d)\n", prcNormSingle[i], normalCount-bothCount);
			prcNormBoth[i] = (double)bothCount/(normalCount+deliCount);
			System.out.printf("The percentage of normal arrivals visiting both counters = %f %% (bothCount = %d)\n", prcNormBoth[i], bothCount);
			
			prcDeli[i] = (double)deliCount/(normalCount+deliCount);
			System.out.printf("The percentage of deli arrivals = %f %% (deliCount = %d)\n", prcDeli[i], deliCount);
			System.out.printf("Total number of arrivals = %d\n", (normalCount+deliCount));
			System.out.println();
		}
			
		// Lets compute the average interarrval rate and print the values for evaluation in Excel
		double arrival = 0.0;  // initialise current arrival to 0
		double nxtArrival;
		double sumInterArrivals = 0.0;  // for computing the average
		
		double[] avgInterArrival = new double[16];
		// For outputing to a file
		PrintStream outFileStream;
		try 
		{
			outFileStream = new PrintStream("data.csv");
			outFileStream.printf("Arrival Time, Inter Arrival Time\n");
			int cnt, i = 0;
			for(cnt = 0; cnt < arrivals.size(); cnt++)
			{
				nxtArrival = arrivals.get(cnt);
				if(nxtArrival - arrival > 0){
					outFileStream.printf("%f,%f\n", nxtArrival, nxtArrival - arrival);
					sumInterArrivals += nxtArrival - arrival;
				} else {
					outFileStream.printf("%f,%f\n", nxtArrival, nxtArrival);
					avgInterArrival[i] = sumInterArrivals/(cnt/(i+1));
					System.out.printf("Interarrival mean = %f, number of interarrival times = %d\n\n", avgInterArrival[i++], cnt);
					sumInterArrivals = 0;
				}
				arrival = nxtArrival;
			}
			
			outFileStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String[] timeOfDay = {"9-9:30", "9:30-10", "10-10:30","10:30-11","11-11:30","11:30-12","12-12:30","12:30-1","1-1:30","1:30-2","2-2:30","2:30-3","3-3:30","3:30-4", "4-4:30","4:30-5"};
		List<List<String>> sTats = new ArrayList<List<String>>();
		for (int i = 0; i< 16; i++)
		{
			ArrayList<String> s = new ArrayList<String>();
			s.add(timeOfDay[i]);
			s.add(String.valueOf(Math.round(prcNorm[i]*10000)/100.0));
			s.add(String.valueOf(Math.round(prcNormSingle[i]*10000)/100.0));
			s.add(String.valueOf(Math.round(prcNormBoth[i]*10000)/100.0));
			s.add(String.valueOf(Math.round(prcDeli[i]*10000)/100.0));
			//s.add(String.valueOf(total[i]));
			s.add(String.valueOf(Math.round(avgInterArrival[i]*100)/100.0));
			sTats.add(s);
		}
		
		Board boards = new Board(100);
		List<String> columnHeaders = Arrays.asList("Time frame", "Normal Customers(%)", "Normal Singe Counter(%)", "Normal Both Counter(%)", "Deli Customers (%)", "Average InterArrival Time");
		List<Integer> alignment = Arrays.asList(Block.DATA_BOTTOM_MIDDLE,Block.DATA_BOTTOM_MIDDLE,Block.DATA_BOTTOM_MIDDLE,Block.DATA_BOTTOM_MIDDLE, Block.DATA_BOTTOM_MIDDLE, Block.DATA_BOTTOM_MIDDLE);
		Table tbl = new Table(boards, 100, columnHeaders, sTats);
		tbl.setColAlignsList(alignment);
		String tblString = boards.setInitialBlock(tbl.tableToBlocks()).build().getPreview();
		System.out.println("The average percentage of normal customers is given below ");
		System.out.println(tblString);
	}

}
