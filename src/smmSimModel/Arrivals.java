package smmSimModel;

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
		Customer icCustomer = new Customer(model);
		
		// If the store is full (more than 30 customers in the store - including the lines and at the counters), the arrived customer will just leave. Else, he will stand in line.
		int totalNumCustomersInStore = model.qCustomerLines.get(Constants.MNF).size() + model.qCustomerLines.get(Constants.DELI).size() + model.rgCounters.get(Constants.MNF).getN() + model.rgCounters.get(Constants.DELI).getN();
		if (totalNumCustomersInStore > 30) {
			// Leave as a dissatisfied customer
			model.output.numDissatisfied++;
			model.rEmployeesInfo.halfHourNumDissatisfied++;
			model.output.numServed++;
			model.rEmployeesInfo.halfHourNumServed++;
			model.output.numBalking++;
		}
		else {
			// Add the arrived customer to the appropriate line
			model.udp.addArrivedCustomerToLine(icCustomer);
		}
	}
}
