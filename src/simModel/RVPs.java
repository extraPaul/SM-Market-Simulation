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
		typeRandGen = new MersenneTwister(sd.type);
		deliSrvTm = new TriangularVariate(STDMIN,STDAVG,STDMAX,new MersenneTwister(sd.deliSd));
		mChooser = new Uniform(0,1,new MersenneTwister(sd.mChooserSd));
		mDist1 = new Normal(STM1MEAN, STM1DEV, new MersenneTwister(sd.mDist1));
		mDist2 = new Normal(STM2MEAN, STM2DEV, new MersenneTwister(sd.mDist2));
	}
	
	/* Random Variate Procedure for Arrivals */
	private Exponential interArrDist;  // Exponential distribution for interarrival times
	private final double[] MEAN = {3,1.2,1,1,0.6,0.285714286,0.25,0.333333333,0.428571429,0.75,0.857142857,0.857142857,0.75,0.666666667,0.6};
	protected double duC()  // for getting next value of duC
	{
	    double nxtInterArr;
	    int timeBucket = (int)model.getClock() % 30;
        nxtInterArr = interArrDist.nextDouble(1.0/MEAN[timeBucket]);
	    // Note that interarrival time is added to current
	    // clock value to get the next arrival time.
	    return(nxtInterArr+model.getClock());
	}
	
	private final double[] PROPD = {0,0.30,0.58,0.46,0.33,0.22,0};
	private final double[] PROPM = {0.50,0.35,0.21,0.27,0.33,0.39,0.50};
	//PROPMD is not needed as it will be inferred
	MersenneTwister typeRandGen;
	public Customer.Type uCustomerType()
	{
		double now = model.getClock();
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
		
		double randNum = typeRandGen.nextDouble();
		
		if(randNum < PROPD[timeBucket]){
			return Customer.Type.D;
		}else if(randNum < (PROPD[timeBucket]+PROPM[timeBucket])){
			return Customer.Type.M;
		}else{
			return Customer.Type.MD;
		}
	}
	
	private Uniform mChooser;
	private final double PROBM1 = 0.804; // Prob of choosing from dist 1 of MNF
	private final double STM1MEAN = 3.462874653; // Mean of dist 1
	private final double STM1DEV = 1.090941686; // Std. Deviation of dist 1
	private final double STM2MEAN = 7.445772096; // Mean of dist 2
	private final double STM2DEV = 0.622866405; // Std. Deviation of dist 2
	private Normal mDist1;
	private Normal mDist2;
	private double mnfSrvTm(){
		double srvTm = 0;
		if(mChooser.nextDouble() <= PROBM1){
			srvTm = mDist1.nextDouble();
		}else{
			srvTm = mDist2.nextDouble();
		}
		return srvTm;
	}
	
	/* Needs to be expanded for MD type */
	private final double STDMIN = 1.7;
	private final double STDAVG = 5;
	private final double STDMAX = 8.05;
	private TriangularVariate deliSrvTm;
	public double uSrvTime(Customer.Type type){
		double srvTm = 0;
		if(type == Customer.Type.M){
			srvTm = mnfSrvTm();
		}else if(type == Customer.Type.D){
			srvTm = deliSrvTm.next();
		}else{
			System.out.println("rvpuSrvTm - invalid type "+type);		
		}
		return(srvTm);
	}

}
