package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.Uniform;
import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simModel.Customer;

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
		mChooser = new Uniform(0,1,new MersenneTwister(sd.mChooserSd)); // Init the choose for which of the multimodal distributions to go from
		mDist1 = new Normal(STM1MEAN, STM1DEV, new MersenneTwister(sd.mDist1)); // lower meat distribution
		mDist2 = new Normal(STM2MEAN, STM2DEV, new MersenneTwister(sd.mDist2)); // upper meat distribution
	}
	
	/* Random Variate Procedure for Arrivals */
	private Exponential interArrDist;  // Exponential distribution for interarrival times
	//MEAN num of minutes between customer arrival for each 30 min block of day
	
	private final double[] MEAN = {3,1.2,1,1,0.6,0.286,0.25,0.333,0.429,0.75,0.857,0.857,0.75,0.667,0.6, 0.5, 0, 0};
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
	
	private final double[] PROPD = {0,0.30,0.58,0.46,0.33,0.22,0}; // Probability of deli customer based on time block
	private final double[] PROPM = {0.50,0.35,0.21,0.27,0.33,0.39,0.50}; // Probability of a meat customer based on time block
	//PROPMD is not needed as it will be inferred
	MersenneTwister randGen;
	/*
	 * Gives customer type based upon the time of day
	 */
	public Customer.Type uCustomerType()
	{
		double now = model.getClock();
		// Determine which time block we're in
		int timeBucket;
		if(now < 120){
			timeBucket = 0;
		}else if(now < 150){
			timeBucket = 1;
		}else if(now < 180){
			timeBucket = 2;
		}else if(now < 210){
			timeBucket = 3;
		}else if(now < 240){
			timeBucket = 4;
		}else if(now < 270){
			timeBucket = 5;
		}else{
			timeBucket = 6;
		}
		
		double randNum = randGen.nextDouble();
		
		if(randNum < PROPD[timeBucket]){
			return Customer.Type.D;
		}else if(randNum < (PROPD[timeBucket]+PROPM[timeBucket])){
			return Customer.Type.M;
		}else{
			return Customer.Type.MD;
		}
	}
	
	private Uniform mChooser; // Uniform distribution for choosing which standard dist to pick from
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
		if(mChooser.nextDouble() <= PROBM1){
			srvTm = mDist1.nextDouble();
		}else{
			srvTm = mDist2.nextDouble();
		}
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
