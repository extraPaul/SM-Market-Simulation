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
		model.qCustomerLines.get(Constants.DELI).add(icCustomer);
	}
}
