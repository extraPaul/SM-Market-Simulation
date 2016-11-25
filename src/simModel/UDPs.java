package simModel;

class UDPs 
{
	SMMarket model;  // for accessing the clock
	
	// Constructor
	protected UDPs(SMMarket model) { this.model = model; }

	// Translate User Defined Procedures into methods
    /*-------------------------------------------------
	                       Example
	    protected int ClerkReadyToCheckOut()
        {
        	int num = 0;
        	Clerk checker;
        	while(num < model.NumClerks)
        	{
        		checker = model.Clerks[num];
        		if((checker.currentstatus == Clerk.status.READYCHECKOUT)  && checker.list.size() != 0)
        		{return num;}
        		num +=1;
        	}
        	return -1;
        }
	------------------------------------------------------------*/
	
	public void initializeUTotalEmp(){
		// derive halfHourSchedule from schedule
				this.model.rEmployeesInfo.uTotalEmployees = new int[18];
				
				for(int i = 0; i < this.model.rEmployeesInfo.uTotalEmployees.length; i++){
					if(5 <= i && i <= 12)
						this.model.rEmployeesInfo.uTotalEmployees[i] = 4;
					else
						this.model.rEmployeesInfo.uTotalEmployees[i] = 2;
				}
				
				for (int i = 0; i < this.model.rEmployeesInfo.schedule.size(); i++) {
					int startTime = this.model.rEmployeesInfo.schedule.get(i).get(0);
					int durationOfShift = this.model.rEmployeesInfo.schedule.get(i).get(1);
					
					this.model.rEmployeesInfo.uTotalEmployees[startTime/30]++;
					
					int tempCounter = 1;
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
	
	/**
	 * Add the arrived customer to the appropriate line
	 * @author Saman
	 */
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
			model.rEmployeesInfo.halfHourNumDissatisfied++;
		}
				
		// increment the number of customers served
		model.output.numServed++;
		model.rEmployeesInfo.halfHourNumServed++;
	}
	
	protected void updateOutputOnHalfHour() {
		
		// updates the halfHourPercentDissatisfied array
		model.output.halfHourPercentDissatisfied[(int) (model.getClock()/30)] = (model.rEmployeesInfo.halfHourNumDissatisfied / model.rEmployeesInfo.halfHourNumServed);
		
		// reset the halfHourNumServed and halfHourNumDissatisfied values
		model.rEmployeesInfo.resetHalfHourStats();
		
	}
	
	/** Rebalance the employees between two counters
	 * based on the length of the lines
	 */
	protected void rebalanceEmployees() {
		boolean swtch = true;
		double mnfRatio, deliRatio, ratioDiff, testDiff;
		while(swtch){
			mnfRatio = (double)model.rgCounters.get(Constants.MNF).uNumEmp/(double)model.qCustomerLines.get(Constants.MNF).size();
			deliRatio = (double)model.rgCounters.get(Constants.DELI).uNumEmp/(double)model.qCustomerLines.get(Constants.DELI).size();
			ratioDiff = Math.abs(mnfRatio - deliRatio);
			if(mnfRatio > deliRatio){
				testDiff = Math.abs(((double)(model.rgCounters.get(Constants.MNF).uNumEmp-1)/(double)model.qCustomerLines.get(Constants.MNF).size()) - ((double)(model.rgCounters.get(Constants.DELI).uNumEmp+1)/(double)model.qCustomerLines.get(Constants.DELI).size()));
				
				if(testDiff < ratioDiff){
					model.rgCounters.get(Constants.MNF).uNumEmp--;
					model.rgCounters.get(Constants.DELI).uNumEmp++;
				} else
					swtch = false;
			} else {
				testDiff = Math.abs(((double)(model.rgCounters.get(Constants.MNF).uNumEmp+1)/(double)model.qCustomerLines.get(Constants.MNF).size()) - ((double)(model.rgCounters.get(Constants.DELI).uNumEmp-1)/(double)model.qCustomerLines.get(Constants.DELI).size()));
				
				if(testDiff < ratioDiff){
					model.rgCounters.get(Constants.MNF).uNumEmp++;
					model.rgCounters.get(Constants.DELI).uNumEmp--;
				} else
					swtch = false;
			}
		}
	}
}
