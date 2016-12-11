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

class UDPs 
{
	SMMarket model;  // for accessing the clock
	
	// Constructor
	protected UDPs(SMMarket model) { this.model = model; }
	
	public void initializeUTotalEmp(){
		// derive halfHourSchedule from schedule
				
		for(int i = 0; i < this.model.rEmployeesInfo.uTotalEmployees.length; i++){
			//Add full time employees
			if(5 <= i && i <= 12)
				this.model.rEmployeesInfo.uTotalEmployees[i] = 4;
			else
				this.model.rEmployeesInfo.uTotalEmployees[i] = 2;
		}
				
		for (int i = 0; i < this.model.rEmployeesInfo.schedule.size(); i++) {
			int startTime = this.model.rEmployeesInfo.schedule.get(i).get(0);
			int durationOfShift = this.model.rEmployeesInfo.schedule.get(i).get(1);
					
			int tempCounter = 0;
			while (durationOfShift > 0) {
				this.model.rEmployeesInfo.uTotalEmployees[(startTime/30) + tempCounter]++;
				tempCounter++;
				durationOfShift -= 30;
			}
		}
	}

	// Check if MNF or Deli counter is ready to serve a customer
	// if both are busy, return NONE constant
	protected int counterReadyToServe(){
		
		int counterId = Constants.NONE;
		
		if ( (model.rgCounters.get(Constants.MNF).getN() < model.rgCounters.get(Constants.MNF).uNumEmp) && (model.qCustomerLines.get(Constants.MNF).size() != 0) ) {
			counterId = Constants.MNF;
		} else if ((model.rgCounters.get(Constants.DELI).getN() < model.rgCounters.get(Constants.DELI).uNumEmp) && (model.qCustomerLines.get(Constants.DELI).size() != 0) ) {
			counterId = Constants.DELI;
		}
		
		return counterId;
		
	}
	
	protected void addArrivedCustomerToLine(Customer icCustomer) {
		
		if (icCustomer.uType == Customer.Type.M)
			model.qCustomerLines.get(Constants.MNF).add(icCustomer);
		else if (icCustomer.uType == Customer.Type.D)
			model.qCustomerLines.get(Constants.DELI).add(icCustomer);
		else if (icCustomer.uType == Customer.Type.MD) {
			// Send the customer of type MD, to the shortest line
			int mnfLineLength = model.qCustomerLines.get(Constants.MNF).size();
			int deliLineLength = model.qCustomerLines.get(Constants.DELI).size();
			if (mnfLineLength <= deliLineLength)
				model.qCustomerLines.get(Constants.MNF).add(icCustomer);
			else
				model.qCustomerLines.get(Constants.DELI).add(icCustomer);
		}
	}
	
	/*
	 * Updates the output with numServed and numDissatisfied 
	 */
	protected void updateOutput(Customer icCustomer) {
		
		//check if the customer was dissatisfied by comparing their wait time with their dissatisfaction threshold
		if ( (model.getClock() - icCustomer.startWaitTime) > icCustomer.dissatisfactionThreshold) {
			model.output.numDissatisfied++;
			model.output.halfHourNumDissatisfied++;
		}	
		// increment the number of customers served
		model.output.numServed++;
		model.output.halfHourNumServed++;
	}
	
	protected void updateOutputOnHalfHour() {
		
		//Keeping track of history for testing
		model.rgCounters.get(Constants.DELI).dailyNumEmp[(int) (model.getClock()/30)] = model.rgCounters.get(Constants.DELI).uNumEmp;
		model.rgCounters.get(Constants.MNF).dailyNumEmp[(int) (model.getClock()/30)] = model.rgCounters.get(Constants.MNF).uNumEmp;
		
		// updates the halfHourPercentDissatisfied array
		if (model.output.halfHourNumServed != 0) {
			
			model.output.halfHourPercentDissatisfied[(int) (model.getClock()/30) -1] = ( (double) model.output.halfHourNumDissatisfied / (double) model.output.halfHourNumServed);
			// reset the halfHourNumServed and halfHourNumDissatisfied values
			model.output.resetHalfHourStats();
		}
		
	}
	
	/** Rebalance the employees between two counters
	 * based on the length of the lines
	 */
	protected void rebalanceEmployees() {
		
		boolean swtch = true;
		double mnfRatio, deliRatio, ratioDiff, testDiff;
		int tempMnFNumEmp = model.rgCounters.get(Constants.MNF).uNumEmp;
		int tempDeliNumEmp = model.rgCounters.get(Constants.DELI).uNumEmp;
		
		while (swtch) {
			mnfRatio = (double)model.qCustomerLines.get(Constants.MNF).size()/(double)tempMnFNumEmp;
			deliRatio = (double)model.qCustomerLines.get(Constants.DELI).size()/(double)tempDeliNumEmp;
			ratioDiff = Math.abs(mnfRatio - deliRatio);
			
			if (mnfRatio < deliRatio) {
				testDiff = Math.abs(((double)model.qCustomerLines.get(Constants.MNF).size()/(double)(tempMnFNumEmp-1)) - ((double)model.qCustomerLines.get(Constants.DELI).size())/(double)(tempDeliNumEmp+1));
				
				if ((testDiff < ratioDiff && model.rgCounters.get(Constants.MNF).uNumEmp - 1 >= model.rgCounters.get(Constants.MNF).getN() || model.rgCounters.get(Constants.DELI).uNumEmp == 0) && tempMnFNumEmp - 1 > 0) {
					model.rgCounters.get(Constants.MNF).uNumEmp--;
					tempMnFNumEmp--;
					model.rgCounters.get(Constants.DELI).uNumEmp++;
					tempDeliNumEmp++;
				} else if (testDiff < ratioDiff && tempMnFNumEmp - 1 > 0) {
					tempMnFNumEmp--;
					tempDeliNumEmp++;
					model.rgCounters.get(Constants.MNF).scheduledEmpChange++;
				} else {
					swtch = false;
				}
			} else {
				testDiff = Math.abs(((double)model.qCustomerLines.get(Constants.MNF).size()/(double)(tempMnFNumEmp+1)) - ((double)model.qCustomerLines.get(Constants.DELI).size()/(double)(tempDeliNumEmp-1)));
				
				if ((testDiff < ratioDiff && model.rgCounters.get(Constants.DELI).uNumEmp - 1 >= model.rgCounters.get(Constants.DELI).getN() || model.rgCounters.get(Constants.MNF).uNumEmp == 0) && tempDeliNumEmp - 1 > 0) {
					model.rgCounters.get(Constants.MNF).uNumEmp++;
					tempMnFNumEmp++;
					model.rgCounters.get(Constants.DELI).uNumEmp--;
					tempDeliNumEmp--;
				} else if (testDiff < ratioDiff && tempDeliNumEmp - 1 > 0){
					tempMnFNumEmp++;
					tempDeliNumEmp--;
					model.rgCounters.get(Constants.DELI).scheduledEmpChange++;
				} else {
					swtch = false;
				}
			}
		}
	}
	
	//Adds or removes employees to/from counters, when they start or stop cleaning/prep work.
	protected void assignCleaningDuty() {
		
		if (model.getClock() == 120) {
			//put prep employees back to counters
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
			model.rgCounters.get(Constants.DELI).uNumEmp += 2;
		}
		
		if (model.getClock() == 270) {
			//put remove employee from counter for restocking.
			if (model.rgCounters.get(Constants.DELI).uNumEmp > 0)
				model.rgCounters.get(Constants.DELI).uNumEmp--;
			else
				model.rgCounters.get(Constants.MNF).uNumEmp--;
			model.rEmployeesInfo.incrementNumEmpCleaning();
		}
		
		if (model.getClock() == 450) {
			//put prep employee back to counter
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
		}
		
	}
}
