package simModel;

import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
	SMMarket model;
	
	// Constructor
	protected Initialise(SMMarket model) { this.model = model; }

	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	protected double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	protected void actionEvent() 
	{
		// System Initialization
		model.rgCounters.get(Constants.MNF).list.clear();   // empties the list
		model.rgCounters.get(Constants.DELI).list.clear();   // empties the list
		model.qCustomerLines.get(Constants.MNF).clear();   // empties the line
		model.qCustomerLines.get(Constants.DELI).clear();   // empties the line
		
		// Initialize the output variables
		model.output.numServed = 0;
		model.output.numDissatisfied = 0;
		
	}

}
