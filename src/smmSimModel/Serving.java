/* CSI4124/SYS5110 – Foundations of Modeling and Simulation
 * SM Market - Simulation Project
 * Fall 2016
 * 
 * Team Members: 
 * Paul Laplante
 * Saman Daneshvar
 * Matthew Gordon Yaraskavitch
 * Toluwalase Olufowobi
 * Ekomabasi Ukpong
 * Qufei Chen
 */

package smmSimModel;

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

		boolean ready = false;
		if (simModel.udp.counterReadyToServe() != Constants.NONE) {
			ready = true;
		}
		
		return ready;
		
	}
	
	@Override
	protected double duration() {
		// Get serving time
		double returnValue = model.rvp.uSrvTime(id);

		// Add the serving time to sumOfSrvTime
		model.output.addToSumOfSrvAndCleaningTime(returnValue);
		
		// Return the serving time
		return returnValue;
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
			
			//Check if an employee needs to switch counters
			if(model.rgCounters.get(id).scheduledEmpChange > 0){
				if (id == Constants.MNF) {
					//Move employee from MnF to Deli
					model.rgCounters.get(Constants.MNF).uNumEmp--;
					model.rgCounters.get(Constants.DELI).uNumEmp++;
				} else {
					//Move employee from Deli to MnF
					model.rgCounters.get(Constants.MNF).uNumEmp++;
					model.rgCounters.get(Constants.DELI).uNumEmp--;
				}
				model.rgCounters.get(id).scheduledEmpChange--;
			}
			
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
