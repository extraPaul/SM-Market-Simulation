package simModel;

import simulationModelling.ConditionalActivity;

public class Serving extends ConditionalActivity{

	SMMarket model;
	private Customer icCustomer;
	int id; //identifier for RG.Counters and Q.CustomerLines
	
	// Constructor
	public Serving(SMMarket model) {
		this.model = model;
	}
	
	protected static boolean precondition(SMMarket simModel) {
		//TODO: implement this
		boolean ready = false;
		if (simModel.udp.counterReadyToServe() != Constants.NONE) {
			ready = true;
		}
		
		return ready;
		
	}
	
	@Override
	protected double duration() {
		
		// returns serving time
		return model.rvp.uSrvTime(id);
		
	}

	@Override
	public void startingEvent() {
		
		// get counter identifier
		id = model.udp.counterReadyToServe();
		// get the first customer in the queue
		icCustomer = model.qCustomerLines.get(id).remove(0);
		
		// increment the number of customers at this counter
		model.rgCounters.get(id).insertList(icCustomer);
		
		// update the output variables
		model.udp.updateOutput(icCustomer);
		
	}

	@Override
	protected void terminatingEvent() {
		
		// remove customer from line
		if (model.rgCounters.get(id).removeList(icCustomer)){
			
			// check if customer needs to enter another line
			if (!icCustomer.canLeave) {
				
				// customer can now leave after visiting the second counter
				icCustomer.canLeave = true;
				
				// add customer to appropriate line
				if (id == Constants.MNF) {
					// add to Deli line
					model.qCustomerLines.get(Constants.DELI).add(icCustomer);
				} else {
					// add to MNF line
					model.qCustomerLines.get(Constants.MNF).add(icCustomer);
				}
			}
			
		} else {
			System.out.println("Error: Customer not at counter");
		}
	
	}

	
	
}
