import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomSeedGenerator;


public class InputDataModelling 
{
	public final static int NUM_OBERVATIONS = 30;
	public final static int OBSERVATION_INTERVAL = NUM_OBERVATIONS * 30;  // observation interval, for each half hour block
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
		int normalCount=0, bothCount = 0, deliCount=0; // count number of A and B components
		double normalArrival, deliArrival;  // For arrivals of components
		MersenneTwister randGen = new MersenneTwister();
		for(int i = 0; i < 16; i++){
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
			System.out.printf("The percentage of normal arrivals = %f %% (normalCount = %d)\n", (double)normalCount/(normalCount+deliCount), normalCount);
			System.out.printf("The percentage of normal arrivals visiting one counter = %f %% (Count = %d)\n", (double)(normalCount-bothCount)/(normalCount+deliCount), normalCount-bothCount);
			System.out.printf("The percentage of normal arrivals visiting both counters = %f %% (bothCount = %d)\n", (double)bothCount/(normalCount+deliCount), bothCount);
			
			System.out.printf("The percentage of deli arrivals = %f %% (bCount = %d)\n", (double)deliCount/(normalCount+deliCount), deliCount);
			System.out.printf("Total number of arrivals = %d\n", (normalCount+deliCount));
			System.out.println();
		}
			
		// Lets compute the average interarrval rate and print the values for evaluation in Excel
		double arrival = 0.0;  // initialise current arrival to 0
		double nxtArrival;
		double sumInterArrivals = 0.0;  // for computing the average
		// For outputing to a file
		PrintStream outFileStream;
		try 
		{
			outFileStream = new PrintStream("data.csv");
			outFileStream.printf("Arrival Time, Inter Arrival Time\n");
			int cnt;
			for(cnt = 0; cnt < arrivals.size(); cnt++)
			{
				nxtArrival = arrivals.get(cnt);
				if(nxtArrival - arrival > 0){
					outFileStream.printf("%f,%f\n", nxtArrival, nxtArrival - arrival);
					sumInterArrivals += nxtArrival - arrival;
				} else {
					outFileStream.printf("%f,%f\n", nxtArrival, nxtArrival);
					System.out.printf("Interarrival mean = %f, number of interarrival times = %d\n\n", sumInterArrivals/cnt, cnt);
					sumInterArrivals = 0;
				}
				arrival = nxtArrival;
			}
			
			outFileStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
