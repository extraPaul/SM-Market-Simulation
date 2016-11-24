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
		interArrDist = new Exponential(1.0/WMEAN1, new MersenneTwister(sd.interArrivSd));
		deliSrvTm = new TriangularVariate(STDMIN,STDAVG,STDMAX,new MersenneTwister(sd.deliSd));
		mChooser = new Uniform(0,1,new MersenneTwister(sd.mChooserSd));
		mDist1 = new Normal(STM1MEAN, STM1DEV, new MersenneTwister(sd.mDist1));
		mDist2 = new Normal(STM2MEAN, STM2DEV, new MersenneTwister(sd.mDist2));
	}
	
	/* Random Variate Procedure for Arrivals */
	private Exponential interArrDist;  // Exponential distribution for interarrival times
	private final double WMEAN1=10.0;
	protected double duInput()  // for getting next value of duInput
	{
	    double nxtInterArr;

        nxtInterArr = interArrDist.nextDouble();
	    // Note that interarrival time is added to current
	    // clock value to get the next arrival time.
	    return(nxtInterArr+model.getClock());
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
		if(mChooser.nextBoolean()){
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
