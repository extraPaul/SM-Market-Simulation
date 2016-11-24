package simModel;

import simulationModelling.ScheduledAction;

public class StaffRearrange extends ScheduledAction {

	SMMarket model;
	
	public StaffRearrange(SMMarket model) {this.model = model;}
	
	@Override
	protected double timeSequence() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void actionEvent() {
		// TODO Auto-generated method stub
		if(model.getClock() == 0){
			int numEmp = model.rEmployeesInfo.uTotalEmployees[0] - 3;
			model.rEmployeesInfo.numEmpCleaning += 3;
			model.rgCounters.get(Constants.DELI).uNumEmp = numEmp/2;
			model.rgCounters.get(Constants.MNF).uNumEmp = numEmp - numEmp/2;
		} else {
			int numExtraEmp = model.rEmployeesInfo.uTotalEmployees[(int)(model.getClock()/30)] - model.rEmployeesInfo.uTotalEmployees[(int)(model.getClock()/30) - 1];
			if(numExtraEmp > 0 || Math.abs(numExtraEmp) <= model.rgCounters.get(Constants.MNF).uNumEmp){
				model.rgCounters.get(Constants.MNF).uNumEmp += numExtraEmp;
			} else if(numExtraEmp < 0){
				//numExtraEmp is negative
				model.rgCounters.get(Constants.DELI).uNumEmp += numExtraEmp + model.rgCounters.get(Constants.MNF).uNumEmp;
				model.rgCounters.get(Constants.MNF).uNumEmp = 0;
			}
		}
		
		//This might need to be changed depending on how
		//time is incremented.
		//Using t >= 120 won't work because then this will
		//be repeated multiple times.
		if(model.getClock() == 120){
			//put prep employees back to counters
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
			model.rgCounters.get(Constants.DELI).uNumEmp += 2;
		}
		
		if(model.getClock() == 270){
			//put remove employee from counter for restocking.
			if(model.rgCounters.get(Constants.DELI).uNumEmp > 0)
				model.rgCounters.get(Constants.DELI).uNumEmp--;
			else
				model.rgCounters.get(Constants.MNF).uNumEmp--;
			model.rEmployeesInfo.incrementNumEmpCleaning();
		}
		
		if(model.getClock() == 450){
			//put prep employee back to counter
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounters.get(Constants.MNF).uNumEmp++;
		}
		
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
