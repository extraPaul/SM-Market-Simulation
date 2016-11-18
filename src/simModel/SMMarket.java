package simModel;

import java.util.ArrayList;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.SequelActivity;

//
// The Simulation model Class
public class SMMarket extends AOSimulationModel
{
	// Constants available from Constants class
	/* Parameter */
    
	//TODO:
	// rEmployeesInfo.schedule holds a 2D Array storing start time and duration of shifts
	EmployeesInfo rEmployeesInfo;

	/*-------------Entity Data Structures-------------------*/
	/* Group and Queue Entities */
	// Define the reference variables to the various 
	// entities with scope Set and Unary
	// Objects can be created here or in the Initialize Action
	
	protected Counter rgCounterMNF = new Counter(); // Meat and Fish Counter 
	protected Counter rgCounterDELI = new Counter(); // Deli Counter 
	protected ArrayList<Customer> qCustomerLineMNF = new ArrayList<Customer>(); // Meat and Fish Line
	protected ArrayList<Customer> qCustomerLineDELI = new ArrayList<Customer>(); // Deli Line
	
	
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
	public SMMarket(double t0time, double tftime, int[][] schedule, /*define other args,*/ Seeds sd)
	{
		// Initialize parameters here
		// Initialize EmployeesInfo with the input parameter schedule
		rEmployeesInfo = new EmployeesInfo(schedule);
		
		// Create RVP object with given seed
		rvp = new RVPs(this,sd);
		
		// rgCounter and qCustLine objects created in Initialize Action
		
		// Initialize the simulation model
		initAOSimulModel(t0time,tftime);   

		// Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		// Schedule other scheduled actions and activities here
		//StaffChange staffChangeAction = new StaffChange(this);
		//scheduleAction(staffChangeAction); // change in employees
		Arrivals arrival = new Arrivals(this);
		scheduleAction(arrival); // customer
	}

	/************  Implementation of Data Modules***********/	
	/*
	 * Testing preconditions
	 */
	protected void testPreconditions(Behaviour behObj)
	{
		reschedule (behObj);
		// Check preconditions of Conditional Activities

		// Check preconditions of Interruptions in Extended Activities
	}
	
	public void eventOccured()
	{
		//this.showSBL();
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


