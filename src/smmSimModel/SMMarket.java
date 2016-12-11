package smmSimModel;

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
	//Public for testing purposes
	public EmployeesInfo rEmployeesInfo;

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
	
	// Add our closing time
	double closingTime;
	
	//For trace logging
	protected boolean printLog;

	// Output object
	protected Output output = new Output(this);
	
	
	// Output values - define the public methods that return values
	// required for experimentation.
	public double getOverallPercentDissatisfied(){
		return output.getOverallPercentageDissatisfied();
	}
	
	public double getHalfHourPercentDissatisfied(int i){
		return output.halfHourPercentDissatisfied[i];
	}
	
	public double[] getHalfHourPercentDissatisfied(){
		return output.halfHourPercentDissatisfied;
	}
	
	public double getNumDissatisfied(){
		return output.numDissatisfied;
	}
	
	public double getNumServed(){
		return output.numServed;
	}
	
	public Output getOutputs(){
		return output;
	}
	
	public double getSechduleCost(){
		return output.getScheduleCost();
	}
	
	public Counter getCounter(int i){
		return this.rgCounters.get(i);
	}
	
	

	// Constructor
	public SMMarket(double t0time, double tftime, ArrayList<ArrayList<Integer>> schedule,Seeds sd, boolean print)
	{
		// Initialize parameters here
		printLog = print;
		
		// Initialize EmployeesInfo with the input parameter schedule
		rEmployeesInfo = new EmployeesInfo(schedule, this);
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// Initialize Counters
		rgCounters.add(Constants.MNF, new Counter()); // Meat and Fish counter
		rgCounters.add(Constants.DELI, new Counter()); // Deli counter
		
		// Initialize Customer Lines
		qCustomerLines.add(Constants.MNF, new ArrayList<Customer>()); // Meat and Fish line
		qCustomerLines.add(Constants.DELI, new ArrayList<Customer>()); // Deli line
		
		// Initialize the simulation model
		initAOSimulModel(t0time);
		closingTime = tftime;
		
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
	
	private  Behaviour activity ;
	

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		activity = behObj;
		// reschedule scheduled actions
		reschedule (behObj);
		// Check preconditions of Conditional Activities

		// Check preconditions of Interruptions in Extended Activities
		if (Serving.precondition(this) == true) 
		{
			Serving act = new Serving(this);
			act.startingEvent();
			scheduleActivity(act);
		}
	}
	
	public void eventOccured()
	{
		
		
		if(printLog){
			this.showSBL();
			System.out.println("Clock: "+getClock()+
	                ", CustomerLineMNF.n: "+qCustomerLines.get(Constants.MNF).size()+
	                ", CustomerLineDEli.n: "+qCustomerLines.get(Constants.DELI).size()+
	                ", CounterMNF.n: "+rgCounters.get(Constants.MNF).getN()+ ", CounterMNF numEmp: " + rgCounters.get(Constants.MNF).uNumEmp +
	                ", CounterDELI.n: "+rgCounters.get(Constants.DELI).getN() + ", CounterDELI numEmp: " + rgCounters.get(Constants.DELI).uNumEmp);
			System.out.println("Number of cleaning emp: " + rEmployeesInfo.numEmpCleaning);
			System.out.println();
		}
	}

	// Standard Procedure to start Sequel Activities with no parameters
	protected void spStart(SequelActivity seqAct)
	{
		seqAct.startingEvent();
		scheduleActivity(seqAct);
		
	}	
	
	//Implicit stop condition
	public boolean implicitStopCondition() // termination explicit
	{
		boolean retVal = false;
		//System.out.println("ClosingTime = " + closingTime + "currentTime = "
		//		+ getClock() + "RG.Counter.n = " + rgCounter.size());
		if (getClock() >= closingTime && rgCounters.get(Constants.DELI).getN() + rgCounters.get(Constants.MNF).getN() == 0) {
			retVal = true;
			// set endTime in output
			output.finalEndTime = getClock();
		}
			
		//System.out.println("implicit stop condition returns " + retVal);

		return (retVal);
	}

}


