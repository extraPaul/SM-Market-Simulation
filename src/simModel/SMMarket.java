package simModel;

import java.util.ArrayList;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.SequelActivity;

//
// The Simulation model Class
public class SMMarket extends AOSimulationModel
{
	/* Parameter */
    
	// rEmployeesInfo.schedule holds a 2D Array storing start time and duration of shifts
	EmployeesInfo rEmployeesInfo;

	/*-------------Entity Data Structures-------------------*/
	// Array lists containing the two counters
	ArrayList<Counter> rgCounters = new ArrayList<Counter>(2);
	ArrayList<ArrayList<Customer>> qCustomerLines = new ArrayList<ArrayList<Customer>>(2);
	
	/* Input Variables */
	// Define any Independent Input Variables here
	
	// References to RVP and DVP objects
	protected RVPs rvp;  // Reference to rvp object - object created in constructor
	protected DVPs dvp = new DVPs(this);  // Reference to dvp object
	protected UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output(this);
	
	// Output values - define the public methods that return values
	// required for experimentation.


	// Constructor
	public SMMarket(double t0time, double tftime, ArrayList<ArrayList<Integer>> schedule, /*define other args,*/ Seeds sd)
	{
		// Initialize parameters here
		// Initialize EmployeesInfo with the input parameter schedule
		rEmployeesInfo = new EmployeesInfo(schedule, this);
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// Initialize Counters
		rgCounters.set(Constants.MNF, new Counter()); // Meat and Fish counter
		rgCounters.set(Constants.DELI, new Counter()); // Deli counter
		
		// Initialize Customer Lines
		qCustomerLines.set(Constants.MNF, new ArrayList<Customer>()); // Meat and Fish line
		qCustomerLines.set(Constants.DELI, new ArrayList<Customer>()); // Deli line
		
		// Initialize the simulation model
		initAOSimulModel(t0time,tftime);   
		
		// Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		// Schedule staff rearrange
		StaffRearrange staffRearrange = new StaffRearrange(this);
		scheduleAction(staffRearrange);
		// Start arrivals
		Arrivals arrival = new Arrivals(this);
		scheduleAction(arrival);
	}
	
	public double getOverallPercentDissatisfied(){
		return output.overallPercentDissatisfied;
	}
	
	public double getHalfHourPercentDissatisfied(int i){
		return output.halfHourPercentDissatisfied[i];
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		// reschedule scheduled actions
		reschedule (behObj);
		// Check preconditions of Conditional Activities

		// Check preconditions of Interruptions in Extended Activities
		if (Serving.precondition(this) == true) {
			Serving act = new Serving(this);
			act.startingEvent();
			scheduleActivity(act);
		}
	}
	
	public void eventOccured()
	{
		this.showSBL();
		// Can add other debug code to monitor the status of the system
		// See examples for suggestions on setup logging
		
		// Setup an updateTrjSequences() method in the Output class
		// and call here if you have Trajectory Sets
		// updateTrjSequences() 
		
	}

	// Standard Procedure to start Sequel Activities with no parameters
	protected void spStart(SequelActivity seqAct)
	{
		seqAct.startingEvent();
		scheduleActivity(seqAct);
		
	}	

}


