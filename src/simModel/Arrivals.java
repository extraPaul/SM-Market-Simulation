package simModel;

import simulationModelling.ScheduledAction;

/**
 * WArrivals Scheduled Action
 * @author Saman
 *
 */
class Arrivals extends ScheduledAction {
	// Reference to model object
	SMMarket model;
	
	// Constructor
	public Arrivals(SMMarket model) {
		this.model = model;
	}
	
	public double timeSequence() {
		return model.rvp.duC();
	}
	
	public void actionEvent() {
		//WArrival Action Sequence SCS
		Customer icCustomer = new Customer();
		icCustomer.uType = model.rvp.uCustomerType();
		icCustomer.startWaitTime = model.getClock();
		icCustomer.dissatisfactionThreshold = model.rvp.uDissatisfactionTime(icCustomer.uType);
		
		// Add the arrived customer to the appropriate line
		if (icCustomer.uType == Customer.Type.M) {
			model.qCustomerLines.get(Constants.MNF).add(icCustomer);
		}
		else if (icCustomer.uType == Customer.Type.D) {
			model.qCustomerLines.get(Constants.MNF).add(icCustomer);
		}
		else if (icCustomer.uType == Customer.Type.MD) {
			
		}
		
	}
}
