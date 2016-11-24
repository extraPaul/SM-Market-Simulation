package simModel;

import cern.jet.random.Exponential;
import cern.jet.random.Uniform;
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
		interArrDist = new Exponential(1.0/WMEAN1,  
				                       new MersenneTwister(sd.seed1));
		deliSrvTm = new TriangularVariate(STDMIN,STDAVG,STDMAX,new MersenneTwister(sd.seed1));
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
	
	
	private double mnfSrvTm(){
		double srvTm = 0;
		
		
		
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
