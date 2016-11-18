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
			int numEmp = model.rEmployeesInfo.halfHourSchedule[0] - 3;
			model.rEmployeesInfo.numEmpCleaning += 3;
			model.rgCounterDELI.uNumEmp = numEmp/2;
			model.rgCounterMNF.uNumEmp = numEmp - numEmp/2;
		} else {
			int numExtraEmp = model.rEmployeesInfo.halfHourSchedule[(int)(model.getClock()/30)] - model.rEmployeesInfo.halfHourSchedule[(int)(model.getClock()/30) - 1];
			if(numExtraEmp > 0 || Math.abs(numExtraEmp) <= model.rgCounterMNF.uNumEmp){
				model.rgCounterMNF.uNumEmp += numExtraEmp;
			} else if(numExtraEmp < 0){
				//numExtraEmp is negative
				model.rgCounterDELI.uNumEmp += numExtraEmp + model.rgCounterMNF.uNumEmp;
				model.rgCounterMNF.uNumEmp = 0;
			}
		}
		
		//This might need to be changed depending on how
		//time is incremented.
		//Using t >= 120 won't work because then this will
		//be repeated multiple times.
		if(model.getClock() == 120){
			//put prep employees back to counters
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounterMNF.uNumEmp++;
			model.rgCounterDELI.uNumEmp += 2;
		}
		
		if(model.getClock() == 270){
			//put remove employee from counter for restocking.
			if(model.rgCounterDELI.uNumEmp > 0)
				model.rgCounterDELI.uNumEmp--;
			else
				model.rgCounterMNF.uNumEmp--;
			model.rEmployeesInfo.incrementNumEmpCleaning();
		}
		
		if(model.getClock() == 450){
			//put prep employee back to counter
			model.rEmployeesInfo.numEmpCleaning = 0;
			model.rgCounterMNF.uNumEmp++;
		}
		
		boolean swtch = true;
		double mnfRatio, deliRatio, ratioDiff, testDiff;
		while(swtch){
			mnfRatio = (double)model.rgCounterMNF.uNumEmp/(double)model.qCustomerLineMNF.size();
			deliRatio = (double)model.rgCounterDELI.uNumEmp/(double)model.qCustomerLineDELI.size();
			ratioDiff = Math.abs(mnfRatio - deliRatio);
			if(mnfRatio > deliRatio){
				testDiff = Math.abs(((double)(model.rgCounterMNF.uNumEmp-1)/(double)model.qCustomerLineMNF.size()) - ((double)(model.rgCounterDELI.uNumEmp+1)/(double)model.qCustomerLineDELI.size()));
				
				if(testDiff < ratioDiff){
					model.rgCounterMNF.uNumEmp--;
					model.rgCounterDELI.uNumEmp++;
				} else
					swtch = false;
			} else {
				testDiff = Math.abs(((double)(model.rgCounterMNF.uNumEmp+1)/(double)model.qCustomerLineMNF.size()) - ((double)(model.rgCounterDELI.uNumEmp-1)/(double)model.qCustomerLineDELI.size()));
				
				if(testDiff < ratioDiff){
					model.rgCounterMNF.uNumEmp++;
					model.rgCounterDELI.uNumEmp--;
				} else
					swtch = false;
			}
		}
		
	}
}
