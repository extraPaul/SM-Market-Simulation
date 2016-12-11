package smmSimModel;

import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import smmSimModel.Customer;

class RVPs 
{
	SMMarket model; // for accessing the clock
    // Data Models - i.e. random veriate generators for distributions
	// are created using Colt classes, define 
	// reference variables here and create the objects in the
	// constructor with seeds


	// Constructor
	protected RVPs(SMMarket model, Seeds sd) 
	{ 
		this.model = model; 
		// Set up distribution functions
		interArrDist = new Exponential(0, new MersenneTwister(sd.interArrivSd));
		
		randGen = new MersenneTwister(sd.type); //Init the general purpose RNG
		deliSrvTm = new TriangularVariate(STDMIN,STDAVG,STDMAX,new MersenneTwister(sd.deliSd)); // Build RNG distribution for deli srv time
		mDist1 = new Normal(STM1MEAN, STM1DEV, new MersenneTwister(sd.mDist1)); // lower meat distribution
		mDist2 = new Normal(STM2MEAN, STM2DEV, new MersenneTwister(sd.mDist2)); // upper meat distribution
	}
	
	/* Random Variate Procedure for Arrivals */
	private Exponential interArrDist;  // Exponential distribution for interarrival times
	//MEAN num of minutes between customer arrival for each 30 min block of day
	//NOTE: in the Case study, rates are given in customers/hour for each half hour block.

	// OLD private final double[] MEAN = {6, 2.4, 2, 2, 1.2, 0.572, 0.5, 0.667, 0.857, 1.5, 1.714, 1.714, 1.5, 1.333, 1.2, 1, 0, 0};
	private final double[] MEAN = {5.714,2.37,1.946,1.974,1.188,0.566,0.494,0.672,0.858,1.52,1.678,1.732,1.518,1.314,1.198,0.998};

	protected double duC()  // for getting next value of duC
	{
	    int timeBucket = (int)model.getClock() / 30; // Divide time into one of the discrete buckets
	    double nextArrival = model.getClock()+interArrDist.nextDouble(1.0/MEAN[timeBucket]);
        if(nextArrival > model.closingTime){
        	return -1.0;
        }else{
        	return nextArrival;
        }
	}
	
	//OLD private final double[] PROPD = {0,0.30,0.58,0.46,0.33,0.22,0}; // Probability of deli customer based on time block
	private final double[] PROPD = {0.0,0.0,0.0,0.0,31.836,56.942,46.344,33.26,21.99,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	private final double[] PROPM = {54.214,54.22,46.864,50.794,34.166,22.356,26.322,36.112,39.832,50.814,49.858,50.728,48.296,49.944,50.22,51.446};
	MersenneTwister randGen;
	/*
	 * Gives customer type based upon the time of day
	 */
	public Customer.Type uCustomerType()
	{
		// Determine which time block we're in
		int timeBucket = (int)model.getClock() / 30;
		
		double randNum = 100.0*randGen.nextDouble();
		
		if(randNum < PROPD[timeBucket]){
			model.output.numDeliCustomers++;
			return Customer.Type.D;
		}else if(randNum < PROPD[timeBucket]+PROPM[timeBucket]){
			model.output.numMnFCustomers++;
			return Customer.Type.M;
		}else{
			model.output.numBothCustomers++;
			return Customer.Type.MD;
		}
	}
	
	private final double PROBM1 = 0.804; // Prob of choosing from dist 1 of MNF
	private final double STM1MEAN = 3.463; // Mean of dist 1
	private final double STM1DEV = 1.091; // Std. Deviation of dist 1
	private final double STM2MEAN = 7.446; // Mean of dist 2
	private final double STM2DEV = 0.623; // Std. Deviation of dist 2
	private Normal mDist1;
	private Normal mDist2;
	/*
	 * Provide a service time for a customer at the Meat counter
	 */
	private double mnfSrvTm(){
		double srvTm = 0;
		srvTm = (PROBM1*mDist1.nextDouble()) + (1.0 - PROBM1)*mDist2.nextDouble();
		return srvTm;
	}
	
	private final double STDMIN = 1.7;
	private final double STDAVG = 5;
	private final double STDMAX = 8.05;
	private TriangularVariate deliSrvTm;
	/*
	 * Manager method to assign service time for a customer.
	 * Will call submethods as needed based on type
	 */
	public double uSrvTime(int type){
		double srvTm = 0;
		if(type == Constants.MNF){
			srvTm = mnfSrvTm();
		}else if(type == Constants.DELI){
			srvTm = deliSrvTm.next();
		}else{
			System.out.println("rvpuSrvTm - invalid type "+type);		
		}
		return(srvTm);
	}
	
	/*
	 * This method will give the particular time threshold 
	 * for a customer before getting displeased
	 */
	public double uDissatisfactionTime(Customer.Type type) {
		if(type == Customer.Type.D){
			return 10 + (5.0*randGen.nextDouble());
		}else if(type == Customer.Type.M){
			return 20 + (5.0*randGen.nextDouble());
		}else{
			return 40;
		}
	}

}
