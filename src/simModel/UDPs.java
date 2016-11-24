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

	// Counter MNF or Deli is ready to serve a customer
	protected int counterReadyToServe(){
		
		int counterId = Constants.NONE;
		
		if ( (model.rgCounters.get(Constants.MNF).getN() < model.rgCounters.get(Constants.MNF).uNumEmp) && (model.qCustomerLines.get(Constants.MNF).size() != 0) ) {
			counterId = Constants.MNF;
		} else if ((model.rgCounters.get(Constants.DELI).getN() < model.rgCounters.get(Constants.DELI).uNumEmp) && (model.qCustomerLines.get(Constants.DELI).size() != 0) ) {
			counterId = Constants.DELI;
		}
		
		return counterId;
		
	}
	
}
